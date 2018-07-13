package splitsound.com.net;

import android.app.Activity;
import android.util.Log;

import java.util.Iterator;

import jlibrtp.DataFrame;
import jlibrtp.Participant;
import jlibrtp.RTPAppIntf;
import jlibrtp.RTPSession;

/**
 * Handles callbacks for RTP packets
 *
 * @version 0.0.1
 * @author Neel
 */
public class RTPSessionTask implements RTPAppIntf, Runnable
{

    private static final String TAG = "RTPSessionTask";

    // Instance of RTP session created in the main thread
    private RTPSession rtpSess;

    /**
     * Constructor to start the RTP receiver callback
     *
     * @param sess Current RTP session
     */
    public RTPSessionTask(RTPSession sess)
    {
        rtpSess = sess;
    }

    @Override
    public void run()
    {
        Log.i(TAG, "Thread initiated");
    }

    @Override
    public int frameSize(int payloadType)
    {
        return 1;
    }

    @Override
    public void receiveData(DataFrame frame, Participant p)
    {
        // Add data to data queue only if participant server is accepted
        boolean exists = false;
        for(Iterator<Participant> e = rtpSess.getUnicastReceivers(); e.hasNext();)
            if(e.next().getSSRC() == p.getSSRC())
                exists = true;

        byte[] data = frame.getConcatenatedData();
        if(exists)
            RTPNetworking.networkPackets.add(data);
        p.debugPrint();
    }

    @Override
    public void userEvent(int type, Participant[] participants)
    {
    }
}
