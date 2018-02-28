package splitsound.session;

import java.util.ArrayList;

import splitsound.protocol.RTPConstants;
import splitsound.protocol.rtcp.RTCPPacket;
import splitsound.protocol.rtcp.RTCPPacketBuilder;
import splitsound.protocol.rtp.RTPPacket;
import splitsound.utilities.RTPError;
import splitsound.utilities.RTPTime;

/**
 * Created by Neel on 2/25/2018.
 */

public class RTPSession {
    private boolean changeIncomingData, changeOutgoingData, created;

    RTPRandom rtprnd;
    boolean deletertprnd;

    RTPTransmitter rtptrans;
    boolean created;
    boolean deleteTransmitter;
    boolean usingpollthread, needthreadsafety;
    boolean accepTownPackets;
    boolean user_bye_ifpossible;
    long maxPacketSize;
    double sessionBandwidth;
    double controlFragment;
    double senderMultiplier;
    double byeMultiplier;
    double memberMultiplier;
    double collisionMultiplier;
    double noteMultiplier;
    boolean sentPackets;

    RTPSessionSources sources;
    RTPPacketBuilder packetbuilder;
    RTCPScheduler rtcpsched;
    RTCPPacketBuilder rtcpbuilder;
    RTPCollisionList collisionlist;

    ArrayList<RTCPCompoundPacket> byepackets = new ArrayList<RTCPCompoundPacket>();


    public RTPSession(RTPRandom r, RTPMemoryManager mgr) // chain constructor
    {
        changeIncomingData = false;
        changeOutgoingData = false;
        created = false;
        timeinit.dummy();
    }

    public int create(RTPSessionParams sessParams, RTPTransmissionParams transparams, TransmissionProtocol protocol) {
        int status;

        if (created)
            return RTPError.ERR_RTP_SESSION_ALREADYCREATED;

        user_bye_ifpossible = sessParams.getSenderReportBye();
        sentPackets = false;

        // Check max packet size
        if ((maxPacketSize = sessParams.getMaximumPacketSize()) < RTPConstants.RTP_MINPACKETSIZE)
            return RTPError.ERR_RTP_SESSION_MAXPACKETSIZETOOSMALL;

        rtptrans = null;
        switch (protocol) {
            case IPv4UDPProto:
                rtptrans = RTPUDPv4Transmitter(GetMemoryManager());
                break;
            case IPv6UDPProto:
                rtptrans = RTPUDPv6Transmitter(GetMemoryManager());
                break;
            case ExternalProto:
                rtptrans = RTPExternalTransmitter(GetMemoryManager());
                break;
            case UserDefinedProto:
                rtptrans = NewUserDefinedTransmitter();
                if (rtptrans == 0)
                    return RTPError.ERR_RTP_SESSION_USERDEFINEDTRANSMITTERNULL;
                break;
            case TCPProto:
                rtptrans = RTPTCPTransmitter(GetMemoryManager());
                break;
            default:
                return RTPError.ERR_RTP_SESSION_UNSUPPORTEDTRANSMISSIONPROTOCOL;

        }
        deleteTransmitter = true;
        return internalCreate(sessParams);
    }

