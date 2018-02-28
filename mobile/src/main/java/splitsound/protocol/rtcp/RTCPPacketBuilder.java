package splitsound.protocol.rtcp;

/**
 * Created by J. McKernan on 2/25/2018.
 */

public class RTCPPacketBuilder
{
	private RTPSources sources;
	private RTPPacketBuilder rtpPacketBuilder;

	private boolean initialized;
	private long maxPacketSize;
	private double timestampUnit;
	private boolean firstPacket;
	private RTPTime prevBuildTime, transmissionDelay;

	private RTCPSDESInfoInternal ownSDESInfo;
	private int intervalName, intervalEmail, intervalLocation;
	private int intervalPhone, intervalTool, intervalNote;
	private boolean doName, doEmail, doLoc, doPhone, doTool, doNote;
	private boolean processingSDES;

	int SDESBuildCount;

	/** Creates an RTCPPacketBuilder instance.
	 *  Creates an instance which will use the source table sources and the RTP packet builder
	 *  rtpPackBuilder to determine the information for the next RTCP compound packet.
	 */
	public RTCPPacketBuilder(RTPSources s, RTPPacketBuilder pb) // TODO: both passed by reference, removed memory manager
	{
		sources = s;
		rtpPacketBuilder = pb;
		prevBuildTime = new RTPTime(0, 0);
		transmissionDelay = new RTPTime(0, 0);
		ownSDESInfo = new RTCPSDESInfoInternal();

		initialized = false;
	}

	/** Initializes the builder.
	 *  Initializes the builder to use the maximum allowed packet size maxPackSize, timestamp unit
	 *  tsUnit and the SDES CNAME item specified by cName.
	 *  The timestamp unit is defined as a time interval divided by the timestamp interval corresponding to
	 *  that interval: for 8000 Hz audio this would be 1/8000.
	 */
	public int init(long maxPackSize, double tsUnit, String cName) // TODO: cName was a void *
	{
		if (initialized)
			return RTPError.ERR_RTP_RTCPPACKETBUILDER_ALREADYINIT;
		if (maxPackSize < RTP_MINPACKETSIZE) // TODO: Convert constant
			return RTPError.ERR_RTP_RTCPPACKETBUILDER_ILLEGALMAXPACKSIZE;
		if (tsUnit < 0.0)
			return RTPError.ERR_RTP_RTCPPACKETBUILDER_ILLEGALTIMESTAMPUNIT;

		if (cName.length() > 255)
			cName = cName.substring(0, 255);

		maxPacketSize = maxPackSize;
		timestampUnit = tsUnit;

		int status;

		if ((status = ownSDESInfo.setCNAME(cName)) < 0)
			return status;

		clearAllSourceFlags();

		intervalName = -1;
		intervalEmail = -1;
		intervalLocation = -1;
		intervalPhone = -1;
		intervalTool = -1;
		intervalNote = -1;

		SDESBuildCount = 0;
		transmissionDelay = new RTPTime(0, 0);

		firstPacket = true;
		processingSDES = false;
		initialized = true;
		return 0;
	}

	public void destroy()
	{
		if (!initialized)
			return;
		ownSDESInfo.clear();
		initialized = false;
	}

	/** Sets the timestamp unit to be used to tsUnit.
	 *  Sets the timestamp unit to be used to tsUnit. The timestamp unit is defined as a time interval
	 *  divided by the timestamp interval corresponding to that interval: for 8000 Hz audio this would
	 *  be 1/8000.
	 */
	public int setTimestampUnit(double tsUnit)
	{
		if (!initialized)
			return RTPError.ERR_RTP_RTCPPACKETBUILDER_NOTINIT;
		if (tsUnit < 0)
			return RTPError.ERR_RTP_RTCPPACKETBUILDER_ILLEGALTIMESTAMPUNIT;

		timestampUnit = tsUnit;
		return 0;
	}

	/** Sets the maximum size allowed size of an RTCP compound packet to maxpacksize. */
	public int setMaximumPacketSize(long maxPackSize)
	{
		if (!initialized)
			return RTPError.ERR_RTP_RTCPPACKETBUILDER_NOTINIT;
		if (maxPackSize < RTP_MINPACKETSIZE)
			return RTPError.ERR_RTP_RTCPPACKETBUILDER_ILLEGALMAXPACKSIZE;

		maxPacketSize = maxPackSize;
		return 0;
	}

