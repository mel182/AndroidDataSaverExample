@file:Suppress("DEPRECATION", "ControlFlowWithEmptyBody", "RedundantNullableReturnType",
    "UNNECESSARY_SAFE_CALL"
)

package com.example.datasaverexampleapp.video_audio.background_audio

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.example.datasaverexampleapp.type_alias.AppString
import com.google.android.exoplayer2.source.MediaSource
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class MediaPlayerPlaybackService  : MediaBrowserServiceCompat(), AudioManager.OnAudioFocusChangeListener
{
    private val TAG = javaClass.simpleName
    private var mediaPlayer:MediaPlayer? = null
    private var mediaSessionCompat: MediaSessionCompat? = null
    private var mediaSource: Any? = null
    private var isStreamingMedia = false
    private lateinit var audioManager: AudioManager

    override fun onCreate() {
        super.onCreate()

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        mediaSessionCompat = MediaSessionCompat(this,TAG).apply {

            setCallback(object: MediaSessionCompat.Callback(){

                override fun onPlay() {
                    super.onPlay()

                    mediaPlayer = null

                    this@MediaPlayerPlaybackService.mediaSource?.let {

                        when(it)
                        {
                            is String -> {
//                                mediaPlayer = MediaPlayer.create(this@MediaPlayerPlaybackService,it)
                                val mediaPlayer = MediaPlayer()
                                mediaPlayer.setOnPreparedListener(object : MediaPlayer.OnPreparedListener{
                                    override fun onPrepared(mp: MediaPlayer?) {
                                        mediaPlayer.start()
                                        Log.i("TAG","on Prepared")
                                        mediaSessionCompat?.setPlaybackState(

                                            PlaybackStateCompat.Builder()
                                                .setActions( if (isStreamingMedia) PlaybackStateCompat.ACTION_PLAY_FROM_URI else PlaybackStateCompat.ACTION_PLAY_PAUSE)
                                                .setState(PlaybackStateCompat.STATE_PLAYING, 0L, 0.0f)
                                                .build()
                                        )

                                    }
                                })

                                try{
                                    mediaPlayer.setDataSource(it)
                                    mediaPlayer.prepareAsync()
                                    isStreamingMedia = true
                                }catch (e: IllegalArgumentException)
                                {
                                    e.printStackTrace()
                                    Log.i("TAG","IllegalArgumentException: ${e.message}")
                                    mediaSessionCompat?.setPlaybackState(
                                        PlaybackStateCompat.Builder()
                                            .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)
                                            .setState(PlaybackStateCompat.STATE_ERROR, 0L, 0.0f)
                                            .build()
                                    )

                                }catch (e:SecurityException)
                                {
                                    e.printStackTrace()
                                    Log.i("TAG","SecurityException: ${e.message}")
                                    mediaSessionCompat?.setPlaybackState(
                                        PlaybackStateCompat.Builder()
                                            .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)
                                            .setState(PlaybackStateCompat.STATE_ERROR, 0L, 0.0f)
                                            .build()
                                    )
                                }catch (e: IllegalStateException)
                                {
                                    e.printStackTrace()
                                    Log.i("TAG","IllegalStateException: ${e.message}")
                                    mediaSessionCompat?.setPlaybackState(
                                        PlaybackStateCompat.Builder()
                                            .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)
                                            .setState(PlaybackStateCompat.STATE_ERROR, 0L, 0.0f)
                                            .build()
                                    )
                                }catch (e: IOException)
                                {
                                    e.printStackTrace()
                                    Log.i("TAG","IOException: ${e.message}")

                                    mediaSessionCompat?.setPlaybackState(
                                        PlaybackStateCompat.Builder()
                                            .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)
                                            .setState(PlaybackStateCompat.STATE_ERROR, 0L, 0.0f)
                                            .build()
                                    )

                                }
                            }

                            is Int -> {
                                mediaPlayer = MediaPlayer.create(this@MediaPlayerPlaybackService,it)
//                                mediaPlayer?.start()

                                val result = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                                {
                                    val audioAttributes = AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build()
                                    val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                                        .setAudioAttributes(audioAttributes)
                                        .setOnAudioFocusChangeListener(this@MediaPlayerPlaybackService)
                                        .build()
                                    audioManager.requestAudioFocus(focusRequest)
                                } else {
                                    audioManager.requestAudioFocus(this@MediaPlayerPlaybackService,AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
                                }

                                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                                {
                                    registerAudioBecomingNoisyReceiver()

                                    mediaPlayer?.start()
                                    // Call startService to keep your service alive during playback
                                    startService(Intent(this@MediaPlayerPlaybackService,MediaPlayerPlaybackService::class.java))

                                    mediaSessionCompat?.setPlaybackState(

                                        PlaybackStateCompat.Builder()
                                            .setActions( if (isStreamingMedia) PlaybackStateCompat.ACTION_PLAY_FROM_URI else PlaybackStateCompat.ACTION_PLAY_PAUSE)
                                            .setState(PlaybackStateCompat.STATE_PLAYING, 0L, 0.0f)
                                            .build()
                                    )

                                } else {}
                            }

                            else -> {}
                        }
                    }


                }

                override fun onStop() {
                    super.onStop()

                    mediaSessionCompat?.isActive = false
                    mediaPlayer?.stop()

                    mediaSessionCompat?.setPlaybackState(
                        PlaybackStateCompat.Builder()
                            .setActions(PlaybackStateCompat.ACTION_STOP)
                            .setState(PlaybackStateCompat.STATE_STOPPED, 0L, 0.0f)
                            .build()
                    )

                    stopSelf()
                }

                override fun onCommand(command: String?, extras: Bundle?, cb: ResultReceiver?) {

                    command?.let {

                        isStreamingMedia = false

                        if (it == ADD_MEDIA_SOURCE)
                        {
                            extras?.let {
                                mediaSource =  it.get(MEDIA_SOURCE)
                                cb?.send(MEDIA_SOURCE_ADDED,null)
                            }
                        }
                    }?: cb?.send(FAILED_ADDING_MEDIA_SOURCE,null)
                }

            })
        }
        sessionToken = mediaSessionCompat?.sessionToken
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

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {
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

    override fun onAudioFocusChange(focusChange: Int) {
        Log.i("TAG", "focus change: ${focusChange}")
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
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

}