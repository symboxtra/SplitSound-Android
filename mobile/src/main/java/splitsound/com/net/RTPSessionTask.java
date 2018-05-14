package splitsound.com.net;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.net.DatagramSocket;
import java.net.MulticastSocket;

import jlibrtp.DataFrame;
import jlibrtp.Participant;
import jlibrtp.RTPAppIntf;
import jlibrtp.RTPSession;
import splitsound.com.splitsound.Receive;

/**
 * Created by Neel on 4/25/2018.
 */

class RTPSessionTask implements RTPAppIntf, Runnable
{
    public Activity activity;
    String receiveText = "";
    int pktCount = 0;

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
        byte[] data = frame.getConcatenatedData();
        pktCount++;
        Log.e("Test: ", "Packet Count: " + pktCount+"\n");
    }

    @Override
    public void userEvent(int type, Participant[] participants)
    {
    }
}