	public int setPreTransmissionDelay(RTPTime delay)
	{
		if (!initialized)
			return RTPError.ERR_RTP_RTCPPACKETBUILDER_NOTINIT;

		transmissionDelay = delay;
		return 0;
	}

	/** Builds the next RTCP compound packet which should be sent and stores it in pack. */
	public int buildNextPacket(RTCPCompoundPacket ** pack)
	{
		// Track various flags by reference
		FlagTracker flags = new FlagTracker();

		if (!initialized)
			return RTPError.ERR_RTP_RTCPPACKETBUILDER_NOTINIT;

		RTCPCompoundPacketBuilder rtcpCompPack;
		int status;
		boolean sender = false;
		RTPSourceData srcData;

		rtcpCompPack = RTCPCompoundPacketBuilder(); // TODO: Remove RTPNew call and out of mem check

		if ((status = rtcpCompPack.initBuild(maxPacketSize)) < 0)
			return status;

		if ((srcData = sources.getOwnSourceInfo()) != 0)
		{
			if (srcData.isSender())
				sender = true;
		}

		int ssrc = rtpPacketBuilder.getSSRC();
		RTPTime curTime = RTPTime.currentTime(); // TODO: :: call to currentTime()

		if (sender)
		{
			RTPTime rtpPackTime = rtpPacketBuilder.getPacketTime();
			int rtpPacketTimestamp = rtpPacketBuilder.getPacketTimestamp();
			int packCount = rtpPacketBuilder.getPacketCount();
			int octectCount = rtpPacketBuilder.getPayloadOctetCount();
			RTPTime diff = curTime;
			diff.minus(curTime);
			diff.plus(transmissionDelay); // the sample being sampled at this very instant will need a larger timestamp

			int tsDiff = (int)((diff.getDouble()/timestampUnit)+0.5);
			int rtpTimestamp = rtpPacketTimestamp + tsDiff;
			RTPNTPTime ntpTimestamp = curTime.getNTPTime();

			if ((status = rtcpCompPack.startSenderReport(ssrc, ntpTimestamp, rtpTimestamp, packCount, octectCount)) < 0)
			{
				if (status == RTPError.ERR_RTP_RTCPCOMPPACKBUILDER_NOTENOUGHBYTESLEFT)
					return RTPError.ERR_RTP_RTCPPACKETBUILDER_PACKETFILLEDTOOSOON;
				return status;
			}
		}
		else
		{
			if ((status = rtcpCompPack.startReceiverReport(ssrc)) < 0)
			{
				if (status == RTPError.ERR_RTP_RTCPCOMPPACKBUILDER_NOTENOUGHBYTESLEFT)
					return RTPError.ERR_RTP_RTCPPACKETBUILDER_PACKETFILLEDTOOSOON;
				return status;
			}
		}

		String ownCName;
		ownCName = ownSDESInfo.getCNAME();

		if ((status = rtcpCompPack.addSDESSource(ssrc)) < 0)
		{
			if (status == RTPError.ERR_RTP_RTCPCOMPPACKBUILDER_NOTENOUGHBYTESLEFT)
				return RTPError.ERR_RTP_RTCPPACKETBUILDER_PACKETFILLEDTOOSOON;
			return status;
		}
		if ((status = rtcpCompPack.addSDESNormalItem(RTCPSDESPacket.CNAME, ownCName)) < 0)
		{
			if (status == RTPError.ERR_RTP_RTCPCOMPPACKBUILDER_NOTENOUGHBYTESLEFT)
				return RTPError.ERR_RTP_RTCPPACKETBUILDER_PACKETFILLEDTOOSOON;
			return status;
		}

		if (!processingSDES)
		{
			// FlagTracker tracks reference to int added, skipped; bool full, atEndOfList

			if (status = fillInReportBlocks(rtcpCompPack, curTime, sources.getTotalCount(), flags) < 0)
			{
				return status;
			}

			if (flags.full && flags.added == 0)
			{
				return RTPError.ERR_RTP_RTCPPACKETBUILDER_PACKETFILLEDTOOSOON;
			}

			if (!flags.full)
			{
				processingSDES = true;
				sdesBuildCount++;

				clearAllSourceFlags();

				doName = false;
				doEmail = false;
				doLoc = false;
				doPhone = false;
				doTool = false;
				doNote = false;
				if (intervalName > 0 && ((SDESBuildCount % intervalName) == 0))
					doName = true;
				if (intervalEmail > 0 && ((SDESBuildCount % intervalEmail) == 0))
					doEmail = true;
				if (intervalLocation > 0 && ((SDESBuildCount % intervalLocation) == 0))
					doLoc = true;
				if (intervalPhone > 0 && ((SDESBuildCount % intervalPhone) == 0))
					doPhone = true;
				if (intervalTool > 0 && ((SDESBuildCount % intervalTool) == 0))
					doTool = true;
				if (intervalNote > 0 && ((SDESBuildCount % intervalNote) == 0))
					doNote = true;

				if ((status = fillInSDES(rtcpCompPack, flags)) < 0)
				{
					return status;
				}

			}
		}
	}

