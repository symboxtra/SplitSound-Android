package splitsound.session;

import splitsound.protocol.rtcp.RTPError;
import splitsound.utilities.RTPError;

/**
 * Created by Neel on 2/25/2018.
 */

public class RTPSession
{
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

    public RTPSession(RTPRandom r, RTPMemoryManager mgr) // chain constructor
    {
        changeIncomingData = false;
        changeOutgoingData = false;
        created = false;
        timeinit.dummy();
    }

    public int create(RTPSessionParams sessParams, RTPTransmissionParams transparams, TransmissionProtocol protocol)
    {
        int status;

        if(created)
            return RTPError.ERR_RTP_SESSION_ALREADYCREATED;

        user_bye_ifpossible = sessParams.getSenderReportBye();
        sentPackets = false;

        // Check max packet size
        if((maxPacketSize = sessParams.getMaximumPacketSize()) < RTPConstants.RTP_MINPACKETSIZE)
            return RTPError.ERR_RTP_SESSION_MAXPACKETSIZETOOSMALL;

        rtptrans = null;
        switch(protocol)
        {
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

    public int internalCreate(RTPSessionParams sessParams)
    {
        int status;

        if((status = packetbuilder.init(maxPacketSize)) < 0)
        {
            if(deleteTransmitter)
                RTPDelete(rtptrans, GetMemoryManager());
            return status;
        }
        if(sessParams.getUsePredefinedSSRC())
        {
            packetbuilder.adjustSSRC(sessparams.getPredefinedSSRC());
        }

        sources.setProbationType(sessParams.getProbationType());

        if ((status = sources.createOwnSSRC(packetbuilder.getSSRC())) < 0)
        {
            packetbuilder.destroy();
            if (deletetransmitter)
                RTPDelete(rtptrans,GetMemoryManager());
            return status;
        }

        if ((status = rtptrans.setReceiveMode(sessParams.getReceiveMode())) < 0)
        {
            packetbuilder.destroy();
            sources.clear();
            if (deletetransmitter)
                RTPDelete(rtptrans,GetMemoryManager());
            return status;
        }

        // Intialize the RTCP packet builder
        double timestampUnit = sessParams.getOwnTimestampUnit();

        byte[] buffer = new byte[1024];
        long buffLen = buffer.length;
        String cName = sessParams.getCName();

        if(cName.length() == 0)
        {
            if((status = createCName(buffer, buffLen, sessParams, getResolveLocalHostName())) < 0)
            {
                packetbuilder.destroy();
                sources.clear();
                if (deletetransmitter)
                    RTPDelete(rtptrans,GetMemoryManager());
                return status;
            }
        }
        else
        {
            RTP_copy(buffer, cName, buffLen);
            buffer[(int)buffLen - 1] = 0;
            buffLen = buffer.length;
        }

        if(status = rtcpbuilder.init(maxPacketSize, timestampUnit, buffer, buffLen) < 0)
        {
            packetbuilder.destroy();
            sources.clear();
            if (deletetransmitter)
                RTPDelete(rtptrans,GetMemoryManager());
            return status;
        }
        rtcpsched.reset();
        rtcpsched.setHeaderOverhead(rtptrans.getHeaderOverHead());

        RTCPSchedulerParams sParams;

        sessionBandwidth = sessParams.getSessionBandwidth();
        controlFragment = sessParams.getControlTrafficFraction();

        return 0;
    }
}
