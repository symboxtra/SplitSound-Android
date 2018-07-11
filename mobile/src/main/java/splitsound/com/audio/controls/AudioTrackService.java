package splitsound.com.audio.controls;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.content.ComponentName;
import android.os.Build;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import splitsound.com.audio.opus.OpusAudioThread;
import splitsound.com.splitsound.DrawerActivityTest;
import splitsound.com.splitsound.R;
import splitsound.com.splitsound.SplitSoundApplication;

/**
 * Created by Neel on 7/1/2018.
 */

public class AudioTrackService extends Service implements
        AudioManager.OnAudioFocusChangeListener
{
    private static final String TAG = "AudioTrackService";

    public class LocalBinder extends Binder
    {
        public AudioTrackService getService(){return AudioTrackService.this;}
    }

    // Binder given to clients
    private final IBinder iBinder = new LocalBinder();

    // Audio instances to manage audio data
    public static AudioTrack audioTrack;
    private AudioManager audioManager;

    // Help handle incoming calls
    private boolean ongoingCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;

    // Sample rate must be one supported by Opus.
    public static final int SAMPLE_RATE = 44100;

    // Number of samples per frame is not arbitrary,
    // it must match one of the predefined values, specified in the standard.
    public static final int FRAME_SIZE = 160;

    // 1 or 2
    public static final int NUM_CHANNELS = 1;

    // Pending Intent actions triggerd by the Media Session listener
    public static final String ACTION_PLAY = "splitsound.com.audio.ACTION_PLAY";
    public static final String ACTION_PAUSE = "splitsound.com.audio.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "splitsound.com.audio.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "splitsound.com.audio.ACTION_NEXT";
    public static final String ACTION_STOP = "splitsound.com.audio.ACTION_STOP";

    //Initialize media session
    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;

    //AudioPlayer notification ID
    private static final int NOTIFICATION_ID = 101;

    /********************************** Initialization *********************************/

    @Override
    public void onCreate()
    {
        super.onCreate();

        if(mediaSessionManager == null)
        {
            try {
                initMediaSession();
                initAudioTrack();
            } catch(RemoteException e)
            {
                e.printStackTrace();
                stopSelf();
            }
            buildNotification(PlaybackStatus.PLAYING);
        }

        initNoisyReceiver();
        callStateListener();
        initPlayNewAudio();

        Log.i(TAG, "Audio service initiated and audio focus acquired");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if(!requestAudioFocus()) {
            Log.e(TAG, "Could not obtain audio focus....exiting");
            stopSelf();
        }

        handleIncomingActions(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return iBinder;
    }

    /*Requests the audio focus from the Android service*/
    private boolean requestAudioFocus()
    {
        audioManager = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        if(audioManager == null)
            Log.e(TAG, "Audio manager unitialized");
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    /*Removes the audio focus and provides it back to the android service*/
    private boolean removeAudioFocus()
    {
        if(audioManager == null)
            Log.e(TAG, "Audio Manager error: audio manager is null");
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.abandonAudioFocus(this);
    }

    public void initAudioTrack()
    {
        // Get the most minimum buffer size based on channel and encoding
        int minBufSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, NUM_CHANNELS == 1 ? AudioFormat.CHANNEL_IN_MONO : AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);

        // Initialize audioTrack
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                SAMPLE_RATE,
                NUM_CHANNELS == 1 ? AudioFormat.CHANNEL_OUT_MONO : AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                minBufSize,
                AudioTrack.MODE_STREAM);

        audioTrack.play();
    }

    private void initMediaSession() throws RemoteException
    {
        if(mediaSession!= null)return;

        mediaSessionManager = (MediaSessionManager)getApplicationContext().getSystemService(Context.MEDIA_SESSION_SERVICE);
        mediaSession = new MediaSessionCompat(getApplicationContext(),"SplitSound Player");
        transportControls = mediaSession.getController().getTransportControls();
        mediaSession.setActive(true);

        mediaSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

        updateMetaData();
        //Attach Callback to recieve MediaSession Updates
        mediaSession.setCallback(new MediaSessionCompat.Callback()
        {
            @Override
            public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
                return super.onMediaButtonEvent(mediaButtonEvent);
            }

            @Override
            public void onPlay()
            {
                super.onPlay();
                resumeMedia();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onPause()
            {
                super.onPause();
                pauseMedia();
                buildNotification(PlaybackStatus.PAUSED);
            }

            @Override
            public void onSkipToNext()
            {
                super.onSkipToNext();
                updateMetaData();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onSkipToPrevious()
            {
                super.onSkipToPrevious();
                updateMetaData();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onStop()
            {
                super.onStop();
                stopMedia();
                removeNotification();
                stopSelf();
            }

            @Override
            public void onSeekTo(long position)
            {
                super.onSeekTo(position);
            }
        });
        Log.i(TAG, "Media session initialized and callbacks set");
    }

    private void initNoisyReceiver()
    {
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        if(intentFilter == null)
            Log.e(TAG, "becomingNoisyReceiver: intent init failed");
        registerReceiver(becomingNoisy, intentFilter);
    }

    private void initPlayNewAudio()
    {
        IntentFilter filter = new IntentFilter(OpusAudioThread.Broadcast_PLAY_NEW_AUDIO);
        registerReceiver(playNewAudio, filter);
    }

    /********************************** Session Updates *********************************/

    private void updateMetaData()
    {
        Bitmap albumArt = BitmapFactory.decodeResource(getResources(), R.drawable.image);

        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "Artisite")
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, "Where is this?")
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "Am I player?")
                .build());
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void buildNotification(PlaybackStatus playbackStatus)
    {
        int notificationAction = R.drawable.ic_pause_black_40dp;//needs to be initialized
        PendingIntent play_pauseAction = null;

        //Build a new notification according to the current state of the MediaPlayer
        if (playbackStatus == PlaybackStatus.PLAYING) {
            notificationAction = R.drawable.ic_pause_black_40dp;
            //create the pause action
            play_pauseAction = playbackAction(1);
        } else if (playbackStatus == PlaybackStatus.PAUSED) {
            notificationAction = R.drawable.ic_play_arrow_black_40dp;
            //create the play action
            play_pauseAction = playbackAction(0);
        }

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.image); //replace with your own image

        //Code so when you tap outside the buttons it goes back to the app
        Intent intent = new Intent(this, DrawerActivityTest.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Create a new Notification
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext(), "notify_001")
                .setShowWhen(false)
                .setOngoing(true)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(0, 1, 2))
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.ic_headset_mic_black_24dp)
                .setContentText("")
                .setContentTitle(getString(R.string.stream_text))
                .setContentInfo("Streaming audio...")
                .addAction(R.drawable.ic_skip_previous_black_40dp, "previous", playbackAction(3))
                .addAction(notificationAction, "pause", play_pauseAction)
                .addAction(R.drawable.ic_skip_next_black_40dp, "next", playbackAction(2))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_LOW);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

        Log.i(TAG, "Media control notification built");
    }

    private void removeNotification()
    {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);

        Log.i(TAG, "Notification removed");

    }

    /********************************** Action Intents *********************************/

    private PendingIntent playbackAction(int actionNumber) {
        Intent playbackAction = new Intent(this, AudioTrackService.class);
        switch (actionNumber) {
            case 0:
                // Play
                playbackAction.setAction(ACTION_PLAY);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 1:
                // Pause
                playbackAction.setAction(ACTION_PAUSE);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 2:
                // Next track
                playbackAction.setAction(ACTION_NEXT);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 3:
                // Previous track
                playbackAction.setAction(ACTION_PREVIOUS);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            default:
                break;
        }
        return null;
    }

    private void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;
        String actionString = playbackAction.getAction();
        if (actionString.contains("ACTION_PLAY")){
            transportControls.play();
        }
        else if (actionString.contains("ACTION_PAUSE")){
            transportControls.pause();
        }
        else if(actionString.contains("ACTION_NEXT")) {
            transportControls.skipToNext();
        }
        else if(actionString.contains("ACTION_PREVIOUS")) {
            transportControls.skipToPrevious();
        }
    }

    /*Handle incoming phone calls*/
    private void callStateListener() {
        // Get the telephony manager
        telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        //Starting listening for PhoneState changes
        phoneStateListener = new PhoneStateListener()
        {
            @Override
            public void onCallStateChanged(int state, String incomingNumber)
            {
                switch (state)
                {
                    //if at least one call exists or the phone is ringing
                    //pause the MediaPlayer
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (audioTrack != null)
                        {
                            pauseMedia();
                            ongoingCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        // Phone idle. Start playing.
                        if (audioTrack != null) {
                            if (ongoingCall) {
                                ongoingCall = false;
                                resumeMedia();
                            }
                        }
                        break;
                }
            }
        };
        // Register the listener with the telephony manager
        // Listen for changes to the device call state.
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        if(telephonyManager != null && phoneStateListener != null)
            Log.i(TAG, "PhoneState Listener added");
        else
            Log.e(TAG, "PhoneState Listener Init failed");
    }

    /********************************** Media Callbacks *********************************/

    /*Invoked when the audio focus of the system is updated*/
    @Override
    public void onAudioFocusChange(int focusChange)
    {
        switch(focusChange)
        {
            case AudioManager.AUDIOFOCUS_GAIN:
                if(audioTrack == null) initAudioTrack();
                else playMedia();
                audioTrack.setVolume(1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                stopMedia();
                audioTrack.release();
                audioTrack = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                pauseMedia();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if(audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)audioTrack.setVolume(0.1f);
                break;
        }
    }

    private BroadcastReceiver becomingNoisy = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //pause audio on ACTION_AUDIO_BECOMING_NOISY
            pauseMedia();
            buildNotification(PlaybackStatus.PAUSED);
        }
    };

    private BroadcastReceiver playNewAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopMedia();
            initAudioTrack();
            updateMetaData();
            buildNotification(PlaybackStatus.PLAYING);
        }
    };

    @Override
    public void onDestroy()
    {

        super.onDestroy();
        requestAudioFocus();
        if(audioTrack != null)
        {
            stopMedia();
            audioTrack.release();
        }
        removeAudioFocus();

        if(phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
            Log.i(TAG, "Remove PhoneState listener");
        }

        removeNotification();

        unregisterReceiver(becomingNoisy);
        unregisterReceiver(playNewAudio);
        stopSelf();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        onDestroy();
    }

    /********************************** Media Controls *********************************/

    /*Plays media*/
    public void playMedia()
    {

        if(audioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING)
            audioTrack.play();
    }

    /*Pause media*/
    public void pauseMedia()
    {
        if(audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)
            audioTrack.pause();
    }

    /*Stops media*/
    public void stopMedia()
    {
        if(audioTrack == null)return;
        if(audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)
            audioTrack.stop();
    }

    /*Resumes media*/
    public void resumeMedia()
    {
        if(audioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING)
        {
            audioTrack.flush();
            audioTrack.play();
        }
    }

    public static AudioTrack getTrack()
    {
        return audioTrack;
    }
}
