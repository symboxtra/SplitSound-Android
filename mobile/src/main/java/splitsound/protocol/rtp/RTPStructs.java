package splitsound.protocol.rtp;

/**
 * Created by Neel on 2/25/2018.
 */

public class RTPStructs
{
    static class RTPHeader
    {
        public byte ver;
        public byte padding;
        public byte extension;
        public byte csrccount;

        public byte marker;
        public byte payloadtype;

        short sequencenumber;
        int timestamp;
        int ssrc;

        public RTPHeader(byte[] packet)
        {
            ver = (byte)((packet[0] & 0xC0) >> 6);
            padding = (byte)((packet[0] & 0x20) >> 5);
            extension = (byte)((packet[0] & 0x10) >> 4);
            csrccount = (byte)(packet[0] & 0xF);

            marker = (byte)((packet[1] & 0x80) >> 7);
            payloadtype = (byte)(packet[1] & 0xEF);

            sequencenumber = (byte)((packet[2] << 8) | packet[3]);

            timestamp = (byte)((packet[4] << 24) | (packet[5] << 16) | (packet[6] << 8) | (packet[7]));
        }

        static int sizeOf()
        {
            return 96;
        }
    }

    static class RTPExtensionHeader
    {
        public short extid;
        public short length;

        public RTPExtensionHeader()
        {}
    }

    static class RTPSourceIdentifier
    {
        public short ssrc;

        public RTPSourceIdentifier()
        {}
    }
}
