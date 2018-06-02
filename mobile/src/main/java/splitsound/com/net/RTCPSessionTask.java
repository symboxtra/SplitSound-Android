package splitsound.com.net;

import android.provider.Telephony;
import android.util.Log;

import java.util.Enumeration;
import java.util.Iterator;

import jlibrtp.Participant;
import jlibrtp.RTPSession;

/**
 * Created by Neel on 6/1/2018.
 */

public class RTCPSessionTask implements Runnable
{
    private RTPSession rtpSess;

    public RTCPSessionTask(RTPSession sess)
    {
        rtpSess = sess;
    }

    @Override
    public void run()
    {
        while(true)
        {
            if(!RTPNetworking.requestQ.isEmpty())
            {
                int appType = 0;
                String data = "";

                AppPacket app = RTPNetworking.requestQ.getNext();
                switch (app)
                {
                    case LIST_ALL:
                        appType = 0;
                        data = "PROVIDE_SERVER_INFO " + RTPNetworking.broadcastAddress + " " + rtpSess.CNAME();
                        while(data.length() % 4 != 0)
                            data += " ";
                        break;
                    case INFO:
                        appType = 1;
                        break;
                    case LOGIN:
                        appType = 2;
                        break;
                    case RR:
                        appType = 3;
                        break;
                    case SR:
                        appType = 4;
                        break;
                    case BYE:
                        appType = 5;
                        break;
                }
                for(Iterator<Participant> e = rtpSess.getUnicastReceivers(); e.hasNext();)
                {
                    rtpSess.sendRTCPAppPacket(e.next().getSSRC(), appType, "SYSS".getBytes(), data.getBytes());
                    Log.d("Sent", "RTCP packet sent!");
                }

            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
