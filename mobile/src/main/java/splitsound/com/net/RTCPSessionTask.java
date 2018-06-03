package splitsound.com.net;

import android.provider.Telephony;
import android.util.Log;
import android.util.Pair;

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

                Pair<AppPacket, Integer> appPair = RTPNetworking.requestQ.getNext();
                AppPacket app = appPair.first;
                switch (app)
                {
                    case LIST_ALL:
                        appType = 0;
                        data = "PROVIDE_SERVER_INFO " + RTPNetworking.deviceIP + " " + rtpSess.getSsrc() + " " + rtpSess.CNAME();
                        break;
                    case INFO:
                        appType = 1;
                        data = "SERVER_INFO " + RTPNetworking.deviceIP + " " + rtpSess.getSsrc() + " " + rtpSess.CNAME() + " " + rtpSess.name + " LOCKED " + "# of clients"; //TODO: Determine locked/unclocked based on server settings and total number of clients
                        break;
                    case LOGIN:
                        appType = 2;
                        data = "LOGIN_INFO " + RTPNetworking.deviceIP + " " + rtpSess.getSsrc() + " " + rtpSess.CNAME() + " " + "PASSWORD"; //TODO: Add hashed password entered by user
                        break;
                    case ACCEPT:
                        appType = 3;
                        data = "ACCEPT_USER" + RTPNetworking.deviceIP + " " + rtpSess.getSsrc() + " " + rtpSess.CNAME() + "1/0"; //TODO: Accept or deny based on return number and add server to participant list
                        break;
                }
                while(data.length() % 4 != 0)
                    data += " ";

                if(appPair.second == 0)
                {
                    for (Iterator<Participant> e = rtpSess.getUnicastReceivers(); e.hasNext(); ) {
                        rtpSess.sendRTCPAppPacket(e.next().getSSRC(), appType, "SYSS".getBytes(), data.getBytes());
                        Log.d("Sent", "RTCP packet sent!");
                    }
                }
                else
                    rtpSess.sendRTCPAppPacket(appPair.second, appType, "SYSS".getBytes(), data.getBytes());
            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