    public int internalCreate(RTPSessionParams sessParams) {
        int status;

        if ((status = packetbuilder.init(maxPacketSize)) < 0) {
            if (deleteTransmitter)
                RTPDelete(rtptrans, GetMemoryManager());
            return status;
        }
        if (sessParams.getUsePredefinedSSRC()) {
            packetbuilder.adjustSSRC(sessparams.getPredefinedSSRC());
        }

        sources.setProbationType(sessParams.getProbationType());

        if ((status = sources.createOwnSSRC(packetbuilder.getSSRC())) < 0) {
            packetbuilder.destroy();
            if (deletetransmitter)
                RTPDelete(rtptrans, GetMemoryManager());
            return status;
        }

        if ((status = rtptrans.setReceiveMode(sessParams.getReceiveMode())) < 0) {
            packetbuilder.destroy();
            sources.clear();
            if (deletetransmitter)
                RTPDelete(rtptrans, GetMemoryManager());
            return status;
        }

        // Intialize the RTCP packet builder
        double timestampUnit = sessParams.getOwnTimestampUnit();

        byte[] buffer = new byte[1024];
        long buffLen = buffer.length;
        String cName = sessParams.getCName();

        if (cName.length() == 0) {
            if ((status = createCName(buffer, buffLen, sessParams, getResolveLocalHostName())) < 0) {
                packetbuilder.destroy();
                sources.clear();
                if (deletetransmitter)
                    RTPDelete(rtptrans, GetMemoryManager());
                return status;
            }
        } else {
            RTP_copy(buffer, cName, buffLen);
            buffer[(int) buffLen - 1] = 0;
            buffLen = buffer.length;
        }

        if (status = rtcpbuilder.init(maxPacketSize, timestampUnit, buffer, buffLen) < 0) {
            packetbuilder.destroy();
            sources.clear();
            if (deletetransmitter)
                RTPDelete(rtptrans, GetMemoryManager());
            return status;
        }
        rtcpsched.reset();
        rtcpsched.setHeaderOverhead(rtptrans.getHeaderOverHead());

        RTCPSchedulerParams sParams = new RTCPSchedulerParams();

        sessionBandwidth = sessParams.getSessionBandwidth();
        controlFragment = sessParams.getControlTrafficFraction();

        if ((status = sParams.setRTCPBandwidth(sessionBandwidth * controlFragment))) {
            packetbuilder.destroy();
            sources.clear();
            rtcpbuilder.destroy();

            return status;
        }

        if ((status = sParams.setSenderBandwidthFraction(sessParams.getSenderFraction())) < 0) {
            packetbuilder.destroy();
            sources.clear();
            rtcpbuilder.destroy();

            return status;
        }

        if ((status = sParams.setMinimumTransmissionInterval(sessParams.getMinRTCPTransInterval())) < 0) {
            packetbuilder.destroy();
            sources.clear();
            rtcpbuilder.destroy();

            return status;
        }

        sParams.setStartUpHalfInterval(sessParams.isStartUpHalfInterval());
        sParams.setRequestImmediateBye(sessParams.isImmediateByeRequest());

        rtcpsched.setParameters(sParams);

        // Copy other parameters set in the SessionParams
        accepTownPackets = sessParams.isAcceptPackets();
        memberMultiplier = sessParams.getTimeoutMulitplier();
        senderMultiplier = sessParams.getSenderMultiplier();
        byeMultiplier = sessParams.getByeTimeout();
        collisionMultiplier = sessParams.getCollisionMultiplier();
        noteMultiplier = sessParams.getNoteMultiplier();
        return 0;
    }

    public void destroy()
    {
        if(!created)
            return;

        packetbuilder.destroy();
        rtcpbuilder.destro();
        rtcpsched.reset();
        collisionlist.clear();
        sources.clear();
        byepackets.clear();
        created = false;

    }

