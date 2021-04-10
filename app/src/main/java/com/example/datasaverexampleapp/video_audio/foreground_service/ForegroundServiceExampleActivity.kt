package com.example.datasaverexampleapp.video_audio.foreground_service

import android.content.ComponentName
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.RemoteException
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.util.TypedValue
import android.view.View
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.type_alias.Layout
import com.example.datasaverexampleapp.video_audio.background_audio.ADD_MEDIA_SOURCE
import com.example.datasaverexampleapp.video_audio.background_audio.ExoPlayerMediaPlaybackService
import com.example.datasaverexampleapp.video_audio.background_audio.MEDIA_SOURCE
import com.example.datasaverexampleapp.video_audio.background_audio.MEDIA_SOURCE_ADDED
import kotlinx.android.synthetic.main.item_player_layout.*

/**
 * This is a foreground service example.
 * Services in Android run in the background and be killed to free resources as needed. Interruptions in audio playback is very noticeable to users,
 * so it's a good practice to give your Service foreground priority when you begin media playback, to minimize the possibility of interrupted playback.
 *
 * NOTE: Foreground Services required an associated Notification to be visible while running.
 *
 * IMPORTANT:
 * Your Service should only maintain foreground priority when it is actively playing audio, as described by the following process:
 * 1. Call 'startForeground' (passing in a Media Style Notification) when you begin media playback.
 * 2. Call 'stopForeground(false)' when playback is paused to remove the foreground status, but maintain the notification
 * 3. Call 'stopForeground(true)' when playback has stopped, to remove the foreground status and remove the notification.
 */
class ForegroundServiceExampleActivity : AppCompatActivity()
{
    private var mediaBrowser: MediaBrowserCompat? = null
    private var mediaController: MediaControllerCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Layout.activity_foreground_example)
        title = "Foreground Service example"

        playerTitle?.text = "Media player"
        updateStatus("Initializing....")

        mediaBrowser = MediaBrowserCompat(this,
            ComponentName(this, ForegroundService::class.java),
            object : MediaBrowserCompat.ConnectionCallback(){

                override fun onConnected() {
                    super.onConnected()
                    initializeMediaBrowserAndController()
                    updateStatus("-")
                }

                override fun onConnectionSuspended() {
                    super.onConnectionSuspended()
                    // We were connected, but no longer are.
                    updateStatus("Service connection suspended")
                }

                override fun onConnectionFailed() {
                    super.onConnectionFailed()
                    // The attempt to connect failed completely.
                    // Check the ComponentName!
                    updateStatus("Service Connection failed")
                }
            },null)

        mediaBrowser?.connect()

        playAudioButton?.setOnClickListener {

            when (playAudioButton?.text)
            {
                "Add Media Audio" -> {
                    updateStatus("Added media....")
                    addPlayAudio(R.raw.bon_vibe)
                    streamAudioButton?.isEnabled = false
                }
                "Play Audio" -> {
                    mediaController?.transportControls?.play()
                    playAudioButton?.text = "Stop Audio"
                    updateStatus("Preparing....")
                }
                "Stop Audio" -> {
                    mediaController?.transportControls?.stop()
                    updateStatus("-")
                    playAudioButton?.text = "Add Media Audio"
                    streamAudioButton?.isEnabled = true
                }
            }
        }

        streamAudioButton?.visibility = View.INVISIBLE
    }

    private fun updateStatus(status:String)
    {
        streamStatus?.text = status
    }

    private fun initializeMediaBrowserAndController()
    {
        try {
            // We can construct a media controller from the session's token
            mediaBrowser?.let {

                mediaController = MediaControllerCompat(this, it.sessionToken).apply {

                    // To ensure your UI stays in sync with your Service, register a 'MediaControllerCompat.Callback' using the 'registerCallback'
                    // method on the Media Controller. This will ensure you receive a callback whenever the metadata or playback state changes, allowing
                    // you to keep your UI updated at all times.
                    registerCallback(object : MediaControllerCompat.Callback(){

                        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                            // Update the UI based on playback state change

                            Log.i("TAG","onPlaybackStateChanged foreground activity line 123")

                            state?.let{

                                if (it.actions == PlaybackStateCompat.ACTION_PLAY_PAUSE && it.state == PlaybackStateCompat.STATE_PLAYING)
                                {
                                    updateStatus("Playing...")
                                } else if (it.actions == PlaybackStateCompat.ACTION_PLAY_FROM_URI && it.state == PlaybackStateCompat.STATE_PLAYING)
                                {
                                    updateStatus("Streaming...")
                                } else if (it.actions == PlaybackStateCompat.ACTION_STOP && it.state == PlaybackStateCompat.STATE_STOPPED)
                                {
                                    Log.i("TAG","Action stop!")
                                    updateStatus("-")
                                    playAudioButton?.isEnabled = true
                                    streamAudioButton?.isEnabled = true
                                }
                            }
                        }

                        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
                            // Update the UI based on Media Metadata change
                            Log.i("TAG","onMetadataChanged foreground activity line 123")
                        }
                    })
                }
            }
        }catch (e: RemoteException)
        {
            Log.i("TAG","Error creating controller: ${e.message}")
        }
    }

    private fun addPlayAudio(mediaSource:Any)
    {
        val mediaBundle : Bundle = Bundle().apply {

            if (mediaSource is Int)
                putInt(MEDIA_SOURCE,mediaSource)
            else if (mediaSource is String)
                putString(MEDIA_SOURCE,mediaSource)
        }

        mediaSource?.let {
            mediaController?.sendCommand(ADD_MEDIA_SOURCE, mediaBundle, object : ResultReceiver(
                Handler()
            )
            {
                override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                    super.onReceiveResult(resultCode, resultData)
                    streamStatus?.text = if (resultCode == MEDIA_SOURCE_ADDED) "Media added, ready to play" else "Failed to add media, try again"

                    if (mediaSource is Int)
                    {
                        playAudioButton?.text = "Play Audio"
                    } else if (mediaSource is String)
                    {
                        streamAudioButton?.text = "Play Stream Audio"
                    }
                }
            })
        }?: kotlin.run {
            updateStatus("Failed adding media source, try again")
            playAudioButton?.isEnabled = true
            streamAudioButton?.isEnabled = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("TAG","onDestroy")
    }
}