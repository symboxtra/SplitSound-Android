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
            return 12;
        }
    }

    static class RTPExtensionHeader
    {
        public short extid;
        public short length;

        private static byte[] bits = new byte[5];

        public RTPExtensionHeader(byte[] packet, int offset)
        {
            extid = (short)((packet[offset] << 8) | packet[offset + 1]);
            length = (short)((packet[offset + 2] << 8) | packet[offset + 3]);

            bits[0] = packet[offset];
            bits[1] = packet[offset];
            bits[2] = packet[offset];
            bits[3] = packet[offset];

        }

        static int sizeOf()
        {
            return 4;
        }

        static byte[] getBytes()
        {
            return bits;
        }
    }

    static class RTPSourceIdentifier
    {
        public short ssrc;

        public RTPSourceIdentifier()
        {}
    }
}
