package com.example.datasaverexampleapp.video_audio.background_audio

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.type_alias.AppString
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_audio_playback_example.*

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
class ExoPlayerMediaPlaybackService : MediaBrowserServiceCompat(), AudioManager.OnAudioFocusChangeListener
{
    private val TAG = javaClass.simpleName
    private var mediaSessionCompat:MediaSessionCompat? = null
    private var player: SimpleExoPlayer? = null

    override fun onCreate() {
        super.onCreate()

        // Create a new Exo Player
        player = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())

        //Build a DataSource.Factory capable of loading http and local content
        val dataSourceFactory = DefaultDataSourceFactory(
            this, Util.getUserAgent(
                this,
                getString(R.string.app_name)
            )
        )

        // This is the MediaSource representing the media to be played.
        val rawDirectoryResource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(RawResourceDataSource.buildRawResourceUri(R.raw.bon_vibe))

        // Start loading the media source
//        player?.prepare(rawDirectoryResource)

        mediaSessionCompat = MediaSessionCompat(this, TAG)

        // Here you can set other initializations such as setFlags, setCallback, etc.

        // Initialize the Media Session within the Service's 'onCreate' method rather than the
        // playback Activity. The same should be done for all the media playback mechanisms
        // described previously as we move control media playback to this service.
        sessionToken = mediaSessionCompat?.sessionToken


        mediaSessionCompat?.setCallback(object : MediaSessionCompat.Callback(){

            override fun onPlay() {
                super.onPlay()

                getSystemService(Context.AUDIO_SERVICE).apply {

                    val audioManager = this as AudioManager
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        val result = audioManager.requestAudioFocus({

                            Log.i("TAG","on audio focus change: ${it}")

                        },AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)

                        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                        {
                            registerAudioBecomingNoisyReceiver()
                            this@ExoPlayerMediaPlaybackService.mediaSessionCompat?.isActive = true

                            //http://38.96.148.28:11832/stream?type=.mp3

                            val userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:40.0) Gecko/20100101 Firefox/40.0"
                            val uri = Uri.parse("http://38.96.148.28:11832/stream?type=.mp3")

                            val dataSourceFactory = DefaultDataSourceFactory(this@ExoPlayerMediaPlaybackService, userAgent, DefaultBandwidthMeter())




//                            val dataSourceFactory = DefaultHttpDataSourceFactory(
//                                userAgent,
//                                null,
//                                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
//                                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
//                                true)

                            val mediaSource2 = ExtractorMediaSource.Factory(dataSourceFactory)
                                .setExtractorsFactory(DefaultExtractorsFactory())
                                .createMediaSource(uri)



                            // Start loading the media source
                            player?.prepare(mediaSource2)

                            // Start playback automatically when ready
                            player?.playWhenReady = true


                            startService(Intent(this@ExoPlayerMediaPlaybackService,ExoPlayerMediaPlaybackService::class.java))
                        }
                    }
                }
            }

            override fun onStop() {
                super.onStop()

                val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
                audioManager.abandonAudioFocus(this@ExoPlayerMediaPlaybackService)

                mediaSessionCompat?.isActive = false
                player?.stop()


                stopSelf()
            }

            override fun onCommand(command: String?, extras: Bundle?, cb: ResultReceiver?) {
                Log.i("TAG","command: ${command}")

                if(command == "play")
                {
                    getSystemService(Context.AUDIO_SERVICE).apply {

                        val audioManager = this as AudioManager
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            val result = audioManager.requestAudioFocus({

                                Log.i(javaClass.simpleName,"on audio focus change: ${it}")

                            },AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)

                            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                            {
                                registerAudioBecomingNoisyReceiver()
                                this@ExoPlayerMediaPlaybackService.mediaSessionCompat?.isActive = true

                                val userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:40.0) Gecko/20100101 Firefox/40.0"
//                                val uri = Uri.parse("http://38.96.148.28:11832/stream?type=.mp3")
                                val uri = Uri.parse("http://158.69.114.190:8072/;?1617555395334")
//                                val uri = Uri.parse("https://stream.audioxi.com/SW")

                                val dataSourceFactory = DefaultDataSourceFactory(this@ExoPlayerMediaPlaybackService, userAgent, DefaultBandwidthMeter())


                                val renderersFactory = DefaultRenderersFactory(this@ExoPlayerMediaPlaybackService,
                                    null,
                                    DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF)

                                val trackSelector = DefaultTrackSelector()
                                player = null

                                player = ExoPlayerFactory.newSimpleInstance(renderersFactory,trackSelector)

                                val mediaSource2 = ExtractorMediaSource(uri,
                                    DefaultDataSourceFactory(this@ExoPlayerMediaPlaybackService,userAgent),
                                    DefaultExtractorsFactory(),
                                    null,
                                    null)

//                            val dataSourceFactory = DefaultHttpDataSourceFactory(
//                                userAgent,
//                                null,
//                                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
//                                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
//                                true)

//                                val mediaSource2 = ExtractorMediaSource.Factory(dataSourceFactory)
//                                    .setExtractorsFactory(DefaultExtractorsFactory())
//                                    .createMediaSource(uri)


                                // Start loading the media source
                                player?.prepare(mediaSource2)

                                // Start playback automatically when ready
                                player?.playWhenReady = true


                                startService(Intent(this@ExoPlayerMediaPlaybackService,ExoPlayerMediaPlaybackService::class.java))
                            }
                        }
                    }
                }

            }

        })
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {

        Log.i("TAG","onGetRoot")

        // Returning null == no one can connect so we'll return something
        return BrowserRoot(
            getString(AppString.app_name), // Name visible in Android Auto
            null) // Bundle of optional extras
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>)
    {
        Log.i("TAG","onLoadChildren")
        // If you want to allow users to browse media content your app returns on Android Auto or Wear OS, return those results here.
        result.sendResult(ArrayList())
    }

    private val audioBecomeNoisyReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {  intentReceived ->
                if (intentReceived.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY)
                {
                    Log.i(javaClass.simpleName,"action audio becoming noisy!")
                    //audio_noisy_textView?.text = "Audio becomes noisy!"
                }
            }
        }
    }

    private fun registerAudioBecomingNoisyReceiver()
    {
        registerReceiver(audioBecomeNoisyReceiver, IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY))
    }

    override fun onDestroy() {
        player?.release()
        player = null
        super.onDestroy()
        unregisterReceiver(audioBecomeNoisyReceiver)
    }

    override fun onAudioFocusChange(focusChange: Int) {
        Log.i(javaClass.simpleName,"focus change: ${focusChange}")
    }

}