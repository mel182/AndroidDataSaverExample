@file:Suppress("UNNECESSARY_SAFE_CALL", "DEPRECATION")

package com.example.datasaverexampleapp.video_audio.background_audio

import android.annotation.SuppressLint
import android.content.ComponentName
import android.os.Bundle
import android.os.Handler
import android.os.RemoteException
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.android.synthetic.main.item_player_layout.*

/**
 * A simple [Fragment] subclass.
 * Use the [MediaPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MediaPlayerFragment : Fragment() {

    private var mediaBrowser: MediaBrowserCompat? = null
    private var mediaController: MediaControllerCompat? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(Layout.fragment_media_player, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerTitle?.text = "Media player"
        updateStatus("Initializing....")

        activity?.let { fragmentActivity ->

            mediaBrowser = MediaBrowserCompat(fragmentActivity,
                ComponentName(fragmentActivity, ExoPlayerMediaPlaybackService::class.java),
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
        }

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

        streamAudioButton?.setOnClickListener {

            when (streamAudioButton?.text)
            {
                "Add Stream Audio" -> {
                    updateStatus("Added streaming media....")
                    addPlayAudio("https://stream.audioxi.com/SW")
                    playAudioButton?.isEnabled = false
                }
                "Play Stream Audio" -> {
                    mediaController?.transportControls?.play()
                    streamAudioButton?.text = "Stop Streaming Audio"
                }
                "Stop Streaming Audio" -> {
                    mediaController?.transportControls?.stop()
                    updateStatus("-")
                    streamAudioButton?.text = "Add Stream Audio"
                    playAudioButton?.isEnabled = true
                }
            }
        }
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

                mediaController = MediaControllerCompat(activity, it.sessionToken).apply {

                    // To ensure your UI stays in sync with your Service, register a 'MediaControllerCompat.Callback' using the 'registerCallback'
                    // method on the Media Controller. This will ensure you receive a callback whenever the metadata or playback state changes, allowing
                    // you to keep your UI updated at all times.
                    registerCallback(object : MediaControllerCompat.Callback(){

                        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                            // Update the UI based on playback state change

                            state?.let{

                                if (it.actions == PlaybackStateCompat.ACTION_PLAY_PAUSE && it.state == PlaybackStateCompat.STATE_PLAYING)
                                {
                                    updateStatus("Playing...")
                                    getParentActivity()?.getPlayerTabLayout()?.visibility = View.GONE

                                } else if (it.actions == PlaybackStateCompat.ACTION_PLAY_FROM_URI && it.state == PlaybackStateCompat.STATE_PLAYING)
                                {
                                    updateStatus("Streaming...")
                                    getParentActivity()?.getPlayerTabLayout()?.visibility = View.GONE
                                } else if (it.actions == PlaybackStateCompat.ACTION_STOP && it.state == PlaybackStateCompat.STATE_STOPPED)
                                {
                                    Log.i("TAG","Action stop!")
                                    updateStatus("-")
                                    playAudioButton?.isEnabled = true
                                    streamAudioButton?.isEnabled = true
                                    getParentActivity()?.getPlayerTabLayout()?.visibility = View.VISIBLE
                                }
                            }
                        }

                        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
                            // Update the UI based on Media Metadata change
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
                @SuppressLint("SetTextI18n")
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

    private fun getParentActivity() : BackgroundAudioExampleActivity?
    {
        return if (activity is BackgroundAudioExampleActivity)
            activity as BackgroundAudioExampleActivity
        else
            null
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaBrowser?.disconnect()
    }

}