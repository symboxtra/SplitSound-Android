package splitsound.com.net;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

import com.score.rahasak.utils.OpusDecoder;
import com.score.rahasak.utils.OpusEncoder;

import java.util.Arrays;

/**
 * Created by Neel on 6/12/2018.
 */

public class OpusAudioThread implements Runnable
{
    // Sample rate must be one supported by Opus.
    static final int SAMPLE_RATE = 8000;

    // Number of samples per frame is not arbitrary,
    // it must match one of the predefined values, specified in the standard.
    static final int FRAME_SIZE = 160;

    // 1 or 2
    static final int NUM_CHANNELS = 1;
    private static final String TAG = "OpusAudioThread";

    @Override
    public void run()
    {
        //TODO: Set thread priority

        // Testing: Initialize Recorder
        int minBufSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, NUM_CHANNELS == 1 ? AudioFormat.CHANNEL_IN_MONO : AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                NUM_CHANNELS == 1 ? AudioFormat.CHANNEL_IN_MONO : AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                minBufSize);

        AudioTrack track = new AudioTrack(AudioManager.STREAM_SYSTEM,
                SAMPLE_RATE,
                NUM_CHANNELS == 1 ? AudioFormat.CHANNEL_OUT_MONO : AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                minBufSize,
                AudioTrack.MODE_STREAM);


        OpusEncoder encoder = new OpusEncoder();
        encoder.init(SAMPLE_RATE, NUM_CHANNELS, OpusEncoder.OPUS_APPLICATION_VOIP);

        OpusDecoder decoder = new OpusDecoder();
        decoder.init(SAMPLE_RATE, NUM_CHANNELS);

        recorder.startRecording();
        track.play();

        byte[] inBuf = new byte[FRAME_SIZE * NUM_CHANNELS * 2];
        byte[] encBuf = new byte[1024];
        short[] outBuf = new short[FRAME_SIZE * NUM_CHANNELS];

        try {
            while (!Thread.interrupted()) {
                // Encoder must be fed entire frames.
                int to_read = inBuf.length;
                int offset = 0;
                //while (to_read > 0) {
                    inBuf = RTPNetworking.networkPackets.getNext().first;
                    //int read = recorder.read(inBuf, offset, to_read);
                    //if (read < 0) {
                     //   throw new RuntimeException("recorder.read() returned error " + read);
                    //}
                    //to_read -= read;
                    //offset += read;
                //}

                //int encoded = encoder.encode(inBuf, FRAME_SIZE, encBuf);

                //Log.v(TAG, "Encoded " + inBuf.length + " bytes of audio into " + encoded + " bytes");

                //byte[] encBuf2 = Arrays.copyOf(encBuf, encoded);

                int decoded = decoder.decode(inBuf, outBuf, FRAME_SIZE);

                Log.v(TAG, "Decoded back " + decoded * NUM_CHANNELS * 2 + " bytes");

                track.write(outBuf, 0, decoded * NUM_CHANNELS);
            }
        } finally {
            recorder.stop();
            recorder.release();
            track.stop();
            track.release();
        }

    }
}
