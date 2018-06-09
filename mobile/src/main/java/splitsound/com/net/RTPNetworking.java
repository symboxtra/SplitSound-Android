package splitsound.com.net;

import android.util.Log;

import java.net.DatagramSocket;
import java.net.*;
import java.util.*;

import jlibrtp.Participant;
import jlibrtp.RTPSession;

/**
 * Created by Neel on 5/11/2018.
 */

public class RTPNetworking implements Runnable
{
    private final int RTPPort = 8000;
    private final int RTCPPort = 6000;

    private String broadcastAddress;
    public static String deviceIP;

    public static Buffer<byte[]> networkPackets = new Buffer<byte[]>();
    public static Buffer<AppPacket> requestQ = new Buffer<AppPacket>();

    public RTPNetworking(String broadcastAddress)
    {
        this.broadcastAddress = broadcastAddress;
    }

    @Override
    public void run()
    {
        setup();
    }

    public void setup()
    {
        deviceIP = getIPAddress(true);

        // Create datagram ports for RTP and RTCP communication
        DatagramSocket rtpSocket = null;
        DatagramSocket rtcpSocket = null;
        try {
            rtpSocket = new DatagramSocket(RTPPort);
            rtcpSocket = new DatagramSocket(RTCPPort);
        }catch(Exception e)
        {
            Log.e("Datagram Socket", "RTPSession failed to obtain port");
            Log.e("Error: ", e.toString());
        }

        // Create the RTP session and setup RTP and RTCP channels
        try{
            RTPSession sess = new RTPSession(rtpSocket, rtcpSocket);
        sess.naivePktReception(true);

        RTCPReceiverTask receiveTask = new RTCPReceiverTask(sess);
        RTPSessionTask sessionTask = new RTPSessionTask(sess);
        sess.RTPSessionRegister(sessionTask,receiveTask, null);

        // Add broadcast IP as participant for initial log
        Participant broad = new Participant(broadcastAddress, RTPPort+2, RTCPPort+2);
        sess.addParticipant(broad);

        // Start individual receive threads
        new Thread(sessionTask).start();
        new Thread(receiveTask).start();

        // Start RTCP sender thread
        new Thread(new RTCPSessionTask(sess)).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getIPAddress(boolean useIPv4)
    {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }
}
