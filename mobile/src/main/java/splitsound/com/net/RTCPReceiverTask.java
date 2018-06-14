package splitsound.com.net;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Iterator;

import jlibrtp.Participant;
import jlibrtp.RTCPAppIntf;
import jlibrtp.RTPSession;
import splitsound.com.splitsound.R;
import splitsound.com.splitsound.SplitSoundApplication;
import splitsound.com.ui.adapters.RecyclerAdapter;
import splitsound.com.ui.adapters.ServerInfo;

/**
 * Handles callbacks for RTCP packets
 *
 * @version 0.0.1
 * @author Neel
 */
public class RTCPReceiverTask implements RTCPAppIntf, Runnable
{
    // Instance of RTP session created in the main thread
    private RTPSession rtpSess;

    /**
     * Constructor to start the receiver callback
     *
     * @param sess Current RTP session
     */
    public RTCPReceiverTask(RTPSession sess)
    {
        rtpSess = sess;
    }

    @Override
    public void run() {}

    @Override
    public void SRPktReceived(long ssrc, long ntpHighOrder, long ntpLowOrder, long rtpTimestamp, long packetCount, long octetCount, long[] reporteeSsrc, int[] lossFraction, int[] cumulPacketsLost, long[] extHighSeq, long[] interArrivalJitter, long[] lastSRTimeStamp, long[] delayLastSR)
    {
    }

    @Override
    public void RRPktReceived(long reporterSsrc, long[] reporteeSsrc, int[] lossFraction, int[] cumulPacketsLost, long[] extHighSeq, long[] interArrivalJitter, long[] lastSRTimeStamp, long[] delayLastSR)
    {
    }

    @Override
    public void SDESPktReceived(Participant[] relevantParticipants)
    {
    }

    @Override
    public void BYEPktReceived(Participant[] relevantParticipants, String reason)
    {
    }

    @Override
    public void APPPktReceived(Participant part, int subtype, byte[] name, byte[] data)
    {
        AppPacket app;

        if(name.equals("SYSS".getBytes()))
        {
            // Segment incoming data into expected variables
            String[] dataString = new String(data).split(" ");
            String command = dataString[0];
            String senderIP = dataString[1];
            String senderSSRC = dataString[2];
            String deviceName = dataString[3];

            // Perform tasks based on incoming commands/requests
            switch (command)
            {
                case "PROVIDE_SERVER_INFO": //Request for information if device is server
                    if(RTPNetworking.isServer())
                        RTPNetworking.requestQ.add(AppPacket.INFO, Integer.parseInt(senderSSRC));
                    break;
                case "SERVER_INFO": // Server information provided ; Add server to serverlist
                    if(dataString[4].equals(null))
                        RecyclerAdapter.addServer(new ServerInfo(deviceName, senderIP, Integer.parseInt(dataString[6]), dataString[5].contains("UN")));
                    else
                        RecyclerAdapter.addServer(new ServerInfo(dataString[4], senderIP, Integer.parseInt(dataString[6]), dataString[5].contains("UN")));
                    break;
                case "LOGIN_INFO": // Request to enter the server if device is server
                    break;
                case "ACCEPT_USER": // Add user to participant list if user is accepted else send failed message
                    boolean exists = false;
                    for(Iterator<Participant> e = rtpSess.getUnicastReceivers(); e.hasNext();)
                        if(e.next().getSSRC() == part.getSSRC())
                            exists = true;
                    if(!exists && Boolean.parseBoolean(dataString[4]))
                        rtpSess.addParticipant(part);

                    // Display error dialog if server denied service
                    if(!Boolean.parseBoolean(dataString[4]))
                    {
                        MaterialDialog builder = new MaterialDialog.Builder(SplitSoundApplication.getAppContext())
                                .iconRes(R.mipmap.access)
                                .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                                .title("Access Denied!")
                                .content("Invalid password")
                                .positiveText("OK")
                                .show();
                    }
                    break;
            }
        }
    }
}
