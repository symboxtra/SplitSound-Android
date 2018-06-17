package splitsound.com.net;

import android.util.Log;

import java.net.DatagramSocket;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import jlibrtp.Participant;
import jlibrtp.RTPSession;

/**
 * RTP Main Thread
 *
 * @version 0.0.1
 * @author Neel
 */
public class RTPNetworking implements Runnable {
    private final int RTPPort = 8000;
    private final int RTCPPort = 6000;

    private String broadcastAddress;
    public static String deviceIP;

    public static Buffer<byte[]> networkPackets = new Buffer<byte[]>();
    public static Buffer<AppPacket> requestQ = new Buffer<AppPacket>();

    private static boolean isServer = false;

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Lock readLock = lock.readLock();
    private static final Lock writeLock = lock.writeLock();

    /**
     * Constructor to start the main networking thread
     *
     * @param broadcastAddress - Calculated broadcast address of the device
     */
    public RTPNetworking(String broadcastAddress) {
        this.broadcastAddress = broadcastAddress;
    }

    @Override
    public void run() {
        setup();
    }

    /**
     * Starts the networking thread by instantiating RTP connection and
     * setting up necessary RTP/RTCP sessions or threads
     */
    public void setup() {
        deviceIP = getIPAddress(true);
        Log.i("Device Address", deviceIP);

        // Create datagram ports for RTP and RTCP communication
        DatagramSocket rtpSocket = null;
        DatagramSocket rtcpSocket = null;
        try {
            rtpSocket = new DatagramSocket(RTPPort);
            rtcpSocket = new DatagramSocket(RTCPPort);
        } catch (Exception e) {
            Log.e("Datagram Socket", "RTPSession failed to obtain port");
            Log.e("Error: ", e.toString());
        }

        // Create the RTP session and setup RTP and RTCP channels
        try {
            RTPSession sess = new RTPSession(rtpSocket, rtcpSocket);
            sess.naivePktReception(true);

            RTCPReceiverTask receiveTask = new RTCPReceiverTask(sess);
            RTPSessionTask sessionTask = new RTPSessionTask(sess);
            sess.RTPSessionRegister(sessionTask, receiveTask, null);

            // Add broadcast IP as participant for initial log
            Participant broad = new Participant(broadcastAddress, RTPPort + 2, RTCPPort + 2);
            sess.addParticipant(broad);

            // Start individual receive threads
            new Thread(sessionTask).start();
            new Thread(receiveTask).start();

            // Start RTCP sender thread
            new Thread(new RTCPSessionTask(sess)).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the IP Address of the device
     *
     * @param useIPv4 true if IPv4 or false if IPv6 format is to be returned
     * @return device IP address in the specified format
     */
    public static String getIPAddress(boolean useIPv4) {

        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
    }

    /**
     * Sets this device as a server
     *
     * @param serv boolean if device is server or not
     */
    public static void isServer(boolean serv)
    {
        writeLock.lock();
        try {
            isServer = serv;
        }finally {
            writeLock.unlock();
        }
    }

    /**
     * Gets the status if this device is server
     * @return server status of this device
     */
    public static boolean isServer()
    {
        readLock.lock();
        boolean temp;
        try{
            temp = isServer;
        }finally {
            readLock.unlock();
        }
        return temp;
    }
}
