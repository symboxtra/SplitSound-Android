package splitsound.protocol.rtp;

/**
 * Created by Neel on 3/20/2018.
 */

public abstract class RTPAddress
{
    public enum AddressType {IPv4Address, IPv6Address, ByteAddress, UserDefinedAddress, TCPAddress};

    private AddressType addressType;

    protected RTPAddress(AddressType t)
    {
        addressType = t;
    }

    public AddressType getAddressType()
    {
        return addressType;
    }

    public abstract RTPAddress createCopy();

    public abstract boolean isSameAddress(RTPAddress addr);

    public abstract boolean isFromSameHost(RTPAddress addr);

    public abstract String getAddressString();

}
