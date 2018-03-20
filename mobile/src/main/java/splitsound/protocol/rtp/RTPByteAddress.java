package splitsound.protocol.rtp;

import splitsound.protocol.RTPConstants;

/**
 * Created by Neel on 3/20/2018.
 */

public class RTPByteAddress extends RTPAddress
{
    private final int RTPBYTEADDRESS_MAXLENGTH = 128;

    private byte hostAddress[] = new byte[RTPBYTEADDRESS_MAXLENGTH];
    private long addressLength;
    private int port;

    public RTPByteAddress(byte host[], long addrLen, int port)
    {
        super(AddressType.ByteAddress);
        if(addrLen > RTPBYTEADDRESS_MAXLENGTH)
            addrLen = RTPBYTEADDRESS_MAXLENGTH;
        this.hostAddress = host;
        this.addressLength = addrLen;
        this.port = port;

    }

    public void setHostAddress(byte host[], long addrLen)
    {
        if(addrLen > RTPBYTEADDRESS_MAXLENGTH)
            addrLen = RTPBYTEADDRESS_MAXLENGTH;
        this.hostAddress = host;
        this.addressLength = addrLen;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public byte[] getHostAddress()
    {
        return hostAddress;
    }

    public long getHostAddressLength()
    {
        return addressLength;
    }

    public long getPort()
    {
        return port;
    }

    @Override
    public RTPAddress createCopy()
    {
        RTPByteAddress a = new RTPByteAddress(hostAddress, addressLength, port);
        return a;
    }

    @Override
    public boolean isSameAddress(RTPAddress addr)
    {
        if(addr == null)
            return false;
        if(addr.getAddressType() != AddressType.ByteAddress)
            return false;

        RTPByteAddress b = (RTPByteAddress)addr;
        if(b.addressLength != this.addressLength)
            return false;

        boolean flag = true;
        for(int i = 0;i < this.getHostAddressLength();i++)
        {
            if(this.hostAddress[i] != b.getHostAddress()[i])
            {
                flag = false;
                break;
            }
        }

        if(addressLength == 0 || (addressLength == b.getHostAddressLength() && flag))
        {
            if(this.port == b.port)
                return true;
        }

        return false;
    }

    @Override
    public boolean isFromSameHost(RTPAddress addr)
    {
        if(addr == null)
            return false;
        if(addr.getAddressType() != AddressType.ByteAddress)
            return false;

        RTPByteAddress b = (RTPByteAddress)addr;
        if(addressLength != b.addressLength)
            return false;

        boolean flag = true;
        for(int i = 0;i < this.getHostAddressLength();i++)
        {
            if(this.hostAddress[i] != b.getHostAddress()[i])
            {
                flag = false;
                break;
            }
        }

        if(addressLength == 0 || (addressLength == b.getHostAddressLength() && flag))
                return true;

        return false;
    }

    @Override
    public String getAddressString()
    {
        String s = "";
        String str;

        for(int i = 0;i < addressLength;i++)
        {
            str = String.format("%02X", (int)hostAddress[i]);
            s += str;
        }
        s += ":";

        str = String.format("%02X", (int)port);
        s += str;

        return s;
    }
}
