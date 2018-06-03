package splitsound.com.net;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.Iterator;

import jlibrtp.Participant;
import jlibrtp.RTCPAppIntf;
import jlibrtp.RTPSession;
import splitsound.com.ui.adapters.RecyclerAdapter;
import splitsound.com.ui.adapters.ServerInfo;

/**
 * Created by Neel on 5/11/2018.
 */

public class RTCPReceiverTask implements RTCPAppIntf, Runnable
{
    private RTPSession rtpSess;

    public RTCPReceiverTask(RTPSession sess)
    {
        rtpSess = sess;
    }

    @Override
    public void run()
    {

    }

    @Override
    public void SRPktReceived(long var1, long var3, long var5, long var7, long var9, long var11, long[] var13, int[] var14, int[] var15, long[] var16, long[] var17, long[] var18, long[] var19)
    {
    }

    @Override
    public void RRPktReceived(long var1, long[] var3, int[] var4, int[] var5, long[] var6, long[] var7, long[] var8, long[] var9)
    {
    }

    @Override
    public void SDESPktReceived(Participant[] var1)
    {
    }

    @Override
    public void BYEPktReceived(Participant[] var1, String var2)
    {
    }

    @Override
    public void APPPktReceived(Participant var1, int var2, byte[] var3, byte[] var4)
    {
        AppPacket app;

        if(var3.equals("SYSS".getBytes()))
        {
            String[] dataString = new String(var4).split(" ");
            String command = dataString[0];
            String senderIP = dataString[1];
            String senderSSRC = dataString[2];
            String deviceName = dataString[3];

            switch (command)
            {
                case "PROVIDE_SERVER_INFO":
                    RTPNetworking.requestQ.add(AppPacket.INFO, Integer.parseInt(senderSSRC));
                    break;
                case "SERVER_INFO":
                    if(dataString[4].equals(null))
                        RecyclerAdapter.addServer(new ServerInfo(deviceName, senderIP, Integer.parseInt(dataString[6]), dataString[5].contains("UN")));
                    else
                        RecyclerAdapter.addServer(new ServerInfo(dataString[4], senderIP, Integer.parseInt(dataString[6]), dataString[5].contains("UN")));
                    break;
                case "LOGIN_INFO":
                    break;
                case "ACCEPT_USER":
                    boolean exists = false;
                    for(Iterator<Participant> e = rtpSess.getUnicastReceivers(); e.hasNext();)
                        if(e.next().getSSRC() == var1.getSSRC())
                            exists = true;
                    if(!exists && Boolean.parseBoolean(dataString[4]))
                        rtpSess.addParticipant(var1);
                    break;
            }
        }
    }
}
