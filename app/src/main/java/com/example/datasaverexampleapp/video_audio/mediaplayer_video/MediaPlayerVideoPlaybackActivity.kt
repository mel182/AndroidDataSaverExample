package com.example.datasaverexampleapp.video_audio.mediaplayer_video

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_media_player_video_playback.*
import java.io.IOException

/**
 * This is example of the media player video playback example from the raw directory.
 * Media player can be used to play audio and video stored in application resource, local files, content providers,
 * or streamed from a network URL. The 'MediaPlayer' class is available as part of the Android framework on all devices
 * for supporting audio and video playback.
 *
 * Note: For application supporting Android 4.1 (API Level 16) or later, the ExoPlayer library is available as an alternative to
 *       Media Player API. Details on using ExoPlayer are described later in this chapter.
 *
 *  The Media Player's management of audio/video files and streams is handled as a state machine. In the most simplistic
 *  term, transitions through the state machine can be described as follows:
 *  1. Initialize the Media Player with media to play
 *  2. Prepare the Media Player for playback
 *  3. Start the playback
 *  4. Pause or stop the playback prior to it completing
 *  5. The playback is complete
 *
 *  Important: Alternatively, you can use the 'setDataSource' method on an existing Media Player instance. This method accepts a file path
 *             , content provider URI, streaming media URL path, or File Descriptor. Because preparing the data source involves potentially
 *             expensive operations like fetching data over the network and decoding the data stream, you should never call the 'prepare'
 *             method on the UI thread. Instead set a 'MediaPlayer.OnPrepareListener' and use 'prepareAsync' to keep your UI responsive while
 *             preparing for media playback.
 *
 *  To stream Internet media using the Media Player, your application manifest must include the 'INTERNET' permission:
 *  '<uses-permission android:name="android.permission.INTERNET" />'
 *
 *  Android supports a limited number of simultaneous Media Player objects; not releasing them can cause runtime exceptions
 *  when the system runs out. When you finish playback, call 'release' on your Media Player object to free the associated
 *  resources:
 *
 *  'mediaplayer.release()'
 *
 *  WARNING: 'MediaPlayer' is a relatively expensive objects to create and maintain, so you should avoid creating multiple instances.
 *           Consider using the 'SoundPool' class if you need low-latency playback of many audio streams such as would be common
 *           in a game with background music and multiple sound effects.
 *
 *  Note: Prior to Android 7.0 (API level 24), each 'SurfaceView' was rendered in its own window, separately from the rest of your
 *        UI. As a result, unlike View-derived classes it could not be moved, transformed, or animated. As an alternative for earlier
 *        platform versions, the 'TextureView' class offers support for these operations, but is less battery efficient.
 */
class MediaPlayerVideoPlaybackActivity : AppCompatActivity(), SurfaceHolder.Callback, MediaPlayer.OnPreparedListener {

    private lateinit var mediaplayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player_video_playback)

        // This specify which audio stream should be controlled by the volume keys while
        // the current 'Activity' is active. You can specify any of the available audio
        // streams, but when using the Media Player, you should specify the STREAM_MUSIC
        // stream to make it the focus of the volume keys.
        //
        // WARNING:
        // Although it's also possible to listen for volume key pressed directly, this is
        // considered poor practice. A user can modify the audio volume in several ways,
        // including the hardware buttons as well as software controls. Triggering volume
        // changes manually based only on the hardware buttons is likely to make your application
        // respond unexpectedly and frustrate your users. Frustrated user lower your application's
        // volume by uninstalling it.
        volumeControlStream = AudioManager.STREAM_MUSIC

        // This approach is if you have a valid file path for media player set datasource
        // mediaplayer = MediaPlayer()

        mediaplayer = MediaPlayer.create(this, R.raw.sample_video).apply {
            setOnPreparedListener(this@MediaPlayerVideoPlaybackActivity)
        }
        buttonPlay?.isEnabled = true

        surface_view?.apply {
            keepScreenOn = true
            holder?.apply {
                addCallback(this@MediaPlayerVideoPlaybackActivity)
                setFixedSize(400,300)
            }
        }

        buttonPlay?.setOnClickListener {
            mediaplayer.start()
            buttonPlay?.isEnabled = false
            buttonPause?.isEnabled = true
        }

        buttonPause?.setOnClickListener {
            mediaplayer.pause()
            buttonPlay?.isEnabled = true
            buttonPause?.isEnabled = false
        }

        buttonSkip?.setOnClickListener {
            mediaplayer.seekTo(mediaplayer.duration / 2)
        }

        close_button?.setOnClickListener {
            onBackPressed()
        }
    }


    override fun surfaceCreated(holder:SurfaceHolder)
    {

        try {

            // When the surface is created, assign it as the
            // display surface and assign and prepare a data
            // source
            mediaplayer.setDisplay(holder)

            // Specify the path, URL, or Content Provider URI of
            // the video resource to play

            // val videoFilePath = "android.resource://${packageName}/${R.raw.sample_video}" // does not work
            // mediaplayer.setDataSource(videoFilePath) // Provide your data source
            // mediaplayer.prepare()

        }catch (e: IllegalArgumentException)
        {
            Log.i("TAG234","Illegal argument exception: ${e.message}")
        } catch (e: IllegalStateException)
        {
            Log.i("TAG234","Illegal state exception: ${e.message}")
        }catch (e : SecurityException)
        {
            Log.i("TAG234","Security exception: ${e.message}")
        } catch (e : IOException)
        {
            Log.i("TAG234","IO exception: ${e.message}")
        }
    }

    override fun surfaceChanged(holder:SurfaceHolder, format:Int, width:Int, height:Int) { }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        mediaplayer.release()
    }

    // When a Media player has finished preparing, the associated on Prepare Listener handler will be triggered.
    override fun onPrepared(mp: MediaPlayer?) {

        // You can start the media player from here as well.
        Log.i("TAG234","On prepared: ${mp}")
    }
}