	/** Builds a BYE packet with reason for leaving specified by reason.
	 *  If useSRIfPossible is set to true, the RTCP compound packet will start with a sender report if
	 *  allowed. Otherwise, a receiver report is used.
	 */
	public int buildBYEPacket(RTCPCompoundPacket **pack, String reason, boolean useSRIfPossible)
	{

	}

	/** OVERLOAD OF ABOVE
	 * Builds a BYE packet with reason for leaving specified by reason.
	 *  If useSRIfPossible is set to true, the RTCP compound packet will start with a sender report if
	 *  allowed. Otherwise, a receiver report is used.
	 */
	public int buildBYEPacket(RTCPCompoundPacket **pack, String reason, boolean useSRIfPossible)
	{
		buildBYEPacket(pack, reason, true);
	}

	/** Sets the RTCP interval for the SDES name item.
	 *  After all possible sources in the source table have been processed, the class will check if other
	 *  SDES items need to be sent. If count is zero or negative, nothing will happen. If count
	 *  is positive, an SDES name item will be added after the sources in the source table have been
	 *  processed count times.
	 */
	public void setNameInterval(int count)
	{
		if (!initialized)
			return;
		intervalName = count;
	}

	/** Sets the RTCP interval for the SDES e-mail item.
	 *  After all possible sources in the source table have been processed, the class will check if other
	 *  SDES items need to be sent. If \c count is zero or negative, nothing will happen. If \c count
	 *  is positive, an SDES e-mail item will be added after the sources in the source table have been
	 *  processed \c count times.
	 */
	public void setEmailInterval(int count)
	{
		if (!initialized)
			return;
		intervalEmail = count;
	}

	/** Sets the RTCP interval for the SDES location item.
	 *  After all possible sources in the source table have been processed, the class will check if other
	 *  SDES items need to be sent. If count is zero or negative, nothing will happen. If count
	 *  is positive, an SDES location item will be added after the sources in the source table have been
	 *  processed count times.
	 */
	public void setLocationInterval(int count)
	{
		if (!initialized)
			return;
		intervalLocation = count;
	}

	/** Sets the RTCP interval for the SDES phone item.
	 *  After all possible sources in the source table have been processed, the class will check if other
	 *  SDES items need to be sent. If count is zero or negative, nothing will happen. If count
	 *  is positive, an SDES phone item will be added after the sources in the source table have been
	 *  processed count times.
	 */
	public void setPhoneInterval(int count)
	{
		if (!initialized)
			return;
		intervalPhone = count;
	}

	/** Sets the RTCP interval for the SDES tool item.
	 *  After all possible sources in the source table have been processed, the class will check if other
	 *  SDES items need to be sent. If count is zero or negative, nothing will happen. If count
	 *  is positive, an SDES tool item will be added after the sources in the source table have been
	 *  processed count times.
	 */
	public void setToolInterval(int count)
	{
		if (!initialized)
			return;
		intervalTool = count;
	}

	/** Sets the RTCP interval for the SDES note item.
	 *  After all possible sources in the source table have been processed, the class will check if other
	 *  SDES items need to be sent. If count is zero or negative, nothing will happen. If count
	 *  is positive, an SDES note item will be added after the sources in the source table have been
	 *  processed count times.
	 */
	public void setNoteInterval(int count)
	{
		if (!initialized)
			return;
		intervalNote = count;
	}

	/** Sets the SDES name item for the local participant to the value s. */
	public int setLocalName(String s)
	{
		if (!initialized)
			return RTPError.ERR_RTP_RTCPPACKETBUILDER_NOTINIT;
		return ownSDESInfo.setName(s);
	}

