@file:Suppress("DEPRECATION", "UNNECESSARY_SAFE_CALL")

package com.example.datasaverexampleapp.video_audio.background_audio

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.type_alias.AppString
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.util.Util

/**
 * This is the Media playback service, a subclass of [MediaBrowserServiceCompat]
 *
 * The 'onGetRoot' and 'onLoadChildren' methods are used to provide support for Android Auto and
 * Wear OS. They provide a list of media items that users can select from the Auto and Wear UIs in
 * order to start playback of specific songs, albums, or artist. It should return a non-null result
 * in 'onGetRoot', because a null result will cause connections to fail.
 *
 * Once your Media Browser Service has been constructed, in order for your Activities and other potential media playback clients
 * to connect to it, you must add it, with a corresponding 'android.media.browse.MediaBrowserService' Intent Filter to your manifest.
 *
 * <service android:name=".MediaPlaybackService"
 *          android:exported="true">
 *    <intent-filter>
 *        <action android:name="android.media.browse.MediaBrowserService" />
 *    </intent-filter>
 * </service>
 */
class ExoPlayerMediaPlaybackService : MediaBrowserServiceCompat(),
    AudioManager.OnAudioFocusChangeListener {
    private val TAG = javaClass.simpleName
    private var mediaSessionCompat: MediaSessionCompat? = null
    private var player: ExoPlayer? = null
    private lateinit var audioManager: AudioManager
    private var mediaSource: MediaSource? = null
    private lateinit var renderersFactory : DefaultRenderersFactory
    private lateinit var trackSelector : DefaultTrackSelector
    private var isStreamingMedia = false


    override fun onCreate() {
        super.onCreate()

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        renderersFactory = DefaultRenderersFactory(this@ExoPlayerMediaPlaybackService)
        trackSelector = DefaultTrackSelector()

        mediaSessionCompat = MediaSessionCompat(this, TAG)

        // Here you can set other initializations such as setFlags, setCallback, etc.

        // Initialize the Media Session within the Service's 'onCreate' method rather than the
        // playback Activity. The same should be done for all the media playback mechanisms
        // described previously as we move control media playback to this service.
        sessionToken = mediaSessionCompat?.sessionToken

        mediaSessionCompat?.setCallback(object : MediaSessionCompat.Callback() {

            override fun onPlay() {
                super.onPlay()

                player = null
                this@ExoPlayerMediaPlaybackService.mediaSource?.let { media_source ->

                    // Create a new Exo Player
//                    player = if (isStreamingMedia) {
//                        ExoPlayerFactory.newSimpleInstance(this@ExoPlayerMediaPlaybackService, trackSelector)
//                    } else {
//                        ExoPlayerFactory.newSimpleInstance(this@ExoPlayerMediaPlaybackService, DefaultTrackSelector())
//                    }

                    player = ExoPlayer.Builder(this@ExoPlayerMediaPlaybackService).build()

                    // Start loading the media source
                    player?.prepare(media_source)

                    val result = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                    {
                        val audioAttributes = AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build()
                        val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                            .setAudioAttributes(audioAttributes)
                            .setOnAudioFocusChangeListener(this@ExoPlayerMediaPlaybackService)
                            .build()
                        audioManager.requestAudioFocus(focusRequest)
                    } else {
                        audioManager.requestAudioFocus(this@ExoPlayerMediaPlaybackService,AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
                    }

                    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                    {
                        registerAudioBecomingNoisyReceiver()

                        // Start playback automatically when ready
                        player?.playWhenReady = true

                        // Call startService to keep your service alive during playback
                        startService(Intent(this@ExoPlayerMediaPlaybackService,ExoPlayerMediaPlaybackService::class.java))
                    }
                }
            }

            override fun onStop() {
                super.onStop()

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                    val audioAttributes = AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build()
                    val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                        .setAudioAttributes(audioAttributes)
                        .setOnAudioFocusChangeListener(this@ExoPlayerMediaPlaybackService)
                        .build()

                    audioManager.abandonAudioFocusRequest(focusRequest)
                } else {
                    audioManager.abandonAudioFocus(this@ExoPlayerMediaPlaybackService)
                }

                mediaSessionCompat?.isActive = false
                player?.stop()

                // Call stopSelf to allow your service to be destroyed now that playback has stopped
                stopSelf()
            }

            override fun onCommand(command: String?, extras: Bundle?, cb: ResultReceiver?) {

                isStreamingMedia = false
                mediaSource = null

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    when (command) {
                        ADD_MEDIA_SOURCE -> {

                            extras?.let {

                                val mediaSource = it.get(MEDIA_SOURCE)

                                when (mediaSource) {
                                    is Int ->
                                    {
                                        if (mediaSource >= 0) {

                                            //Build a DataSource.Factory capable of loading http and local content
                                            val dataSourceFactory = DefaultDataSourceFactory(this@ExoPlayerMediaPlaybackService,
                                                Util.getUserAgent(this@ExoPlayerMediaPlaybackService, getString(R.string.app_name)))

                                            // This is the MediaSource representing the media to be played.
                                            this@ExoPlayerMediaPlaybackService.mediaSource = ProgressiveMediaSource
                                                .Factory(dataSourceFactory)
                                                .createMediaSource(MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(mediaSource)))

                                            cb?.send(MEDIA_SOURCE_ADDED,null)

                                        } else {
                                            cb?.send(FAILED_ADDING_MEDIA_SOURCE,null)
                                        }
                                    }

                                    is String ->
                                    {
                                        isStreamingMedia = true
                                        val userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:40.0) Gecko/20100101 Firefox/40.0"
                                        val uri = Uri.parse(mediaSource)
                                        this@ExoPlayerMediaPlaybackService.mediaSource = ProgressiveMediaSource
                                            .Factory(DefaultDataSourceFactory(this@ExoPlayerMediaPlaybackService, userAgent))
                                            .createMediaSource(MediaItem.fromUri(uri))

                                        cb?.send(MEDIA_SOURCE_ADDED,null)
                                    }

                                    else -> cb?.send(FAILED_ADDING_MEDIA_SOURCE,null)
                                }

                            }?:cb?.send(FAILED_ADDING_MEDIA_SOURCE,null)
                        }
                    }

                    mediaSource?.apply {

                       addEventListener(Handler(), object : MediaSourceEventListener {

                           override fun onLoadStarted(windowIndex: Int, mediaPeriodId: MediaSource.MediaPeriodId?, loadEventInfo: LoadEventInfo, mediaLoadData: MediaLoadData) {
                               super.onLoadStarted(windowIndex, mediaPeriodId, loadEventInfo, mediaLoadData)

                              mediaSessionCompat?.setPlaybackState(

                                   PlaybackStateCompat.Builder()
                                       .setActions( if (isStreamingMedia) PlaybackStateCompat.ACTION_PLAY_FROM_URI else PlaybackStateCompat.ACTION_PLAY_PAUSE)
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
                    cb?.send(FAILED_ADDING_MEDIA_SOURCE,null)
                }
            }
        })
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot?
    {
        // Returning null == no one can connect so we'll return something
        return BrowserRoot(
            getString(AppString.app_name), // Name visible in Android Auto
            null // Bundle of optional extras
        )
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        // If you want to allow users to browse media content your app returns on Android Auto or Wear OS, return those results here.
        result.sendResult(ArrayList())
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

    override fun onDestroy() {
        player?.release()
        player = null
        super.onDestroy()
        audioBecomeNoisyReceiver?.let {
            try {
                unregisterReceiver(it)
            }catch (e:IllegalArgumentException)
            {
                e.printStackTrace()
            }
        }
    }

    override fun onAudioFocusChange(focusChange: Int) {
        Log.i("TAG", "focus change: ${focusChange}")
    }
}