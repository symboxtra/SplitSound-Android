package splitsound.protocol.rtp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
        packet = new byte[0];
        payload = new byte[0];
        packetlength = 0;
        payloadlength = 0;
        extid = 0;
        extension = new byte[0];
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

    public int parseRawPacket(RTPRawPacket raw)
    {
        byte[] packetbytes;
        long packetlen;
        byte payloadtype;
        RTPStructs.RTPHeader rtpHeader;
        boolean marker;
        final int csrccount;
        boolean hasextension;
        int payloadoffset, payloadlength;
        int numpadbytes;
        RTPStructs.RTPExtensionHeader rtpExtHeader;

        if(!raw.isRTP())
            return RTPError.ERR_RTP_PACKET_INVALIDPACKET;

        packetlen = raw.getDataLength();
        if(packetlen < RTPStructs.RTPHeader.sizeOf())
            return RTPError.ERR_RTP_PACKET_INVALIDPACKET;

        packetbytes = raw.getData();
        rtpHeader = new RTPStructs.RTPHeader(packetbytes);

        if(rtpHeader.ver != RTP_VERSION)
            return RTPError.ERR_RTP_PACKET_INVALIDPACKET;

        marker = rtpHeader.marker == 0;
        payloadtype = rtpHeader.payloadtype;

        if(marker)
        {
            if(payloadtype == (RTCP_RTCPTYPE_SR & 127))
                return RTPError.ERR_RTP_PACKET_INVALIDPACKET;
            if(payloadtype == (RTCP_RTCPTYPE_SR & 127))
                return RTPError.ERR_RTP_PACKET_INVALIDPACKET;
        }

        csrccount = rtpHeader.csrccount;
        payloadoffset = RTPStructs.RTPHeader.sizeOf()+(int)(csrccount*4);

        if(rtpHeader.padding != 0)
        {
            numpadbytes = (int)packetbytes[(int)packetlen - 1];
            if(numpadbytes <= 0)
                return RTPError.ERR_RTP_PACKET_INVALIDPACKET;
        }
        else
            numpadbytes = 0;

        hasextension = rtpHeader.extension != 0;
        if(hasextension)
        {
            rtpExtHeader = new RTPStructs.RTPExtensionHeader(packetbytes, payloadoffset);
            payloadoffset += RTPStructs.RTPExtensionHeader.sizeOf();

            short extLen = rtpExtHeader.length;
            payloadoffset += ((int)extLen)*4;
        }
        else
            rtpExtHeader = null;

        payloadlength = (int)(packetlen - numpadbytes - payloadoffset);
        if(payloadlength < 0)
            return RTPError.ERR_RTP_PACKET_INVALIDPACKET;


        if(hasextension)
        {
            extid = rtpExtHeader.extid;
            extensionlength = (int)rtpExtHeader.length*4;
            byte[] ext = rtpExtHeader.getBytes();
            extension = ext;
        }

        hasmarker = marker;
        numcsrcs = csrccount;
        this.payloadtype = payloadtype;


        extseqnr = (int)rtpHeader.sequencenumber;
        timestamp = rtpHeader.timestamp;
        packet = packetbytes;

        payload = new byte[packetbytes.length - payloadoffset];
        int ind = 0;
        for(int i = payloadoffset;i < packetbytes.length;i++, ind++)
        {
            payload[ind] = packetbytes[i];
        }
        packetlength = packetlen;
        this.payloadlength = payloadlength;

        return 0;
    }

    public int getCSRC(int num)
    {
        if(num >= numcsrcs)
            return 0;

        byte[] csrcpos = new byte[packet.length - RTPStructs.RTPHeader.sizeOf()-(num*4)];
        int[] csrc_val = new int[1];
        int csrc_num;
        int ind = 0;
        for(int i = RTPStructs.RTPHeader.sizeOf()+(num*4);i < packet.length;i++, ind++)
        {
            csrcpos[i] = packet[i];
        }
        csrc_num = (csrcpos[0] << 24) | (csrcpos[1] << 16) | (csrcpos[2]);
        return csrc_num;
    }

    public int buildPacket(byte payloadtype,const void *payloaddata,long payloadlen,short seqnr,int timestamp,int ssrc,boolean gotmarker,byte numcsrcs,const int[] csrcs,boolean gotextension,short extensionid,short extensionlen_numwords,const void *extensiondata,void *buffer,long maxsize)
    {
        if(numcsrcs > RTP_MAXCSRCS)
            return ERR_RTP_PACKET_TOOMANYCSRCS;

        if(payloadtype > 127)
            return ERR_RTP_PACKET_TOOMANYCSRCS;

        if(payloadtype == 72 || payloadtype == 73)
            return ERR_RTP_PACKET_BADPAYLOADTYPE;

        packetlength = RTPStructs.RTPHeader.sizeOf();
        packetlength += 4*(long)(numcsrcs);

        if(gotextension)
            packetlength += RTPStructs.RTPExtensionHeader.sizeOf() + 4*(long)(extensionlen_numwords);

        packetlength += payloadlen;

        if(maxsize > 0 && packetlength > maxsize)
        {
            packetlength = 0;
            return ERR_RTP_PACKET_DATAEXCEEDSMAXSIZE;
        }

        if(buffer == 0)
        {
            packet = RTPGetNew(GetMemoryManager(), RTPMEM_TYPE_BUFFER_RTPPACKET) uint8_t [packetlength]);
            if(packet.length == 0)
            {
                packetlength = 0;
                return ERR_RTP_OUTOFMEM;
            }
            externalbuffer = false;
        }
        else
        {
            packet = buffer;
            externalbuffer = true;
        }

        hasmarker = gotmarker;
        hasextension = gotextension;
        this.numcsrcs = numcsrcs;
        this.payloadtype = payloadtype;
        extseqnr = (int)seqnr;
        this.timestamp = timestamp;
        this.payloadlength = payloadlen;
        extid = extensionid;
        extensionlength = (long)(extensionlen_numwords)*4;

        RTPStructs.RTPHeader rtpHeader = new RTPStructs.RTPHeader(packet);
        rtpHeader.ver = RTP_VERSION;
        rtpHeader.padding = 0;
        if(gotmarker)
            rtpHeader.marker = 1;
        else
            rtpHeader.marker = 0;

        if(gotextension)
            rtpHeader.extension = 1;
        else
            rtpHeader.extension = 0;

        rtpHeader.csrccount = numcsrcs;
        rtpHeader.payloadtype = (byte)(this.payloadtype & 0x7F);

        //htons
        byte[] b = new byte[1];
        ByteBuffer buf = ByteBuffer.wrap(b);
        rtpHeader.sequencenumber = buf.putShort(seqnr).array()[0];
        rtpHeader.timestamp = buf.putLong(timestamp).array()[0];
        rtpHeader.ssrc = buf.putLong(ssrc).array()[0];

        for(int i = 0;i < numcsrcs*4;i++)
        {
            packet[RTPStructs.RTPHeader.sizeOf() + i] = (byte)((csrcs[i/4] << (8 * (3 - i%4))) & 0xFF);
        }
        payload = new byte[(int)(packet.length- RTPStructs.RTPHeader.sizeOf()-((long)numcsrcs)*4)];
        int ind = 0;
        for(int i = (int)(RTPStructs.RTPHeader.sizeOf()+((long)numcsrcs)*4);i < packet.length;i++, ind++)
        {
            payload[ind] = packet[i];
        }

        if(gotextension)
        {
            RTPStructs.RTPExtensionHeader rtpExtensionHeader = new RTPStructs.RTPExtensionHeader(payload, 0);
            rtpExtensionHeader.extid = extensionid;
            rtpExtensionHeader.length = (short)extensionlen_numwords;

            //REmoved extensions
        }

        return 0;
    }

    public int getCreationError()
    {
        return error;
    }

    public boolean hasExtension()
    {
        return hasextension;
    }

    public boolean hasMarker()
    {
        return hasmarker;
    }

    public int getCSRCCount()
    {
        return numcsrcs;
    }

    public int getPayloadType()
    {
        return payloadtype;
    }

    public int getExtendedSequenceNumber()
    {
        return extseqnr;
    }

    public void setSequenceNumber(int seq)
    {
        extseqnr = seq;
    }

    int getTimestamp()
    {
        return timestamp;
    }

    int getSSRC()
    {
        return ssrc;
    }

    byte[] getPackedData()
    {
        return packet;
    }

    byte[] getPayloadData()
    {
        return payload;
    }

    long getPacketLength()
    {
        return packetlength;
    }

    long getPayloadLength()
    {
        return payloadlength;
    }

    short getExtensionID()
    {
        return extid;
    }

    byte[] getExtensionData()
    {
        return extension;
    }

    long getExtensionLength()
    {
        return extensionlength;
    }

    public void Dump()
    {
        int i;

        System.out.printf("Payload type:                %d\n",(int)getPayloadType());
        System.out.printf("Extended sequence number:    0x%08x\n",getExtendedSequenceNumber());
        System.out.printf("Timestamp:                   0x%08x\n",getTimestamp());
        System.out.printf("SSRC:                        0x%08x\n",getSSRC());
        System.out.printf("Marker:                      %s\n",hasMarker()?"yes":"no");
        System.out.printf("CSRC count:                  %d\n",getCSRCCount());
        for (i = 0 ; i < getCSRCCount() ; i++)
            System.out.printf("    CSRC[%02d]:                0x%08x\n",i,getCSRC(i));
        System.out.printf("Payload:                     %s\n",getPayloadData());
        System.out.printf("Payload length:              %d\n",getPayloadLength());
        System.out.printf("Packet length:               %d\n",getPacketLength());
        System.out.printf("Extension:                   %s\n",hasExtension()?"yes":"no");
        if (hasExtension())
        {
            System.out.printf("    Extension ID:            0x%04x\n",getExtensionID());
            System.out.printf("    Extension data:          %s\n",getExtensionData());
            System.out.printf("    Extension length:        %d\n",getExtensionLength());
        }
    }
}
