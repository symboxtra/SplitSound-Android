package splitsound.com.splitsound;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.net.DatagramSocket;

import jlibrtp.Participant;
import jlibrtp.RTPSession;

/**
 * Created by Neel on 4/25/2018.
 */

class RTPSessionTask extends AsyncTask<Void, Integer, Boolean>
{
    @Override
    protected Boolean doInBackground(Void... params)
    {
        DatagramSocket rtpSocket = null;
        DatagramSocket rtcpSocket = null;

        try {
            rtpSocket = new DatagramSocket(6003);
            rtcpSocket = new DatagramSocket(6004);
        }catch(Exception e)
        {
            Log.e("Datagram Socket", "RTPSession failed to obtain port");
            Log.e("Error: ", e.toString());
        }

        Receive testReceive = new Receive(rtpSocket, rtcpSocket);

        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... progress)
    {
    }

    protected void onPostExecute(Boolean result)
    {
    }
}
