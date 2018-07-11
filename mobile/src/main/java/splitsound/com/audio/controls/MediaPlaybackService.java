package splitsound.com.audio.controls;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;

import splitsound.com.audio.controls.PlaybackStatus;
import splitsound.com.splitsound.R;
import splitsound.com.splitsound.SplitSoundApplication;

/**
 * Created by Neel on 7/8/2018.
 */

public class MediaPlaybackService extends MediaBrowserServiceCompat implements
        AudioManager.OnAudioFocusChangeListener
{
    private static final String TAG = "MediaPlaybackService";

    private static final String MY_MEDIA_ROOT_ID = "media_root_id";

    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;

    // Creates Media player instance
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

    @Override
    public void onCreate() {
        super.onCreate();

        if(!requestAudioFocus()) {
            Log.e(TAG, "Could not obtain audio focus....exiting");
            stopSelf();
        }

        callStateListener();
        initAudioTrack();
        initMediaSession();

        Log.i(TAG, "Audio service initiated and audio focus acquired");

        updateMetaData();
        buildNotification(PlaybackStatus.PLAYING);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        MediaButtonReceiver.handleIntent(mediaSession, intent);
        return START_NOT_STICKY;
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

        Log.i(TAG, "AudioTrack initialized");
    }

    public void initMediaSession()
    {
        mediaSession = new MediaSessionCompat(SplitSoundApplication.getAppContext(), "Player");
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay()
            {
                super.onPlay();

                if(!requestAudioFocus())
                    stopSelf();

                Log.d(TAG, "Temp");
                mediaSession.setActive(true);
                playMedia();
                buildNotification(PlaybackStatus.PAUSED);
                startForeground(101, buildNotification(PlaybackStatus.PLAYING).build());
            }

            @Override
            public void onPause()
            {
                super.onPause();

                Log.d(TAG, "Temp");
                pauseMedia();
                buildNotification(PlaybackStatus.PLAYING);
                stopForeground(false);
            }

            @Override
            public void onStop()
            {
                super.onStop();

                stopMedia();
                removeNotification();
                stopSelf();
            }
        });

        setSessionToken(mediaSession.getSessionToken());
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        removeNotification();
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

        stopSelf();
    }

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
                if(audioTrack != null)
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

    public void callStateListener()
    {
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

    public void updateMetaData()
    {
        Bitmap albumArt = BitmapFactory.decodeResource(getResources(), R.drawable.image);

        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "Artisite")
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, "Where is this?")
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "Am I player?")
                .build());
    }

    public void removeNotification()
    {
        stopForeground(true);
    }

    public NotificationCompat.Builder buildNotification(PlaybackStatus status)
    {
        int notificationAction = android.R.drawable.ic_media_pause;//needs to be initialized
        PendingIntent play_pauseAction = null;

        //Build a new notification according to the current state of the MediaPlayer
        if (status == PlaybackStatus.PLAYING)
            notificationAction = android.R.drawable.ic_media_pause;
            //create the pause action
        else if (status == PlaybackStatus.PAUSED)
            notificationAction = android.R.drawable.ic_media_play;
            //create the play action

        MediaControllerCompat controller = mediaSession.getController();
        MediaMetadataCompat mediaMetadata = controller.getMetadata();
        MediaDescriptionCompat description = mediaMetadata.getDescription();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(SplitSoundApplication.getAppContext(), "notify_001");
        builder.setContentTitle(description.getTitle())
                .setContentText(description.getSubtitle())
                .setSubText(description.getDescription())
                .setLargeIcon(description.getIconBitmap())

                // Enable launching the player by clicking the notification
                .setContentIntent(controller.getSessionActivity())

                // Stop the service when the notification is swiped away
                .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(SplitSoundApplication.getAppContext(),
                        PlaybackStateCompat.ACTION_STOP))

                // Make the transport controls visible on the lockscreen
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                // Add an app icon and set its accent color
                // Be careful about the color
                .setSmallIcon(android.R.drawable.btn_default)
                .setColor(ContextCompat.getColor(SplitSoundApplication.getAppContext(), R.color.colorPrimary))

                // Add a pause button
                .addAction(
                        notificationAction, "Pause",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(SplitSoundApplication.getAppContext(),
                                PlaybackStateCompat.ACTION_PLAY))

                // Take advantage of MediaStyle features
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(0)

                        // Add a cancel button
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(SplitSoundApplication.getAppContext(),
                                PlaybackStateCompat.ACTION_STOP)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("notify_001", "Test Channel", importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        startForeground(101, builder.build());

        return builder;
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints)
    {
        return new BrowserRoot(MY_MEDIA_ROOT_ID, null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result)
    {
        result.sendResult(null);
    }

    /********************************** Media Player control functions*********************************/

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
            audioTrack.play();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        onDestroy();
    }
}
