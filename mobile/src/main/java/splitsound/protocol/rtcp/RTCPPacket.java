package splitsound.protocol.rtcp;

/**
 * Created by J. McKernan on 2/25/2018.
 */

public class RTCPPacket
{
	protected byte[] data;
	protected long dataLen;
	protected boolean knownFormat;
	private final PacketType packetType;
	
	public enum PacketType
	{
		SR,			// RTCP sender report.
		RR,			// RTCP receiver report.
		SDES, 		// RTCP source description packet.
		BYE,		// RTCP bye packet.
		APP,		// RTCP packet containing application specific data.
		Unknown		// Unrecognized type
	}
	
	public RTCPacket(PacketType t, byte[] d, long dlen) // TODO: Chained constructor? data(d),datalen(dlen),packettype(t)
	{
		packetType = t;
		data = d;
		dataLen = dlen;
		knownFormat = false;
	}
	
	/**
	 * Returns true if subclass was able to interpret the data and false otherwise.
	 */
	boolean isKnownFormat()
	{
		return knownFormat;
	}
	
	/**
	 * Returns the actual packet type which the subclass implements.
	 */
	PacketType getPacketType()
	{
		return packetType;
	}
	
	/**
	 * Returns the data of this RTCP packet.
	 * Previously returned a pointer.
	 */
	byte getPacketData() // TODO: was pointer return type
	{
		return data;
	}
	
	/**
	 * Return the length of this RTCP packet.
	 */
	long getPacketLength()
	{
		return dataLen;
	}
	
	/**
	 * Dump useful imformation about the packet.
	 */
	 {
		 switch(packetType)
		 {
			case SR:
				System.out.print("RTCP Sender Report      ");
				break;
			case RR:
				System.out.print("RTCP Receiver Report    ");
				break;
			case SDES:
				System.out.print("RTCP Source Description ");
				break;
			case APP:
				System.out.print("RTCP APP Packet         ");
				break;
			case BYE:
				System.out.print("RTCP Byte Packet        ");
				break;
			case Unknown:
				System.out.print("Unknown RTCP Packet     ");
				break;
			default:
				System.out.println("ERROR: Invalid packet type!");
		 }
		 
		 System.out.println("Length: " + dataLen);
	 }
}