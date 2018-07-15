package splitsound.com.audio.opus;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.score.rahasak.utils.OpusDecoder;
import com.score.rahasak.utils.OpusEncoder;

import java.util.Arrays;

import splitsound.com.audio.controls.AudioTrackService;
import splitsound.com.audio.controls.AudioTrackService;
import splitsound.com.net.RTPNetworking;
import splitsound.com.splitsound.SplitSoundApplication;

/**
 * Main Opus audio thread that handles
 * audio playback and UI interface
 *
 * @version 0.0.1
 * @author Neel
 */
public class OpusAudioThread implements Runnable
{
    private static final String TAG = "OpusAudioThread";

    public static final String Broadcast_PLAY_NEW_AUDIO = "splitsound.com.audio.PlayNewAudio";

    boolean serviceBound = false;

    @Override
    public void run()
    {
        //TODO: Set thread priority

        // Start the background service
        if(!serviceBound)
        {
            Intent playerIntent = new Intent(SplitSoundApplication.getAppContext(), AudioTrackService.class);
            SplitSoundApplication.getAppContext().startService(playerIntent);
            boolean boo = SplitSoundApplication.getAppContext().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            Log.d("Status", boo+"");
        }
        else
        {
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            SplitSoundApplication.getAppContext().sendBroadcast(broadcastIntent);
        }

        try {
            byte[] inBuf;
            while(!Thread.interrupted())
            {
                if(!RTPNetworking.networkPackets.isEmpty() && AudioTrackService.getTrack() != null)
                {
                    inBuf = RTPNetworking.networkPackets.getNext().first;

                    // Add audio data to play queue
                    AudioTrackService.getTrack().write(inBuf, 0, inBuf.length * AudioTrackService.NUM_CHANNELS);
                    Log.d(TAG, "Data packet added to audio queue");
                }
            }
        }finally {
            SplitSoundApplication.getAppContext().stopService(new Intent(SplitSoundApplication.getAppContext(), AudioTrackService.class));
        }
    }

    /**
     * Initiates a service connection between the application and the service
     *
     */
    private ServiceConnection serviceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            serviceBound = true;
            Toast.makeText(SplitSoundApplication.getAppContext(), "Connected",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            serviceBound = false;
        }
    };
}
