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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.FragmentExoPlayerBinding
import com.example.datasaverexampleapp.type_alias.Layout

/**
 * A simple [Fragment] subclass.
 * Use the [ExoPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExoPlayerFragment : Fragment()  {

    private var mediaBrowser: MediaBrowserCompat? = null
    private var mediaController: MediaControllerCompat? = null
    private var binding: FragmentExoPlayerBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(Layout.fragment_exo_player, container, false)

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // While your activity no longer has direct access to underlying Media Player, your Activity can connect
        // to your Media Browser Service, and create a new Media Controller using the 'MediaBrowserCompat' API.

        // Create the MediaBrowserCompat
        activity?.let { fragmentActivity ->


            binding = DataBindingUtil.setContentView<FragmentExoPlayerBinding>(
                fragmentActivity, Layout.fragment_exo_player
            ).apply {
                playerLayout.playerTitle.text = "Exo player"
                updateStatus("Initializing....")

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

                playerLayout.playAudioButton?.setOnClickListener {

                    when (playerLayout.playAudioButton?.text)
                    {
                        "Add Media Audio" -> {
                            updateStatus("Added media....")
                            addPlayAudio(R.raw.bon_vibe)
                            playerLayout.streamAudioButton?.isEnabled = false
                        }
                        "Play Audio" -> {
                            mediaController?.transportControls?.play()
                            playerLayout.playAudioButton.text = "Stop Audio"
                            updateStatus("Preparing....")
                        }
                        "Stop Audio" -> {
                            mediaController?.transportControls?.stop()
                            updateStatus("-")
                            playerLayout.playAudioButton?.text = "Add Media Audio"
                            playerLayout.streamAudioButton?.isEnabled = true
                        }
                    }
                }

                playerLayout.streamAudioButton?.setOnClickListener {

                    when (playerLayout.streamAudioButton?.text)
                    {
                        "Add Stream Audio" -> {
                            updateStatus("Added streaming media....")
                            addPlayAudio("https://stream.audioxi.com/SW")
                            playerLayout.playAudioButton.isEnabled = false
                        }
                        "Play Stream Audio" -> {
                            mediaController?.transportControls?.play()
                            playerLayout.streamAudioButton?.text = "Stop Streaming Audio"
                        }
                        "Stop Streaming Audio" -> {
                            mediaController?.transportControls?.stop()
                            updateStatus("-")
                            playerLayout.streamAudioButton?.text = "Add Stream Audio"
                            playerLayout.playAudioButton?.isEnabled = true
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaBrowser?.disconnect()
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
            mediaController?.sendCommand(ADD_MEDIA_SOURCE, mediaBundle, object : ResultReceiver(Handler())
            {
                @SuppressLint("SetTextI18n")
                override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                    super.onReceiveResult(resultCode, resultData)
                    binding?.apply {

                        playerLayout.streamStatus.text = if (resultCode == MEDIA_SOURCE_ADDED) "Media added, ready to play" else "Failed to add media, try again"

                        if (mediaSource is Int)
                        {
                            playerLayout.playAudioButton?.text = "Play Audio"
                        } else if (mediaSource is String)
                        {
                            playerLayout.streamAudioButton?.text = "Play Stream Audio"
                        }
                    }
                }
            })
        }?: kotlin.run {
            updateStatus("Failed adding media source, try again")
            binding?.apply {
                playerLayout.playAudioButton?.isEnabled = true
                playerLayout.streamAudioButton?.isEnabled = true
            }
        }
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
                                    binding?.apply {
                                        playerLayout.playAudioButton?.isEnabled = true
                                        playerLayout.streamAudioButton?.isEnabled = true
                                    }
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

    private fun updateStatus(status:String)
    {
        binding?.playerLayout?.streamStatus?.text = status
    }

    private fun getParentActivity() : BackgroundAudioExampleActivity?
    {
        return if (activity is BackgroundAudioExampleActivity)
            activity as BackgroundAudioExampleActivity
        else
            null
    }
}