package splitsound.com.net;

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
                AppPacket app = RTPNetworking.requestQ.getNext();
                switch (app)
                {
                    case LIST_ALL:
                        break;
                    case INFO:
                        break;
                    case LOGIN:
                        break;
                    case RR:
                        break;
                    case SR:
                        break;
                    case BYE:
                        break;
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
