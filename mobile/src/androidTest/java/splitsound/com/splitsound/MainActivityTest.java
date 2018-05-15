package splitsound.com.splitsound;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.common.api.internal.ApiExceptionMapper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.DatagramSocket;

import jlibrtp.Participant;
import jlibrtp.RTPSession;
import splitsound.com.net.RTCPReceiverTask;
import splitsound.com.net.RTPSessionTask;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("splitsound.com.splitsound", appContext.getPackageName());
    }
/*
    @Test
    public void getPackets() throws Exception
    {
        // Create datagram ports for RTP and RTCP communication
        DatagramSocket rtpSocket = null;
        DatagramSocket rtcpSocket = null;
        try {
            rtpSocket = new DatagramSocket(8001);
            rtcpSocket = new DatagramSocket(6001);
        }catch(Exception e)
        {
            Log.e("Datagram Socket", "RTPSession failed to obtain port");
            Log.e("Error: ", e.toString());
        }

        RTPSessionTask sessionTask = new RTPSessionTask();

        // Create the RTP session and setup RTP and RTCP channels
        RTPSession sess = new RTPSession(rtpSocket, rtcpSocket);
        sess.naivePktReception(true);
        sess.RTPSessionRegister(sessionTask,null, null);
        Participant p = new Participant("127.0.0.1", 8000, 6005);
        sess.addParticipant(p);

        for(int i = 0;i < 5;i++)
        {
            sess.sendData("Test Hi".getBytes());
        }
    }
   */
}
