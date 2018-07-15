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
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import splitsound.com.audio.opus.OpusAudioThread;
import splitsound.com.net.AppPacket;
import splitsound.com.net.RTPNetworking;
import splitsound.com.splitsound.DrawerActivityTest;
import splitsound.com.splitsound.HomeActivity;
import splitsound.com.splitsound.R;
import splitsound.com.splitsound.SplitSoundApplication;

/**
 * Service to handle user control over media playback and
 * allow for background playback to remove restriction to only
 * the application
 *
 * @version 0.0.1
 * @author Neel, Emanuel
 */
public class AudioTrackService extends Service implements
        AudioManager.OnAudioFocusChangeListener
{
    //TODO: Try to reduce static uses...find alternatives because static is not good

    private static final String TAG = "AudioTrackService";

    // Audio instances to manage audio data
    public static AudioTrack audioTrack;
    private AudioManager audioManager;

    // Help handle incoming calls
    private boolean ongoingCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;

    // Opus supported audio sample rate
    public static final int SAMPLE_RATE = 44100;

    // Number of samples per frame is not arbitrary,
    // it must match one of the predefined values, specified in the standard.
    public static final int FRAME_SIZE = 160;
    public static final int NUM_CHANNELS = 1;

    // Pending Intent actions triggerd by the Media Session listener
    public static final String ACTION_PLAY = "splitsound.com.audio.ACTION_PLAY";
    public static final String ACTION_PAUSE = "splitsound.com.audio.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "splitsound.com.audio.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "splitsound.com.audio.ACTION_NEXT";
    public static final String ACTION_STOP = "splitsound.com.audio.ACTION_STOP";

    // Media session managers
    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private static MediaControllerCompat.TransportControls transportControls;

    // SplitSound media control notification id
    private static final int NOTIFICATION_ID = 101;

    /********************************** Initialization *********************************/

    /**
     * Executed when the service is initiated
     *
     */
    @Override
    public void onCreate()
    {
        super.onCreate();

        // Initialize media session and the audio track
        // for media playback
        if(mediaSessionManager == null)
        {
            try {
                initAudioTrack();
                initMediaSession();
            } catch(RemoteException e)
            {
                e.printStackTrace();
                stopSelf();
            }
            buildNotification(PlaybackStatus.PLAYING);
        }

        // Initialize necessary listeners
        initNoisyReceiver();
        callStateListener();
        initPlayNewAudio();

        Log.i(TAG, "Audio service initiated and audio focus acquired");
    }

    /**
     * Triggered everytime a new pending intent is collected
     * from the Pending Intent queue
     *
     * @param intent The next intent in the Pending Intent queue
     * @param flags
     * @param startId The state at which the session should be restarted
     *                Ex: Sticky, Not sticky etc.
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        // Request audio focus for successful
        // audio playback
        if(!requestAudioFocus()) {
            Log.e(TAG, "Could not obtain audio focus....exiting");
            stopSelf();
        }

        // Handle the next intent from the intent queue
        handleIncomingActions(intent);

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Bind the service to an activity
     *
     * @param intent The intent that is binded to the activity
     *
     * @return The binder that confirms the activity bind
     */
    @Override
    public IBinder onBind(Intent intent)
    {
        return new Binder();
    }

    /**
     * Request system for audio focus to instantiate
     * audio playback
     *
     * @return Success status of the focus request
     */
    private boolean requestAudioFocus()
    {
        audioManager = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        if(audioManager == null)
            Log.e(TAG, "Audio manager unitialized");
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    /**
     * Removes the audio focus from the Android system
     *
     * @return Success status of the removed focus
     */
    private boolean removeAudioFocus()
    {
        if(audioManager == null)
            Log.e(TAG, "Audio Manager error: audio manager is null");
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.abandonAudioFocus(this);
    }

    /**
     * Initializes the audio track that inputs audio
     * buffers for audio playback
     *
     */
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

        // Start the audio track
        audioTrack.play();
    }

    /**
     * Initializes the media session that allows
     * interaction with media controllers and acts as a bridge
     * between the service and the UI controls
     *
     * @throws RemoteException
     */
    private void initMediaSession() throws RemoteException
    {
        if(mediaSession!= null)return;

        mediaSessionManager = (MediaSessionManager)getApplicationContext().getSystemService(Context.MEDIA_SESSION_SERVICE);

        // Init media session and transport controls for media controls
        mediaSession = new MediaSessionCompat(getApplicationContext(),"SplitSound Player");
        transportControls = mediaSession.getController().getTransportControls();
        mediaSession.setActive(true);

        // Set flags to allow media control buttons as response
        mediaSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Update media session metadata for user info
        updateMetaData();

        // Attach callback to receive media session updates
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
            public void onStop()
            {
                super.onStop();
                stopMedia();
                RTPNetworking.requestQ.add(AppPacket.BYE);
                removeNotification();
                stopSelf();
            }
        });

        Log.i(TAG, "Media session initialized and callbacks set");
    }

    /**
     * Initializes noisy receiver that shuts down audio
     * when external audio sources disconnect
     *
     */
    private void initNoisyReceiver()
    {
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        if(intentFilter == null)
            Log.e(TAG, "becomingNoisyReceiver: intent init failed");
        registerReceiver(becomingNoisy, intentFilter);
    }

    /**
     * Plays new audio
     *
     * TODO: Look into what this actually does.....Remove if not really important
     */
    private void initPlayNewAudio()
    {
        IntentFilter filter = new IntentFilter(OpusAudioThread.Broadcast_PLAY_NEW_AUDIO);
        registerReceiver(playNewAudio, filter);
    }

    /********************************** Session Updates *********************************/

    /**
     * Update audio metadata based on incoming audio streams
     */
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

    /**
     * Builds the media control notifications that allows
     * users to control the audio playback from outside the application
     *
     * @param playbackStatus State of the playback if audio is paused or playing
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void buildNotification(PlaybackStatus playbackStatus)
    {
        int notificationAction = R.drawable.ic_pause_black_40dp;
        String notificationText = "Pause";
        PendingIntent play_pauseAction = playbackAction(1);

        //Build a new notification according to the current state of the MediaPlayer
        if (playbackStatus == PlaybackStatus.PAUSED)
        {
            notificationAction = R.drawable.ic_play_arrow_black_40dp;
            notificationText = "Play";
            play_pauseAction = playbackAction(0);
        }


        MediaControllerCompat controller = mediaSession.getController();
        MediaMetadataCompat mediaMetadata = controller.getMetadata();
        MediaDescriptionCompat description = mediaMetadata.getDescription();

        // Allows user to tap on notification to redirect to the application
        Intent intent = new Intent(this, DrawerActivityTest.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Create a new Notification
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext(), "notify_001")
                .setContentTitle(description.getTitle())
                .setContentText(description.getSubtitle())
                .setSubText(description.getDescription())
                .setLargeIcon(description.getIconBitmap())
                .setSmallIcon(R.drawable.ic_headset_mic_black_24dp)
                .setContentIntent(pendingIntent)
                .setShowWhen(false)
                .setAutoCancel(false)
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(0))
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentInfo("Streaming audio...")
                .addAction(notificationAction, notificationText, play_pauseAction)
                .addAction(R.drawable.ic_leave_white, "Leave", playbackAction(2));

        // Add a notification channel on newer Android OS
        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "SplitSound",
                    NotificationManager.IMPORTANCE_LOW);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

        Log.i(TAG, "Media control notification built");
    }

    /**
     * Removes the notification from the notification bar
     *
     */
    private void removeNotification()
    {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);

        Log.i(TAG, "Notification removed");
    }

    /********************************** Action Intents *********************************/

    /**
     * Creates a service pending intent and adds it to pending queue
     * based on the action performed on the media controls
     *
     * @param actionNumber control action that is performed
     * @return New Pending Intent
     */
    private PendingIntent playbackAction(int actionNumber)
    {
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
                playbackAction.setAction(ACTION_STOP);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            default:
                break;
        }
        return null;
    }

    /**
     * Handles the next Pending Intent from the intent queue
     *
     * @param playbackAction The intent that needs to be handled by transport controls
     */
    private void handleIncomingActions(Intent playbackAction)
    {
        if (playbackAction == null || playbackAction.getAction() == null) return;
        String actionString = playbackAction.getAction();

        if (actionString.contains("ACTION_PLAY"))
            transportControls.play();
        else if (actionString.contains("ACTION_PAUSE"))
            transportControls.pause();
        else if(actionString.contains("ACTION_STOP"))
            transportControls.stop();
    }

    /**
     * Handle incoming phone calls
     *
     */
    private void callStateListener()
    {
        // Get the telephony manager from Android system
        telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        // Listen for phone state changes
        phoneStateListener = new PhoneStateListener()
        {
            @Override
            public void onCallStateChanged(int state, String incomingNumber)
            {
                switch (state)
                {
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (audioTrack != null)
                        {
                            pauseMedia();
                            ongoingCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        if (audioTrack != null)
                        {
                            if (ongoingCall)
                            {
                                ongoingCall = false;
                                resumeMedia();
                            }
                        }
                        break;
                }
            }
        };

        // Register the listener with the telephony manager
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        if(telephonyManager != null && phoneStateListener != null)
            Log.i(TAG, "PhoneState Listener added");
        else
            Log.e(TAG, "PhoneState Listener Init failed");
    }

    /********************************** Media Callbacks *********************************/

    /*Invoked when the audio focus of the system is updated*/

    /**
     * Handle audio focus changes
     *
     * @param focusChange Focus change state
     */
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

    /**
     * Pauses media when device becomes noisy
     *
     */
    private BroadcastReceiver becomingNoisy = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //pause audio on ACTION_AUDIO_BECOMING_NOISY
            pauseMedia();
            buildNotification(PlaybackStatus.PAUSED);
        }
    };

    /**
     * Plays media when new audio invoked from external sources
     *
     */
    private BroadcastReceiver playNewAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopMedia();
            initAudioTrack();
            updateMetaData();
            buildNotification(PlaybackStatus.PLAYING);
        }
    };

    /**
     * Executed when the service is disconnected or application is
     * low on resources
     *
     */
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

        // Remove audio focus
        removeAudioFocus();

        // Remove phone state listener
        if(phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
            Log.i(TAG, "Remove PhoneState listener");
        }

        // Remove the notification
        removeNotification();

        unregisterReceiver(becomingNoisy);
        unregisterReceiver(playNewAudio);

        // Stop the service
        stopSelf();
    }

    /**
     * Forces onDestroy to be called becasue application quit does
     * not invoke onDestroy for services
     *
     * @param rootIntent Intent that invoked the service
     */
    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        super.onTaskRemoved(rootIntent);
        onDestroy();
    }

    /********************************** Media Controls *********************************/

    /**
     * Plays the media
     *
     */
    public void playMedia()
    {

        if(audioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING)
            audioTrack.play();
    }

    /**
     * Pauses the media
     *
     */
    public void pauseMedia()
    {
        if(audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)
            audioTrack.pause();
    }

    /**
     * Stops the media
     */
    public void stopMedia()
    {
        if(audioTrack == null)return;
        if(audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)
            audioTrack.stop();
    }

    /**
     * Resumes the media
     */
    public void resumeMedia()
    {
        if(audioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING)
        {
            audioTrack.flush();
            audioTrack.play();
        }
    }

    /**
     * Returns the audio track for adding to
     * audio buffer
     *
     * @return AudioTrack instance
     */
    public static AudioTrack getTrack()
    {
        return audioTrack;
    }

    /**
     * Returns the controls for UI to send play/pause commands
     *
     * @return TransportControls instance
     */
    public static MediaControllerCompat.TransportControls getTransportControls()
    {
        return transportControls;
    }
}
