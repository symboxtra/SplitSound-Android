package splitsound.com.net;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.google.android.gms.common.util.ArrayUtils;

import java.math.BigInteger;
import java.net.DatagramSocket;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import jlibrtp.Participant;
import jlibrtp.RTPSession;
import splitsound.com.audio.opus.OpusAudioThread;
import splitsound.com.splitsound.SplitSoundApplication;

/**
 * RTP Main Thread
 *
 * @version 0.0.1
 * @author Neel
 */
public class RTPNetworking implements Runnable {

    private static final String TAG = "RTPNetworking";

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

        Log.i(TAG, "Main networking thread initiated");

        deviceIP = getIPAddress(true);
        Log.i("Device Address", deviceIP);

        // Create datagram ports for RTP and RTCP communication
        DatagramSocket rtpSocket = null;
        DatagramSocket rtcpSocket = null;
        try {
            rtpSocket = new DatagramSocket(RTPPort);
            rtcpSocket = new DatagramSocket(RTCPPort);
        } catch (Exception e) {
            Log.e(TAG, "Datagram Socket: RTPSession failed to obtain port");
            Log.e(TAG, e.toString());
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

            Log.i(TAG, "All sub-threads initiated");

            new Thread(new OpusAudioThread()).start();

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * Get the IP Address of the device
     *
     * @param useIPv4 true if IPv4 or false if IPv6 format is to be returned
     * @return device IP address in the specified format
     */
    public static String getIPAddress(boolean useIPv4)
    {
        WifiManager wm = (WifiManager) SplitSoundApplication.getAppContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiinfo = wm.getConnectionInfo();
        byte[] myIPAddress = BigInteger.valueOf(wifiinfo.getIpAddress()).toByteArray();
        for(int i = 0; i < myIPAddress.length / 2; i++)
        {
            byte temp = myIPAddress[i];
            myIPAddress[i] = myIPAddress[myIPAddress.length - i - 1];
            myIPAddress[myIPAddress.length - i - 1] = temp;
        }
        InetAddress myInetIP = null;
        try {
            myInetIP = InetAddress.getByAddress(myIPAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return myInetIP.getHostAddress();
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
