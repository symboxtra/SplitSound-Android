package splitsound.protocol.rtcp;

/**
 * Created by Neel on 2/25/2018.
 */

public class RTCPStructs
{
    static class RTCPCommonHeader
    {
        public byte verson;
        public byte padding;
        public byte count;

        public byte packettype;
        short length;

        public RTCPCommonHeader()
        {}
    }

    static class RTCPSenderReport
    {
        int ntptime_msw;
        int ntptime_lsw;
        int rtptimestamp;
        int packetcount;
        int octetcount;

        public RTCPSenderReport()
        {}
    }

    static class RTCPReceiverReport
    {
        int ssrc;
        byte fractionlost;
        byte packetslost[];
        byte exthighseqnr;
        int jitter;
        int lsr;
        int dlsr;

        public RTCPReceiverReport()
        {}
    }

    static class RTCPSDESHeader
    {
        byte sdesid;
        byte length;

        public RTCPSDESHeader()
        {}
    }
}
