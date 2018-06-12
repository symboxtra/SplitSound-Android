package splitsound.com.net;

import android.content.Context;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.util.Log;
import android.util.Pair;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Iterator;

import jlibrtp.Participant;
import jlibrtp.RTPSession;
import splitsound.com.splitsound.SplitSoundApplication;

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
                        int sum = 0;
                        for(Iterator<Participant> e = rtpSess.getUnicastReceivers(); e.hasNext();e.next(), sum++);
                        data = "SERVER_INFO " + RTPNetworking.deviceIP + " " + rtpSess.getSsrc() + " " + rtpSess.CNAME() + " " + rtpSess.name + " UNLOCKED " + sum; //TODO: Determine locked/unclocked based on server settings
                        break;
                    case LOGIN:
                        appType = 2;
                        String pass = PreferenceManager.getDefaultSharedPreferences(SplitSoundApplication.getAppContext()).getString("password", "Default");
                        data = "LOGIN_INFO " + RTPNetworking.deviceIP + " " + rtpSess.getSsrc() + " " + rtpSess.CNAME() + " " + getEncryption(pass);
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
                    for (Iterator<Participant> e = rtpSess.getUnicastReceivers(); e.hasNext(); )
                    {
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


    // Password Encoding
    public static String getEncryption(String encode)
    {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] encodedHash =  digest.digest(encode.getBytes(StandardCharsets.UTF_8));

        return bytesToHex(encodedHash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