    public void byeDestroy(RTPTime maxWaitTime, String reason, long reasonlength)
    {
        if(!created)
            return;

        RTPTime stopTime = RTPTime.currentTime();
        stopTime.addTime(maxWaitTime);

        // Add the bye packet to the list if data is already sent
        RTCPCompoundPacket packet = new RTCPCompoundPacket();

        if(sentPackets)
        {
            int status;

            if(reasonlength > RTPConstants.RTCP_BYE_MAXREASONLENGTH)
                reason = reason.substring(0, (int)RTPConstants.RTCP_BYE_MAXREASONLENGTH);

            status = rtcpbuilder.buildBYEPacket(packet, reason, user_bye_ifpossible);

            if(status >= 0)
            {
                byepackets.add(packet);
                if(byepackets.size() == 1)
                    rtcpsched.scheduleBYEPacket(packet.getCompoundPacketLength());
            }
        }

        if(!byepackets.isEmpty())
        {
            boolean done = false;

            while(!done)
            {
                RTPTime curTime = RTPTime.currentTime();

                if(curTime.getTime() >= stopTime.getTime())
                    done = true;

                if(rtcpsched.isTime())
                {
                    packet = byepackets.get(0);

                    byepackets.remove(0);
                    sendRTCPData(packet.getCompoundPacketData(), packet.getCompoundPacketLength());

                    onSendRTCPCompoundPacket(packet);

                    if(!byepackets.isEmpty())
                        rtcpsched.scheduleBYEPacket(byepackets.get(0).getCompoundPacketLength());
                    else
                        done = true;
                }
                if(!done)
                    RTPTime.wait(new RTPTime(0, 100000));
            }
        }

        // Reset all components after use
        packetbuilder.destroy();
        rtcpbuilder.destroy();
        rtcpsched.destroy();
        rtcpsched.reset();
        collisionlist.clear();
        sources.clear();
        byepackets.clear();
        created = false;
    }

