@file:Suppress("DEPRECATION", "UNNECESSARY_SAFE_CALL")

package com.example.datasaverexampleapp.video_audio.foreground_service

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.KeyEvent
import androidx.core.app.NotificationCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.type_alias.AppString
import com.example.datasaverexampleapp.type_alias.Drawable
import com.example.datasaverexampleapp.utils.refinePath
import com.example.datasaverexampleapp.video_audio.background_audio.ADD_MEDIA_SOURCE
import com.example.datasaverexampleapp.video_audio.background_audio.FAILED_ADDING_MEDIA_SOURCE
import com.example.datasaverexampleapp.video_audio.background_audio.MEDIA_SOURCE
import com.example.datasaverexampleapp.video_audio.background_audio.MEDIA_SOURCE_ADDED
import com.example.datasaverexampleapp.video_audio.media_notification.CustomMediaStyleNotification
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.util.Util

/**
 * This is the foreground service example
 */
class ForegroundService :  MediaBrowserServiceCompat(), AudioManager.OnAudioFocusChangeListener, Player.Listener
{
    private val TAG = javaClass.simpleName
    private var mediaSessionCompat: MediaSessionCompat? = null
    private var player: ExoPlayer? = null
    private lateinit var audioManager: AudioManager
    private var mediaSource: MediaSource? = null
    private lateinit var renderersFactory : DefaultRenderersFactory
    private lateinit var trackSelector : DefaultTrackSelector
    private var mediaResource : Int? = null
    private var notification : NotificationCompat.Builder? = null
    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val playButton by lazy { NotificationCompat.Action(Drawable.ic_play_button,"play",MediaButtonReceiver.buildMediaButtonPendingIntent(this@ForegroundService, PlaybackStateCompat.ACTION_PLAY)) }
    private val pauseButton by lazy { NotificationCompat.Action(Drawable.ic_pause_button, "pause", MediaButtonReceiver.buildMediaButtonPendingIntent(this@ForegroundService, PlaybackStateCompat.ACTION_PAUSE)) }


    companion object {
        const val NOTIFICATION_ID = 201
    }

    override fun onCreate() {
        super.onCreate()

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        renderersFactory = DefaultRenderersFactory(this@ForegroundService)
        trackSelector = DefaultTrackSelector()

        mediaSessionCompat = MediaSessionCompat(this, TAG)

        // Here you can set other initializations such as setFlags, setCallback, etc.

        // Initialize the Media Session within the Service's 'onCreate' method rather than the
        // playback Activity. The same should be done for all the media playback mechanisms
        // described previously as we move control media playback to this service.
        sessionToken = mediaSessionCompat?.sessionToken

        mediaSessionCompat?.setCallback(object : MediaSessionCompat.Callback() {

            @SuppressLint("RestrictedApi")
            override fun onPlay() {
                super.onPlay()

                player?.let {

                    it.playWhenReady = true
                    it.playbackState

                    // Resume exo player
                    notification?.let { notificationBuilder ->

                        notificationBuilder.mActions?.apply {

                            if (isNotEmpty())
                            {
                                removeAt(0) // remove play button
                                add(0,pauseButton) // replace with pause button
                                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
                            }
                        }
                    }

                } ?: kotlin.run {

                    this@ForegroundService.mediaSource?.let { media_source ->

                        // Create a new Exo Player
                        player = ExoPlayer.Builder(this@ForegroundService).build().apply {
                            addListener(this@ForegroundService)
                        }

                        // Start loading the media source
                        player?.prepare(media_source)

                        val result =
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                val audioAttributes = AudioAttributes.Builder().setLegacyStreamType(
                                    AudioManager.STREAM_MUSIC
                                ).build()
                                val focusRequest =
                                    AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                                        .setAudioAttributes(audioAttributes)
                                        .setOnAudioFocusChangeListener(this@ForegroundService)
                                        .build()
                                audioManager.requestAudioFocus(focusRequest)
                            } else {
                                audioManager.requestAudioFocus(
                                    this@ForegroundService,
                                    AudioManager.STREAM_MUSIC,
                                    AudioManager.AUDIOFOCUS_GAIN
                                )
                            }

                        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                            registerAudioBecomingNoisyReceiver()

                            // Start playback automatically when ready
                            player?.playWhenReady = true

                            // Construct a Media Style Notification and start the foreground service
                            mediaSessionCompat?.let { mediaSessionCompat ->

                                mediaResource?.let {

                                    mediaSessionCompat.setMetadata(
                                        MediaMetadataCompat.Builder()
                                            .putString(
                                                MediaMetadataCompat.METADATA_KEY_ARTIST,
                                                it.refinePath()
                                            )
                                            .build()
                                    )

                                    notification = CustomMediaStyleNotification.create(
                                        mediaSessionCompat,
                                        this@ForegroundService
                                    ).apply {

                                        startForeground(NOTIFICATION_ID, build())
                                        registerMediaButtonReceiver()
                                    }
                                }
                            }
                        }
                    }
                }
            }

            override fun onStop() {
                super.onStop()
                stopPlayer()
            }

            @SuppressLint("RestrictedApi")
            override fun onPause() {
                super.onPause()
                unregisterAudioNoisyReceiver()

                player?.apply {
                    playWhenReady = false
                    playbackState
                }

                mediaSessionCompat?.setPlaybackState(

                    PlaybackStateCompat.Builder()
                        .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)
                        .setState(PlaybackStateCompat.STATE_PAUSED, 0L, 0.0f)
                        .build()
                )

                // Stop being a foreground service and remove the notification
                stopForeground(false)

                notification?.let { notificationBuilder ->

                    notificationBuilder.mActions?.apply {

                        if (isNotEmpty())
                        {
                            removeAt(0) // remove play button
                            add(0,playButton)
                            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
                        }
                    }
                }
            }

