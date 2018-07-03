package splitsound.com.net;

import android.content.Context;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.support.annotation.NonNull;
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
 * Handles sending RTCP packets to its receivers
 *
 * @version 0.0.1
 * @author Neel
 */
public class RTCPSessionTask implements Runnable
{
    private static final String TAG = "RTCPSessionTask";

    // Instance of RTP session created in the main thread
    private RTPSession rtpSess;

    /**
     * Constructor to start the RTCP sender thread
     *
     * @param sess Current RTP session
     */
    public RTCPSessionTask(RTPSession sess)
    {
        rtpSess = sess;
    }

    @Override
    public void run()
    {
        Log.i(TAG, "Thread initiated");

        // Infinite loop to keep sending RTCP packets until available
        // or session executed
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
                    case LIST_ALL: // Send packet to list all servers in the network
                        appType = 0;
                        data = "PROVIDE_SERVER_INFO " + RTPNetworking.deviceIP + " " + rtpSess.getSsrc() + " " + rtpSess.CNAME();
                        break;
                    case INFO: // Send information of device if device is server
                        appType = 1;
                        int sum = 0;
                        for(Iterator<Participant> e = rtpSess.getUnicastReceivers(); e.hasNext();e.next(), sum++);
                        data = "SERVER_INFO " + RTPNetworking.deviceIP + " " + rtpSess.getSsrc() + " " + rtpSess.CNAME() + " " + rtpSess.name + " UNLOCKED " + sum; //TODO: Determine locked/unclocked based on server settings
                        break;
                    case LOGIN: // Send login information to join destination server
                        appType = 2;
                        String pass = PreferenceManager.getDefaultSharedPreferences(SplitSoundApplication.getAppContext()).getString("password", "Default");
                        data = "LOGIN_INFO " + RTPNetworking.deviceIP + " " + rtpSess.getSsrc() + " " + rtpSess.CNAME() + " " + getEncryption(pass);
                        break;
                    case ACCEPT: // Send acceptance or denial information based on password if device is server
                        appType = 3;
                        data = "ACCEPT_USER " + RTPNetworking.deviceIP + " " + rtpSess.getSsrc() + " " + rtpSess.CNAME() + "1/0"; //TODO: Accept or deny based password and client number limit
                        break;
                    case KICK:
                        appType = 4;
                        data = "KICK_USER " + RTPNetworking.deviceIP + " " + rtpSess.getSsrc() + " " + rtpSess.CNAME() + " "; //TODO: Get kicked user ssrc
                        break;
                }

                // Pad data with empty strings to pass modularity of 4
                while(data.length() % 4 != 0)
                    data += " ";

                // Send concatenated data on broadcast IP or specific IP based on requestQ SSRC
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

            // Stall for sanity purposes
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Encodes the provided string into the designated format
     *
     * @param encode input string to be encoded
     * @return Encrypted string
     */
    @NonNull
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

    /**
     * Converts byte array to hexadecimal String
     * @param hash byte array to be converted
     * @return hexadecimal String format
     */
    @NonNull
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
