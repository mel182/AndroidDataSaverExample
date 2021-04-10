package com.example.datasaverexampleapp.video_audio.foreground_service

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.util.TypedValue
import android.view.InputEvent
import android.view.KeyEvent
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.type_alias.AppString
import com.example.datasaverexampleapp.utils.refinePath
import com.example.datasaverexampleapp.video_audio.background_audio.*
import com.example.datasaverexampleapp.video_audio.media_notification.CustomMediaStyleNotification
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MediaSourceEventListener
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.util.Util
import java.io.IOException

/**
 * This is the foreground service example
 */
class ForegroundService :  MediaBrowserServiceCompat(), AudioManager.OnAudioFocusChangeListener
{
    private val TAG = javaClass.simpleName
    private var mediaSessionCompat: MediaSessionCompat? = null
    private var player: SimpleExoPlayer? = null
    private lateinit var audioManager: AudioManager
    private var mediaSource: MediaSource? = null
    private lateinit var renderersFactory : DefaultRenderersFactory
    private lateinit var trackSelector : DefaultTrackSelector
    private var mediaResource : Int? = null

    companion object {
        const val NOTIFICATION_ID = 201
    }

    override fun onCreate() {
        super.onCreate()

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        renderersFactory = DefaultRenderersFactory(
            this@ForegroundService,
            DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
        )
        trackSelector = DefaultTrackSelector()

        mediaSessionCompat = MediaSessionCompat(this, TAG)

//        mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS or MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS)

        /*
        mediaSession.setFlags( MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS or MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS )
connector.setPlayer(player)
        */


        // Here you can set other initializations such as setFlags, setCallback, etc.

        // Initialize the Media Session within the Service's 'onCreate' method rather than the
        // playback Activity. The same should be done for all the media playback mechanisms
        // described previously as we move control media playback to this service.
        sessionToken = mediaSessionCompat?.sessionToken

        mediaSessionCompat?.setCallback(object : MediaSessionCompat.Callback() {

            override fun onPlay() {
                super.onPlay()

                player?.let {
                    it.apply {
                        playWhenReady = true
                        playbackState
                    }

                } ?: kotlin.run {

                    this@ForegroundService.mediaSource?.let { media_source ->

                        // Create a new Exo Player
                        player = ExoPlayerFactory.newSimpleInstance(
                            this@ForegroundService,
                            DefaultTrackSelector()
                        )

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

                                    startForeground(
                                        NOTIFICATION_ID, CustomMediaStyleNotification.create(
                                            mediaSessionCompat,
                                            this@ForegroundService
                                        )
                                    )

                                    registerMediaButtonReceiver()
                                }
                            }
                        }
                    }
                }
            }

            override fun onStop() {
                super.onStop()


                Log.i("TAG", "on stop service")

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
            }

            override fun onPause() {
                super.onPause()

                Log.i("TAG", "on pause foreground service")

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
            }

            override fun onCommand(command: String?, extras: Bundle?, cb: ResultReceiver?) {

                Log.i("TAG", "onCommand: ${command}")

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
                                                        ExtractorMediaSource
                                                            .Factory(dataSourceFactory)
                                                            .createMediaSource(
                                                                RawResourceDataSource.buildRawResourceUri(
                                                                    it
                                                                )
                                                            )

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

                            override fun onMediaPeriodCreated(
                                windowIndex: Int,
                                mediaPeriodId: MediaSource.MediaPeriodId?
                            ) {
                            }

                            override fun onMediaPeriodReleased(
                                windowIndex: Int,
                                mediaPeriodId: MediaSource.MediaPeriodId?
                            ) {
                            }

                            override fun onLoadStarted(
                                windowIndex: Int,
                                mediaPeriodId: MediaSource.MediaPeriodId?,
                                loadEventInfo: MediaSourceEventListener.LoadEventInfo?,
                                mediaLoadData: MediaSourceEventListener.MediaLoadData?
                            ) {
                                mediaSessionCompat?.setPlaybackState(

                                    PlaybackStateCompat.Builder()
                                        .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)
                                        .setState(PlaybackStateCompat.STATE_PLAYING, 0L, 0.0f)
                                        .build()
                                )
                            }