            override fun onCommand(command: String?, extras: Bundle?, cb: ResultReceiver?) {

                mediaSource = null

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    when (command) {
                        ADD_MEDIA_SOURCE -> {

                            extras?.let {

                                it.get(MEDIA_SOURCE)?.let {

                                    if (it is Int) {
                                        mediaResource = it

                                        when (mediaResource) {
                                            is Int -> {
                                                if (it >= 0) {

                                                    //Build a DataSource.Factory capable of loading http and local content
                                                    val dataSourceFactory =
                                                        DefaultDataSourceFactory(
                                                            this@ForegroundService,
                                                            Util.getUserAgent(
                                                                this@ForegroundService, getString(
                                                                    R.string.app_name
                                                                )
                                                            )
                                                        )

                                                    // This is the MediaSource representing the media to be played.
                                                    this@ForegroundService.mediaSource =
                                                        ProgressiveMediaSource
                                                            .Factory(dataSourceFactory)
                                                            .createMediaSource(MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(it)))

                                                    cb?.send(MEDIA_SOURCE_ADDED, null)

                                                } else {
                                                    cb?.send(FAILED_ADDING_MEDIA_SOURCE, null)
                                                }
                                            }

                                            else -> cb?.send(FAILED_ADDING_MEDIA_SOURCE, null)
                                        }
                                    }
                                }
                            } ?: cb?.send(FAILED_ADDING_MEDIA_SOURCE, null)
                        }
                    }

