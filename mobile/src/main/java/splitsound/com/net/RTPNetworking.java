package splitsound.com.net;

import android.util.Log;

import java.net.DatagramSocket;

import jlibrtp.RTCPSession;
import jlibrtp.RTPSession;

/**
 * Created by Neel on 5/11/2018.
 */

public class RTPNetworking implements Runnable
{
    private final int RTPPort = 8000;
    private final int RTCPPort = 6000;

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

        RTCPReceiverTask receiveTask = new RTCPReceiverTask();
        RTPSessionTask sessionTask = new RTPSessionTask();

        // Create the RTP session and setup RTP and RTCP channels
        RTPSession sess = new RTPSession(rtpSocket, rtcpSocket);
        sess.naivePktReception(true);
        sess.RTPSessionRegister(sessionTask,receiveTask, null);

        // Start individual receive threads
        new Thread(sessionTask).start();
        new Thread(receiveTask).start();

        //RTCPSession rtcpSess = new RTCPSession(sess, rtcpSocket);
    }
}