    public int addDestination(RTPAddress addr)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;
        return rtptrans.addDestination(addr);
    }

    public int deleteDestination(RTPAddress addr)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;
        return rtptrans.deleteDestination(addr);
    }

    public void clearDestinations()
    {
        if(!created)
            return;
        rtptrans.clearDestinations();
    }

    public boolean supportsMulitcasting()
    {
        if(!created)
            return false;
        return rtptrans.supportsMulticasting();
    }

    public int joinMulticastingGroup(RTPAddress addr)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;
        return rtptrans.joinMulticastingGroup(addr);
    }

    public int leaveMulticastingGroup(RTPAddress addr)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;
        return rtptrans.leaveMulticastingGroup();
    }

    public void leaveAllMulticastingGroups()
    {
        if(!created)
            return;
        return rtptrans.leaveAllMulticastingGroups();
    }

    public int sendPacket(const void data, long len) // TODO: const void data needs to go here after its determined
    {
        int status;

        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;
        if((status = packetbuilder.buildPacket(data, len)) < 0)
            return status;

        if((status = sendRTPData(packetbuilder.getPacket(), packetbuilder.getPacketLength())) < 0)
            return status;

        sources.sentRTPPacket();
        sentPackets = true;

        return 0;
    }

    public int sendPacket(const void data, long len, byte pt, boolean mark, int timeStampInc) // TODO: same as above
    {
        int status;
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        if((status = packetbuilder.buildPacket(data, len, pt, mark, timeStampInc)) < 0)
            return status;

        if((status = sendRTPData(packetbuilder.getPacket(), packetbuilder.getPacketLength())) < 0)
            return status;

        sources.sentRTPPacket();
        sentPackets = true;

        return 0;
    }

    public int sendPacketEx(const void data, long len, short hdrextID, const void hdrexdata, long numhdrextwords)
    {
        int status;

        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        if((status = packetbuilder.buildPacketEx(data, len, hdrextdata, numhdrextwords)) < 0)
            return status;

        if((status = sendRTPData(packetbuilder.getPacket(), packetbuilder.getPacketLength())) < 0)
            return status;

        sources.sentRTPPacket();
        sentPackets = true;

        return 0;
    }

    public int SendPacketEx(const void data,long len,byte pt,boolean mark,int timestampinc,short hdrextID,const void hdrextdata,long numhdrextwords)
    {
        int status;

        if (!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        if ((status = packetbuilder.buildPacketEx(data,len,pt,mark,timestampinc,hdrextID,hdrextdata,numhdrextwords)) < 0)
            return status;

        if ((status = sendRTPData(packetbuilder.getPacket(),packetbuilder.getPacketLength())) < 0)
            return status;

        sources.sentRTPPacket();
        sentPackets = true;

        return 0;
    }

    public int sendRTCPAppPacket(short subtype, byte[] name, const void appdata, long appdatalen)
    {
        int status;
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        int ssrc = packetbuilder.getSSRC();

        RTCPCompoundPacketBuilder pb = new RTCPCompoundPacketBuilder();

        status = pb.initBuild(maxPacketSize);

        if(status < 0)
            return status;

        // First packet in the RTCP CompoundPacket is a SR or RR
        if((status = pb.startRecieverReport(ssrc)) < 0)
            return status;

        if((status = pb.addSDESSource(ssrc)) < 0)
            return status;

        long ownCNAMElen = 0;
        String ownCNAME = rtcpbuilder.getLocalCNAME();

        if((status = pb.addSDESNormalItem(RTCPSDESPacket.CNAME, ownCNAME, ownCNAMElen)) < 0)
            return status;

        // Add the application specific packet here
        if((status = pb.addAppPacket(subtype, ssrc, name, appdata, appdatalen)) < 0)
            return status;

        if((status = pb.endBuild()) < 0)
            return status;

        // Send packet
        if((status = sendRTCPData(pb.getCompoundPacket(), pb.getCompoundPacketLength())) < 0)
            return status;

        sentPackets = true;

        return pb.getCompoundPacketLength();
    }

    public int sendUnknownPacket(boolean sr, short payload_type, short subtype, const void* data, long len)
    {
        int status;
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        int ssrc = packetbuilder.getSSRC();

        RTCPCompoundPacketBuilder rtcpComPack = new RTCPCompoundPacketBuilder();
        if(rtcpComPack == null)
            return RTPError.ERR_RTP_OUTOFMEM;

        status = rtcpComPack.initBuild(maxPacketSize);

        if(sr)
        {
            // Setup for RTCP
            RTPTime time = packetbuilder.getPacketTime();
            int timestamp = packetbuilder.getPacketTimestamp();
            int packCount = packetbuilder.getPacketCount();
            int octetCount = packetbuilder.getOctetCount();
            RTPTime currTime = RTPTime.currentTime();
            RTPTime diff = currTime;

            diff.subtractTime(diff);
            diff.addTime(new RTPTime(0, 0));

            double tsUnit = 90000;

            int tsDiff = (int)((diff.getTime()/tsUnit)+0.5);
            int rtpTS = timestamp + tsDiff;
            RTPTime.RTPNTPTime ntime = currTime.getNTPTime();


            // First packet should be SR or RR
            if((status = rtcpComPack.startSenderReport(ssrc, ntime, rtpTS, packCount, octetCount)) < 0)
                return status;

        }
        else
        {
            // First packet should be SR or RR
            if((status = rtcpComPack.startRecieverReport(ssrc)) < 0)
                return status;
        }

        // Add SDES packet with CNAME
        if((status = rtcpComPack.addSDESSource(ssrc)) < 0)
            return status;

        long ownCNAMElen = 0;
        String ownCNAME = rtcpbuilder.getLocalCNAME();

        if((status = rtcpComPack.addSDESNormalItem(RTCPSDESPacket.CNAME, ownCNAME, ownCNAMElen)) < 0)
            return status;

        //add our packet
        if((status = rtcpComPack.addUnknownPacket(payload_type, subtype, ssrc, data, len)) < 0)
            return status;

        if((status = rtcpComPack.endBuild()) < 0)
            return status;

        //send packet
        status = sendRTCPData(rtcpComPack.GetCompoundPacketData(), rtcpComPack.GetCompoundPacketLength());
        if((status = sendRTCPData(rtcpComPack.GetCompoundPacketData(), rtcpComPack.GetCompoundPacketLength())) < 0)
            return status;

        sentPackets = true;

        onSendRTCPCompoundPacket(rtcpComPack);

        return rtcpComPack.getCompoundPacketLength();
    }

    public int sendRawData(const void data, long len, boolean useRTPChannel)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        int status;
        if(useRTPChannel)
            status = rtptrans.sendRTPData(data, len);
        else
            status = rtptrans.sendRTCPData(data, len);

        return status;
    }

    public int setDefaultPayloadType(byte pt)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        int status;
        status = packetbuilder.setDefaultPayloadType(pt);

        return status;
    }

    public int setDefaultMark(boolean m)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        int status;
        status = packetbuilder.setDefaultMark(m);

        return status;
    }

    public int setDefaultTimestampIncrement(int inc)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        int status;
        status = packetbuilder.setDefaultTimestampIncrement(inc);

        return status;
    }

    public int incrementTimestamp(int inc)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        int status;
        status = packetbuilder.incrementTimestamp(inc);

        return status;
    }

    public int setIncrementTimestampDefault()
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        int status;
        status = packetbuilder.incrementTimestampDefault();

        return status;
    }

    public int setPreTransmissionDelay(RTPTime delay)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        int status;
        status = packetbuilder.setPreTransmissionDelay(delay);

        return status;
    }

    public RTPTransmissionInfo getTransmissionInfo()
    {
        if(!created)
            return null;

        return rtptrans.getTransmissionInfo();
    }

    public int abortWait()
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        return rtptrans.abortWait();
    }

    public int waitForIncomingData(RTPTime delay, boolean dataAvailable)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        return rtptrans.waitForIncomingData(delay, dataAvailable);
    }

    public RTPTime getRTCPDelay()
    {
        if(!created)
            return new RTPTime(0, 0);

        RTPTime t = rtcpsched.getTransmissionDelay();

        return t;
    }
    public int beginDataAccess()
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        return 0;
    }

    public boolean goToFirstSource()
    {
        if(!created)
            return false;

        return sources.goToFirstSource();
    }

    public boolean goToNextSource()
    {
        if(!created)
            return false;

        return sources.goToNextSource();
    }

    public boolean goToPreviousSource()
    {
        if(!created)
            return false;

        return sources.goToPreviousSource();
    }

    public boolean goToFirstSourceWithData()
    {
        if(!created)
            return false;

        return sources.goToFirstSourceWithData();
    }

    public boolean goToPreviousSourceWithData()
    {
        if(!created)
            return false;

        return sources.goToPreviousSourceWithData();
    }

    public RTPSourceData getCurrentSourceInfo()
    {
        if(!created)
            return null;

        return sources.getCurrentSourceInfo();
    }

    public RTPSourceData getNextPacket()
    {
        if(!created)
            return 0;

        return sources.getNextPacket();
    }

    public short getNextSequenceNumber()
    {
        return packetbuilder.getSequenceNumber();
    }

    public void deletePacket(RTPPacket p)
    {
        return;
    }

    public int endDataAccess()
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        return 0;
    }

    public int setReceiveMode(RTPTransmitter.RecieveMode m)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        return rtptrans.setRecieveMode(m);
    }

    public int addToIgnoreList(RTPAddress addr)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        return rtptrans.addToIgnoreList(addr);
    }

    public int deleteFromIgnoreList(RTPAddress addr)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        return rtptrans.deleteFromIgnoreList(addr);
    }

    public void clearIgnoreList()
    {
        if(!created)
            return;

        rtptrans.clearIgnoreList();
    }

    public int addToAcceptList(RTPAddress addr)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        return rtptrans.addToAcceptList(addr);
    }

    public int deleteFromAcceptList(RTPAddress addr)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        return rtptrans.deleteFromAcceptList(addr);
    }

    public void clearAcceptList()
    {
        if(!created)
            return;

        rtptrans.clearAcceptList();
    }

    public int setMaximumPacketSize(long s)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        if(s < RTPConstants.RTP_MINPACKETSIZE)
            return RTPError.ERR_RTP_SESSION_MAXPACKETSIZETOOSMALL;

        int status;

        if((status = rtptrans.setMaximumPacketSize(s)) < 0)
            return status;

        if((status = packetbuilder.setMaximumPacketSize(s)) < 0)
        {
            packetbuilder.setMaximumPacketSize(maxPacketSize);
            return status;
        }
        if((status = rtcpbuilder.setMaximumPacketSize(s)) < 0)
        {
            packetbuilder.setMaximumPacketSize(maxPacketSize);
            rtptrans.setMaximumPacketSize(maxPacketSize);
            return status;
        }

        maxPacketSize = s;
        return 0;
    }

    public int setSessionBandwidth(double dw)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        int status;
        RTCPSchedulerParams p = rtcpsched.getParameters();
        status = p.setRTCPBandwidth(bw * controlFragment);
        if(status >= 0)
        {
            rtcpsched.setParameters(p);
            sessionBandwidth = bw;
        }
        return status;
    }

    public int setTimestampUnit(double u)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        int status;
        status = rtcpbuilder.setTimestampUnit(u);

        return status;
    }

    public void setNameInterval(int count)
    {
        if(!created)
            return;

        rtcpbuilder.setNameInterval(count);
    }

    public void setEmailinterval(int count)
    {
        if(!created)
            return;

        rtcpbuilder.setEmailInterval(count);
    }

    public void setLocationInterval(int count)
    {
        if(!created)
            return;

        rtcpbuilder.setLocationInterval(count);
    }

    public void setPhoneInterval(int count)
    {
        if(!created)
            return;

        rtcpbuilder.setLocationInterval(count);
    }

    public void setToolInterval(int count)
    {
        if(!created)
            return;

        rtcpbuilder.setNoteInterval(count);
    }

    public int setLocalName(String s, long len)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        int status;
        status = rtcpbuilder.setLocalName(s);

        return status;
    }

    public int setLocalEmail(String s, int len)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        return rtcpbuilder.setLocalEmail(s);
    }

    public int setLocalLocation(String s, int len)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        return rtcpbuilder.setLocalLocation(s);
    }

    public int setLocalPhone(String s, int len)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        return rtcpbuilder.setLocalPhone(s);
    }

    public int setLocalTool(String s, int len)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        return rtcpbuilder.setLocalTool(s);
    }

    public int setLocalNote(String s, int len)
    {
        if(!created)
            return RTPError.ERR_RTP_SESSION_NOTCREATED;

        return rtcpbuilder.setLocalNote(s);
    }

    //TODO: Place the random generator in RTPRandom class
    public RTPRandom GetRandomNumberGenerator(RTPRandom r)
    {
        RTPRandom rnew = null;

        if (r == null)
        {
            rnew = RTPRandom.createDefaultRandomNumberGenerator();
            deletertprnd = true;
        }
        else
        {
            rnew = r;
            deletertprnd = false;
        }

        return rnew;
    }

    public int sendRTPData(const void data, long len)
    {
        if(!changeOutgoingData)
            return rtptrans.sendRTPData(data, len);

        void pSendData = 0;
        long sendLen = 0;
        int status = 0;

        status = onChangeRTPOrRTCPData(data, len, true, pSendData, sendLen);
        if(status < 0)
            return status;

        if(pSendData)
        {
            status = rtptrans.sendRTPData(pSendData, sendLen);
            onSendRTPOrRTCPData(pSendData, sendLen, true);
        }

        return status;
    }

    public int sendRTCPData(const void data, long len)
    {
        if(!changeOutgoingData)
            return rtptrans.sendRTCPData(data, len);

        void pSendData = 0;
        long sendLen = 0;
        int status = 0;

        status = onChangeRTPOrRTCPData(data, len, false, pSendData, sendLen);

        if(status < 0)
            return status;

        if(pSendData)
        {
            status = rtptrans.sendRTCPData(pSendData, sendLen);
            onSentRTPOrRTCPData(pSendData, sendLen, false);
        }

        return status;
    }

    public void dumpSources()
    {
        beginDataAccess();
        sources.dump();
        endDataAccess();
    }

    public void dumpTransmitter()
    {
        if(created)
            rtptrans.dump();
    }
}