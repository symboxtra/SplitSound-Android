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

    public static Buffer<Participant> servers = new Buffer<Participant>();
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
        RTCPReceiverTask receiveTask = new RTCPReceiverTask();
        RTPSessionTask sessionTask = new RTPSessionTask();

        RTPSession sess = new RTPSession(rtpSocket, rtcpSocket);
        sess.naivePktReception(true);
        sess.RTPSessionRegister(sessionTask,receiveTask, null);

        // Add broadcast IP as participant for initial log
        Participant broad = new Participant(broadcastAddress, RTPPort+2, RTCPPort+2);
        sess.addParticipant(broad);


        // Start individual receive threads
        new Thread(sessionTask).start();
        new Thread(receiveTask).start();

        // Start RTCP sender thread
        new Thread(new RTCPSessionTask(sess)).start();
    }
}
