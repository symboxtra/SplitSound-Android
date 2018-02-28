package splitsound.session;

import splitsound.protocol.RTPConstants;
import splitsound.utilities.*;

/**
 * Created by Neel on 2/25/2018.
 */

public class RTPSessionParams
{
    private boolean acceptown;
    private long maxPacketSize;
    private boolean resolveHostname;
    private double timestampUnit;
    private RecieveMode recieveMode;
    private double sessionBandwidth;
    private double controlFraction;
    private double senderFraction;
    private RTPTime minInterval;
    private boolean startUpTimeInterval;
    private boolean immediateBye;
    private boolean senderReport_bye;
    private double senderMultiplier;
    private double timeoutMulitplier;
    private double byeTimeout;
    private double collisionMultiplier;
    private double noteMultiplier;

    private boolean predefinedSSRCexists;
    int predefinedSSRC;
    String cName;


    public RTPSessionParams()
    {
        maxPacketSize = RTPConstants.RTP_DEFAULTPACKETSIZE;
        recieveMode = RTPTransmitter.acceptAll();

        acceptown = false;
        timestampUnit = -1;
        resolveHostname = false;


        minInterval = RTPTime(RTPConstants.RTCP_DEFAULTMININTERVAL);
        sessionBandwidth = RTPConstants.RTP_DEFAULTSESSIONBANDWIDTH;
        controlFraction = RTPConstants.RTCP_DEFAULTBANDWIDTHFRACTION;
        senderFraction = RTPConstants.RTCP_DEFAULTSENDERFRACTION;
        startUpTimeInterval = RTPConstants.RTCP_DEFAULTHALFATSTARTUP;
        immediateBye = RTPConstants.RTCP_DEFAULTIMMEDIATEBYE;
        senderReport_bye = RTPConstants.RTCP_DEFAULTSRBYE;

        senderMultiplier = RTPConstants.RTP_SENDERTIMEOUTMULTIPLIER;
        timeoutMulitplier = RTPConstants.RTP_MEMBERTIMEOUTMULTIPLIER;
        byeTimeout = RTPConstants.RTP_BYETIMEOUTMULTIPLIER;
        collisionMultiplier = RTPConstants.RTP_COLLISIONTIMEOUTMULTIPLIER;
        noteMultiplier = RTPConstants.RTP_NOTETTIMEOUTMULTIPLIER;
    }

    public void setAcceptPackets(boolean acceptown)
    {
        this.acceptown = acceptown;
    }

    public void setMaxPacketSize(long maxPacketSize)
    {
        this.maxPacketSize = maxPacketSize;
    }

    public void setResolveHostname(boolean resolveHostname)
    {
        this.resolveHostname = resolveHostname;
    }

    public void setTimestampUnit(double timestampUnit)
    {
        this.timestampUnit = timestampUnit;
    }

    public void setRecieveMode(RecieveMode recieveMode)
    {
        this.recieveMode = recieveMode;
    }

    public void setSessionBandwidth(double sessionBandwidth)
    {
        this.sessionBandwidth = sessionBandwidth;
    }

    public void setControlFraction(double controlFraction)
    {
        this.controlFraction = controlFraction;
    }

    public void setSenderFraction(double senderFraction)
    {
        this.senderFraction = senderFraction;
    }

    public void setMinRTCPTransInterval(RTPTime minInterval)
    {
        this.minInterval = minInterval;
    }

    public void setStartUpHalfInterval(boolean startUpTimeInterval)
    {
        this.startUpTimeInterval = startUpTimeInterval;
    }

    public void setImmediateByeRequest(boolean immediateBye)
    {
        this.immediateBye = immediateBye;
    }

    public void setSenderReport_bye(boolean senderReport_bye)
    {
        this.senderReport_bye = senderReport_bye;
    }

    public void setSenderMultiplier(double senderMultiplier)
    {
        this.senderMultiplier = senderMultiplier;
    }

    public void setTimeoutMulitplier(double timeoutMulitplier)
    {
        this.timeoutMulitplier = timeoutMulitplier;
    }

    public void setByeTimeout(double byeTimeout) {
        this.byeTimeout = byeTimeout;
    }

    public void setCollisionMultiplier(double collisionMultiplier)
    {
        this.collisionMultiplier = collisionMultiplier;
    }

    public void setNoteMultiplier(double noteMultiplier)
    {
        this.noteMultiplier = noteMultiplier;
    }

    public void setPredefinedSSRCexists(boolean predefinedSSRCexists)
    {
        this.predefinedSSRCexists = predefinedSSRCexists;
    }

    public void setPredefinedSSRC(int predefinedSSRC)
    {
        this.predefinedSSRC = predefinedSSRC;
    }

    public void setcName(String cName)
    {
        this.cName = cName;
    }

    public boolean isAcceptPackets()
    {
        return acceptown;
    }

    public long getMaxPacketSize()
    {
        return maxPacketSize;
    }

    public boolean isResolveHostname()
    {
        return resolveHostname;
    }

    public double getTimestampUnit()
    {
        return timestampUnit;
    }

    public RecieveMode getRecieveMode()
    {
        return recieveMode;
    }

    public double getSessionBandwidth()
    {
        return sessionBandwidth;
    }

    public double getControlFraction()
    {
        return controlFraction;
    }

    public double getSenderFraction()
    {
        return senderFraction;
    }

    public RTPTime getMinRTCPTransInterval()
    {
        return minInterval;
    }

    public boolean isStartUpHalfInterval()
    {
        return startUpTimeInterval;
    }

    public boolean isImmediateByeRequest()
    {
        return immediateBye;
    }

    public boolean isSenderReport_bye()
    {
        return senderReport_bye;
    }

    public double getSenderMultiplier()
    {
        return senderMultiplier;
    }

    public double getTimeoutMulitplier()
    {
        return timeoutMulitplier;
    }

    public double getByeTimeout()
    {
        return byeTimeout;
    }

    public double getCollisionMultiplier()
    {
        return collisionMultiplier;
    }

    public double getNoteMultiplier()
    {
        return noteMultiplier;
    }

    public boolean isPredefinedSSRCexists()
    {
        return predefinedSSRCexists;
    }

    public int getPredefinedSSRC()
    {
        return predefinedSSRC;
    }

    public String getcName()
    {
        return cName;
    }
}