                            override fun onLoadCompleted(
                                windowIndex: Int,
                                mediaPeriodId: MediaSource.MediaPeriodId?,
                                loadEventInfo: MediaSourceEventListener.LoadEventInfo?,
                                mediaLoadData: MediaSourceEventListener.MediaLoadData?
                            ) {
                            }

                            override fun onLoadCanceled(
                                windowIndex: Int,
                                mediaPeriodId: MediaSource.MediaPeriodId?,
                                loadEventInfo: MediaSourceEventListener.LoadEventInfo?,
                                mediaLoadData: MediaSourceEventListener.MediaLoadData?
                            ) {
                                mediaSessionCompat?.setPlaybackState(
                                    PlaybackStateCompat.Builder()
                                        .setActions(PlaybackStateCompat.ACTION_STOP)
                                        .setState(PlaybackStateCompat.STATE_STOPPED, 0L, 0.0f)
                                        .build()
                                )
                            }

                            override fun onLoadError(
                                windowIndex: Int,
                                mediaPeriodId: MediaSource.MediaPeriodId?,
                                loadEventInfo: MediaSourceEventListener.LoadEventInfo?,
                                mediaLoadData: MediaSourceEventListener.MediaLoadData?,
                                error: IOException?,
                                wasCanceled: Boolean
                            ) {
                            }

                            override fun onReadingStarted(
                                windowIndex: Int,
                                mediaPeriodId: MediaSource.MediaPeriodId?
                            ) {
                            }

                            override fun onUpstreamDiscarded(
                                windowIndex: Int,
                                mediaPeriodId: MediaSource.MediaPeriodId?,
                                mediaLoadData: MediaSourceEventListener.MediaLoadData?
                            ) {
                            }

                            override fun onDownstreamFormatChanged(
                                windowIndex: Int,
                                mediaPeriodId: MediaSource.MediaPeriodId?,
                                mediaLoadData: MediaSourceEventListener.MediaLoadData?
                            ) {
                            }
                        }
                        )
                    }
                } else {
                    cb?.send(FAILED_ADDING_MEDIA_SOURCE, null)
                }
            }

            override fun onMediaButtonEvent(mediaButtonEvent: Intent?): Boolean {

                mediaButtonEvent?.action?.let { action ->

                    if (action == Intent.ACTION_MEDIA_BUTTON) {
                        Log.i("TAG", "on media button event!")

                        mediaButtonEvent?.extras?.let {

                            //Bundle[{android.intent.extra.KEY_EVENT=KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_MEDIA_PLAY_PAUSE, scanCode=0, metaState=0, flags=0x0, repeatCount=0, eventTime=0, downTime=0, deviceId=-1, source=0x0, displayId=0 }}]

                            for (keyFound in it.keySet()) {
                                Log.i("TAG","Key found: ${keyFound}")
                            }

                            //android.view.InputEvent

                            //85
                            val test = it.get("android.intent.extra.KEY_EVENT")
                            Log.i("TAG", "test: ${test}")


                            if (test is KeyEvent)
                            {
                                Log.i("TAG", "is key event")

                                val inputEvent = test as KeyEvent

                                if (inputEvent.keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
                                {
                                    Log.i("TAG", "pause button clicked!")
                                } else if (inputEvent.keyCode == KeyEvent.KEYCODE_MEDIA_NEXT){
                                    Log.i("TAG", "skip button clicked")
                                }

                            } else {
                                Log.i("TAG", "is not key event")
                            }

                            val pause =
                                it.getLong(PlaybackStateCompat.ACTION_PLAY_PAUSE.toString()) // 512
                            val skip =
                                it.getLong(PlaybackStateCompat.ACTION_SKIP_TO_NEXT.toString()) // 32

                            Log.i("TAG", "Pause: ${pause}")
                            Log.i("TAG", "Skip: ${skip}")
                        }
                    }
                }




                return true
            }

        })
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
}