	/** Sets the SDES e-mail item for the local participant to the String s. */
	public int setLocalEmail(String s)
	{
		if (!initialized)
			return RTPError.ERR_RTP_RTCPPACKETBUILDER_NOTINIT;
		ownSDESInfo.setEmail(s);
	}

	/** Sets the SDES location item for the local participant to the String s. */
	public int setLocalLocation(String s)
	{
		if (!initialized)
			return RTPError.ERR_RTP_RTCPPACKETBUILDER_NOTINIT;
		ownSDESInfo.setLocation(s);
	}

	/** Sets the SDES phone item for the local participant to the String s. */
	public int setLocalPhone(String s)
	{
		if (!initialized)
			return RTPError.ERR_RTP_RTCPPACKETBUILDER_NOTINIT;
		ownSDESInfo.setLocation(s);
	}

	/** Sets the SDES tool item for the local participant to the String s. */
	public int setLocalTool(String s)
	{
		if (!initialized)
			return RTPError.ERR_RTP_RTCPPACKETBUILDER_NOTINIT;
		ownSDESInfo.setTool(s);
	}

	/** Sets the SDES note item for the local participant to the String s. */
	public int setLocalNote(String s)
	{
		if (!initialized)
			return RTPError.ERR_RTP_RTCPPACKETBUILDER_NOTINIT;
		ownSDESInfo.setNote(s);
	}

	/** Returns the own CNAME String. */
	public String getLocalCNAME()
	{
		if (!initialized)
			return RTPError.ERR_RTP_RTCPPACKETBUILDER_NOTINIT;
		return ownSDESInfo.getCNAME();
	}

	private void clearAllSourceFlags()
	{

	}

	/** Since Java cannot handle passing primitives by reference, update an object to reflect the changes. */
	private class FlagTracker
	{
		public int maxCount;
		public int added;
		public boolean full;
		public boolean atEndOfList;

		public boolean processedAll;
		public int itemCount;

		public FlagTracker()
		{
			this(0, 0, false, false, false, 0);
		}

		public FlagTracker(int maxCount, int added, boolean full, boolean atEndOfList)
		{
			this(maxCount, added, full, atEndOfList, false, 0);
		}

		public FlagTracker(boolean full, boolean processedAll, int itemCount)
		{
			this (0, 0, full, false, processedAll, itemCount);
		}

		public FlagTracker(int maxCount, int added, boolean full, boolean atEndOfList, boolean processedAll, int itemCount)
		{
			this.maxCount = maxCount;
			this.added = added;
			this.full = full;
			this.atEndOfList = atEndOfList
			this.processedAll = processedAll;
			this.itemCount = itemCount;
		}
	}