                    mediaSource?.apply {

                        addEventListener(Handler(), object : MediaSourceEventListener {

                            override fun onLoadStarted(windowIndex: Int, mediaPeriodId: MediaSource.MediaPeriodId?, loadEventInfo: LoadEventInfo, mediaLoadData: MediaLoadData) {
                                super.onLoadStarted(windowIndex, mediaPeriodId, loadEventInfo, mediaLoadData)

                                mediaSessionCompat?.setPlaybackState(

                                    PlaybackStateCompat.Builder()
                                        .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)
                                        .setState(PlaybackStateCompat.STATE_PLAYING, 0L, 0.0f)
                                        .build()
                                )
                            }

                            override fun onLoadCanceled(windowIndex: Int, mediaPeriodId: MediaSource.MediaPeriodId?, loadEventInfo: LoadEventInfo, mediaLoadData: MediaLoadData) {
                                super.onLoadCanceled(windowIndex, mediaPeriodId, loadEventInfo, mediaLoadData)
                                mediaSessionCompat?.setPlaybackState(
                                    PlaybackStateCompat.Builder()
                                        .setActions(PlaybackStateCompat.ACTION_STOP)
                                        .setState(PlaybackStateCompat.STATE_STOPPED, 0L, 0.0f)
                                        .build()
                                )
                            }
                        })
                    }
                } else {
                    cb?.send(FAILED_ADDING_MEDIA_SOURCE, null)
                }
            }

            override fun onMediaButtonEvent(mediaButtonEvent: Intent?): Boolean {

                mediaButtonEvent?.action?.let { action ->

                    if (action == Intent.ACTION_MEDIA_BUTTON) {
                        mediaButtonEvent.extras?.let {

                            val keyEventValue = it.get(Intent.EXTRA_KEY_EVENT)

                            if (keyEventValue is KeyEvent) {
                                when (keyEventValue.keyCode) {
                                    KeyEvent.KEYCODE_MEDIA_PAUSE -> onPause()
                                    KeyEvent.KEYCODE_MEDIA_STOP -> onStop()
                                    KeyEvent.KEYCODE_MEDIA_PLAY -> onPlay()
                                }
                            }
                        }
                    }
                }

                return true
            }
        })
    }

    private fun stopPlayer()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            val audioAttributes =
                AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build()
            val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setOnAudioFocusChangeListener(this@ForegroundService)
                .build()

            audioManager.abandonAudioFocusRequest(focusRequest)
        } else {
            audioManager.abandonAudioFocus(this@ForegroundService)
        }

        mediaSessionCompat?.isActive = false
        player?.stop()

        // Stop being a foreground service and remove the notification
        stopForeground(true)

        // Then call stopSelf to allow your service to be destroyed now that playback has stopped
        stopSelf()

        cancelNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // This will route Notification commands into your Media Session and Media Controller,
        // allowing you to handle them as you would playback controls within your Activity.
        MediaButtonReceiver.handleIntent(mediaSessionCompat, intent)
        return super.onStartCommand(intent, flags, startId)
    }

    private val audioBecomeNoisyReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent?) {

            intent?.let { intentReceived ->

                if (intentReceived.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
                    Log.i(javaClass.simpleName, "action audio becoming noisy!")
                }
            }
        }
    }

    private fun registerAudioBecomingNoisyReceiver() {
        registerReceiver(
            audioBecomeNoisyReceiver,
            IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        )
    }

    private fun registerMediaButtonReceiver() {
        registerReceiver(
            audioBecomeNoisyReceiver,
            IntentFilter(Intent.ACTION_MEDIA_BUTTON)
        )
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot?
    {
        // Returning null == no one can connect so we'll return something
        return BrowserRoot(
            getString(AppString.app_name), // Name visible in Android Auto
            null // Bundle of optional extras
        )
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        // If you want to allow users to browse media content your app returns on Android Auto or Wear OS, return those results here.
        result.sendResult(ArrayList())
    }

    override fun onDestroy() {
        player?.release()
        player = null
        super.onDestroy()
        unregisterAudioNoisyReceiver()
    }

    private fun unregisterAudioNoisyReceiver()
    {
        audioBecomeNoisyReceiver?.let {
            try {
                unregisterReceiver(it)
            }catch (e: IllegalArgumentException)
            {
                e.printStackTrace()
            }
        }
    }

    override fun onAudioFocusChange(focusChange: Int) {
        Log.i(TAG, "focus change: ${focusChange}")
    }

    private fun cancelNotification()
    {
        // In previous version of Android 11 you cannot dismiss the media notification, but since the patch update from dec 2020, it is possible
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }

    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
        Log.i("TAG","onTimelineChanged")
    }

    override fun onLoadingChanged(isLoading: Boolean) {
        Log.i("TAG","onLoadingChanged")
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        Log.i("TAG","onRepeatModeChanged")

        if (playbackState == Player.STATE_ENDED)
        {
            stopPlayer()
        }
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
        Log.i("TAG","onRepeatModeChanged")
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
        Log.i("TAG","onShuffleModeEnabledChanged")
    }

    override fun onPlayerError(error: PlaybackException) {
        Log.i("TAG","onPlayerError")
    }

    override fun onPositionDiscontinuity(reason: Int) {
        Log.i("TAG","onPositionDiscontinuity")
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
        Log.i("TAG","onPlaybackParametersChanged")
    }

    override fun onSeekProcessed() {
        Log.i("TAG","onSeekProcessed")
    }
}