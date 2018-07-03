package splitsound.com.net;

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
import android.util.Log;
import android.widget.Toast;

import com.score.rahasak.utils.OpusDecoder;
import com.score.rahasak.utils.OpusEncoder;

import java.util.Arrays;

import splitsound.com.audio.controls.AudioTrackService;
import splitsound.com.splitsound.SplitSoundApplication;

/**
 * Main Opus audio thread that handles
 * audio playback and playback UI/UX interface
 *
 * @version 0.0.1
 * @author Neel
 */
public class OpusAudioThread implements Runnable
{

    public static final String Broadcast_PLAY_NEW_AUDIO = "splitsound.com.audio.PlayNewAudio";
    //public static final String Broadcast_PLAY_NEW_AUDIO = "androidpodcast.com.audioapp.PlayNewAudio";

    private AudioTrackService track;
    boolean serviceBound = false;

    // Sample rate must be one supported by Opus.
    static final int SAMPLE_RATE = 44100;

    // Number of samples per frame is not arbitrary,
    // it must match one of the predefined values, specified in the standard.
    static final int FRAME_SIZE = 160;

    // 1 or 2
    static final int NUM_CHANNELS = 1;
    private static final String TAG = "OpusAudioThread";

    @Override
    public void run()
    {
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

        /*
        //TODO: Set thread priority

        // Testing: Initialize Recorder
        int minBufSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, NUM_CHANNELS == 1 ? AudioFormat.CHANNEL_IN_MONO : AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);

        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC,
                SAMPLE_RATE,
                NUM_CHANNELS == 1 ? AudioFormat.CHANNEL_OUT_MONO : AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                minBufSize,
                AudioTrack.MODE_STREAM);

        track.play();

        byte[] inBuf;
        short[] outBuf = new short[FRAME_SIZE * NUM_CHANNELS];

        int packetsPlayed = 0;

        try {
            while (!Thread.interrupted()) {
                if(!RTPNetworking.networkPackets.isEmpty())
                {
                    // Get encoded data from transmission
                    inBuf = RTPNetworking.networkPackets.getNext().first;

                    track.write(inBuf, 0, inBuf.length * NUM_CHANNELS);
                    Log.d("Data packet", ++packetsPlayed + " packets played. Sound data received and to be decoded and played");
                }
            }
        }
        finally {
            track.stop();
            track.release();
        }
        */


    }

    private ServiceConnection serviceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            AudioTrackService.LocalBinder binder = (AudioTrackService.LocalBinder)service;
            track = binder.getService();
            serviceBound = true;
            Toast.makeText(SplitSoundApplication.getAppContext(), "Service Bound",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            serviceBound = false;
        }
    };

    /********************************** Initiate Service and connect Client *********************************/
}