	private int fillInReportBlocks(RTCPCompoundPacketBuilder pack, RTPTime curTime, FlagTracker flags)
	{
		RTPSourceData srcData;
		int addedCount = 0;
		int skippedCount = 0;
		boolean done = false;
		boolean filled = false;
		boolean atend = false;
		int status;

		if (sources.goToFirstSource())
		{
			do
			{
				boolean shouldProcess = false;

				srcData = sources.getCurrentSourceInfo();
				if (srcData.isOwnSSRC()) // don't send to ourselves
				{
					if (srcData.INF_hasSentData()) // TODO: INF naming
					{
						if (firstPacket)
							shouldProcess = true;
						else
						{
							// p 35: only if rtp packets were received since the last RTP packet, a report block
							// should be added

							RTPTime lastRTPTime = srcData.INF_getLastRTPPacketTime();

							if (lastRTPTime > prevBuildTime)
								shouldProcess = true;
						}
					}
				}

				if (shouldProcess)
				{
					if (srcData.isProcessedInRTCP()) // already covered this one
					{
						skippedCount++;
					}
					else
					{
						int rrSSRC = srcData.getSSRC();
						int num = srcData.INF_getNumPacketsReceivedInInterval();
						int prevSeq = srcData.INF_getSavedExtendedSequenceNumber();
						int curSeq = srcData.INF_getExtendedHighestSequenceNumber();
						int expected = curSeq - prevSeq;
						byte fracLost; // TODO: correct byte range for number > 127

						if (expected < num) // got duplicates
							fracLost = 0;
						else
						{
							double lost = (double)(expected - num);
							double frac = lost / ((double)expected);
							fracLost = (byte)(frac * 256.0);
						}

						expected = curSeq - srcData.INF_getBaseSequenceNumber();
						num = srcData.INF_getNumPacketsReceived();

						int diff = expected - num;
						int packLost = diff; // TODO: was a pointer to a signed int

						int jitter = srcData.INF_getJitter();
						int lsr;
						int dlsr;

						if (!srcData.SR_hasInfo())
						{
							lsr = 0;
							dlsr = 0;
						}
						else
						{
							RTPNTPTime srTime = srcData.SR_getNTPTimestamp();
							int m = (srTime.getMSW() & 0xFFFF);
							int l = ((srtime.getLSW() >> 16) & 0xFFFF);
							lsr = ((m << 16) | 1);
							RTPTime diff = curTime;
							diff -= srcData.SR_getReceiveTime();
							double diff2 = diff.getDouble();
							diff2 *= 65536.0; // TODO: Why?
							dlsr = (int)diff2;
						}

						status = rtcpCompPack.addReportBlock(rrSSRC, fracLost, packLost, curSeq, jitter, lsr, dlsr);
						if (status < 0)
						{
							if (status == RTPError.ERR_RTP_RTCPCOMPPACKBUILDER_NOTENOUGHBYTESLEFT)
							{
								done = true;
								filled = true;
							}
							else
								return status;
						}
						else
						{
							addedCount++;
							if (addedCount >= maxCount)
							{
								done = true;
								if (!sources.goToNextSource())
									atEnd = true;
							}
							srcData.INF_startNewInterval();
							srcData.setProcessedInRTCP(true);
						}
					}
				}

				if (!done)
				{
					if (!source.goToNextSource())
					{
						atEnd = true;
						done = true;
					}
				}
			} while (!done);
		}

		flags.added = addedCount;
		flags.skipped = skippedCount;
		flags.full = filled;

		if (!atEnd) // search for available sources
		{
			boolean shouldProcess = false;

			do
			{
				srcData = sources.getCurrentSourceInfo();
				if (!srcData.isOwnSSRC()) // don't send to ourselves
				{
					if (!srcData.isCSRC()) // p 35: no reports should go to CSRCs
					{
						if (srcData.INF_hasSentData()) // if this isn't true, INF_getLastRTPPacketTime() won't make any sense
						{
							if (firstPacket)
								shouldProcess = true;
							else
							{
								// p 35: only if rtp packets were received since the last RTP packet, a report block
								// should be added

								RTPTime lastRTPTime = srcData.INF_getLastRTPPacketTime();

								if (lastRTPTime > prevBuildTime)
									shouldProcess = true;
							}
						}
					}
				}

				if (shouldProcess)
				{
					if (srcData.isProcessedInRTCP())
						shouldProcess = false;
				}

				if (!shouldProcess)
				{
					if (!sources.gotoNextSource())
						atEnd = true;
				}
			} while (!atEnd && !shouldProcess)
		}

		flags.atEndOfList = atEnd;
		return 0;
	}

	private int fillInSDES(RTCPCompoundPacketBuilder pack, boolean full, boolean processedAll, int added)
	{

	}

	private void clearAllSDESFlags()
	{

	}

	/**
	 * Subclass for storing SDES information internally.
	 */
	private class RTCPSDESInfoInternal extends RTCPSDESInfoInternal
	{

		private boolean pName, pEmail, pLocation, pPhone, pTool, pNote;

		public RTCPSDESInfoInternal()
		{
			clearFlags();
		}

		public void clearFlags()
		{
			pName = false;
			pEmail = false;
			pLocation = false;
			pPhone = false;
			pTool = false;
			pNote = false;
		}

		public boolean processedName()
		{
			return pName;
		}

		public boolean processedEmail()
		{
			return pEmail;
		}

		public boolean processedLocation()
		{
			return pLocation;
		}

		public boolean processedPhone()
		{
			return pPhone;
		}

		public boolean processedTool()
		{
			return pTool;
		}

		public boolean processedNote()
		{
			return pNote;
		}

		public void setProcessedName(boolean v)
		{
			pName = v;
		}

		public void setProcessedEmail(boolean v)
		{
			pEmail = v;
		}

		public void setProcessedLocation(boolean v)
		{
			pLocation = v;
		}

		public void setProcessedPhone(boolean v)
		{
			pPhone = v;
		}

		public void setProcessedTool(boolean v)
		{
			pTool = v;
		}

		public void setProcessedNote(boolean v)
		{
			pNote = v;
		}
	}
}
