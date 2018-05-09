package splitsound.com.splitsound;


import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import java.net.DatagramSocket;

import jlibrtp.*;

/**
 * Created by Neel on 2/28/2018.
 */

public class Receive implements RTPAppIntf{

    RTPSession session = null;
    public Activity activity;
    String receiveText = "";
    int pktCount = 0;

    public Receive(DatagramSocket rtpSocket, DatagramSocket rtcpSocket)
    {
        session = new RTPSession(rtpSocket, rtcpSocket);
        session.naivePktReception(true);
        session.RTPSessionRegister(this, null, null);


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
