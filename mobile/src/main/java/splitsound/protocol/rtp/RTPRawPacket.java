package splitsound.protocol.rtp;

/**
 * Created by Neel on 2/25/2018.
 */

public class RTPRawPacket
{
    private byte[] packetData;
    private long packetDataLength;
    private RTPTime recieveTime;
    private RTPAddress senderAddress;
    private boolean isRTP;

    public RTPPacket(byte[] data,long datalen,RTPAddress address,RTPTime recvtime,boolean rtp,RTPMemoryManager mgr)
    {
        packetData = data;
        packetDataLength = datalen;
        senderAddress = address;
        isRTP = rtp;
    }

    public void setSenderAddress(RTPAddress address)
    {
        if (senderAddress != NULL)
            RTPDelete(senderAddress, GetMemoryManager());

        senderAddress = address;
    }

    public void setData(byte[] data, long datalen)
    {
        if (packetData != 0)
            RTPDeleteByteArray(packetdata,GetMemoryManager());

        packetData = data;
        packetDataLength = datalen;
    }

    public byte[] getData()
    {
        return packetData;
    }

    public long getDataLength()
    {
        return packetDataLength;
    }

    public RTPTime getRTPTime()
    {
        return recieveTime;
    }

    boolean isRTP()
    {
        return isRTP;
    }


}
