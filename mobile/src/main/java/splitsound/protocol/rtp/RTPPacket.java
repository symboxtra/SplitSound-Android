package splitsound.protocol.rtp;

/**
 * Created by Neel on 2/25/2018.
 */

public class RTPPacket extends RTPRawPacket
{
    private int error;

    private boolean hasextension,hasmarker;
    private int numcsrcs;

    private byte payloadtype;
    private int extseqnr,timestamp,ssrc;
    private byte packet[],payload[];
    private long packetlength,payloadlength;

    private short extid;
    private byte extension[];
    private long extensionlength;

    private boolean externalbuffer;

    private RTPTime receivetime;

    public void clear()
    {
        hasextension = false;
        hasmarker = false;
        numcsrcs = 0;
        payloadtype = 0;
        extseqnr = 0;
        timestamp = 0;
        ssrc = 0;
        packet = 0;
        payload = 0;
        packetlength = 0;
        payloadlength = 0;
        extid = 0;
        extension = 0;
        extensionlength = 0;
        error = 0;
        externalbuffer = false;
    }

    public RTPPacket(RTPRawPacket rawpack,RTPMemoryManager mgr )
    {
        clear();
        error = parseRawPacket(rawpack);
    }

    public RTPPacket(byte payloadtype,const void payloaddata[],long payloadlen,short seqnr, int timestamp,int ssrc,boolean gotmarker,byte numcsrcs,const int *csrcs, boolean gotextension,short extensionid,short extensionlen_numwords,const void *extensiondata, long maxpacksize, RTPMemoryManager mgr)
    {
        clear();
        error = BuildPacket(payloadtype,payloaddata,payloadlen,seqnr,timestamp,ssrc,gotmarker,numcsrcs,csrcs,gotextension,extensionid,extensionlen_numwords,extensiondata,0,maxpacksize);
    }

    int parseRawPacket(RTPRawPacket raw)
    {
        byte[] packetbytes;
        long packetlen;
        byte payloadtype;
        RTPStructs.RTPHeader rtpHeader;
        boolean marker;
        int csrccount;
        boolean hasextension;
        int payloadoffset, payloadlength;
        int numpadbytes;
        RTPStructs.RTPExtensionHeader rtpExtHeader;

        if(!raw.isRTP())
            return ERR_RTP_PACKET_INVALIDPACKET;

        packetlen = raw.getDataLength();
        if(packetlen < RTPStructs.RTPHeader.sizeOf())
            return ERR_RTP_PACKET_INVALIDPACKET;

        packetbytes = raw.getData();
        rtpHeader = new RTPStructs.RTPHeader(packetbytes);

        if(rtpHeader.ver != RTP_VERSION)
            return ERR_RTP_PACKET_INVALIDPACKET;

        marker = rtpHeader.marker;
    }
}
