package com.example.datasaverexampleapp.video_audio.background_audio

import android.annotation.SuppressLint
import android.content.*
import android.media.AudioFocusRequest
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.RemoteException
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.android.synthetic.main.activity_audio_playback_example.*

/**
 * This is the background audio playback example Activity Example
 * Android  provides the 'MediaBrowserServiceCompat' and 'MediaBrowserCompat' API's, to simplify
 * the separation of your audio playback Service from any connected clients-including your playback
 * Activity.
 *
 * NOTE: As with the 'MediaSession' class, Android 5.0 (API Level 21) introduced a 'MediaBrowserService'
 *       and 'MediaBrowser' class. However, we strongly recommended using the 'MediaBrowserServiceCompat'
 *       and 'MediaBrowserCompat' from the Android Support Library, and will use the compatibility library
 *       classes throughout this chapter.
 *
 */
class BackgroundAudioExampleActivity : AppCompatActivity()
{
    private var mediaBrowser: MediaBrowserCompat? = null
    private var mediaController: MediaControllerCompat? = null
    private var mediaSession : MediaSessionCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Layout.activity_background_audio_example)
        title = "Background audio example"

        // While your activity no longer has direct access to underlying Media Player, your Activity can connect
        // to your Media Browser Service, and create a new Media Controller using the 'MediaBrowserCompat' API.

        // Create the MediaBrowserCompat
        mediaBrowser = MediaBrowserCompat(this,
        ComponentName(this, MediaPlaybackService::class.java),
        object : MediaBrowserCompat.ConnectionCallback(){

            override fun onConnected() {
                super.onConnected()

                Log.i("TAG","onConnected and media browser: ${mediaBrowser}")

                try {
                    // We can construct a media controller from the session's token
                    mediaBrowser?.let {
                        Log.i("TAG","Creating media controller!")

                        mediaController = MediaControllerCompat(this@BackgroundAudioExampleActivity, it.sessionToken).apply {

                            // To ensure your UI stays in sync with your Service, register a 'MediaControllerCompat.Callback' using the 'registerCallback'
                            // method on the Media Controller. This will ensure you receive a callback whenever the metadata or playback state changes, allowing
                            // you to keep your UI updated at all times.
                            registerCallback(object : MediaControllerCompat.Callback(){

                                override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                                    // Update the UI based on playback state change
                                    Log.i("TAG","onPlaybackStateChanged: ${state}")

                                }

                                override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
                                    // Update the UI based on Media Metadata change
                                    Log.i("TAG","onMetadataChanged: ${metadata}")
                                }
                            })
                        }

                        mediaController?.sendCommand("play",null, object: ResultReceiver(Handler()) {

                        })


                    }
                }catch (e:RemoteException)
                {
                    Log.i("TAG","Error creating controller: ${e.message}")
                }
            }

            override fun onConnectionSuspended() {
                super.onConnectionSuspended()
                // We were connected, but no longer are.
                Log.i("TAG","onConnectionSuspended")
            }

            override fun onConnectionFailed() {
                super.onConnectionFailed()
                Log.i("TAG","onConnectionFailed")
                // The attempt to connect failed completely.
                // Check the ComponentName!
            }
        },null)

        Log.i("TAG","media browser: ${mediaBrowser}")
        mediaBrowser?.connect()

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaBrowser?.disconnect()
    }
}