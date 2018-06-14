package splitsound.com.net;

import android.app.Activity;

import java.util.Iterator;

import jlibrtp.DataFrame;
import jlibrtp.Participant;
import jlibrtp.RTPAppIntf;
import jlibrtp.RTPSession;

/**
 * Created by Neel on 4/25/2018.
 */

public class RTPSessionTask implements RTPAppIntf, Runnable
{
    private RTPSession rtpSess;

    public RTPSessionTask(RTPSession sess)
    {
        rtpSess = sess;
    }

    @Override
    public void run()
    {
    }

    @Override
    public int frameSize(int payloadType)
    {
        return 1;
    }

    @Override
    public void receiveData(DataFrame frame, Participant p)
    {
        boolean exists = false;
        for(Iterator<Participant> e = rtpSess.getUnicastReceivers(); e.hasNext();)
        {
            if(e.next().getSSRC() == p.getSSRC())
                exists = true;
        }

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
