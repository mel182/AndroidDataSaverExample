
package com.example.datasaverexampleapp.camera.video_recording

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.widget.MediaController
import androidx.activity.result.contract.ActivityResultContracts
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_media_player_video_playback.*
import kotlinx.android.synthetic.main.activity_media_player_video_playback.surface_view
import kotlinx.android.synthetic.main.activity_video_recording_example.*
import kotlinx.android.synthetic.main.activity_video_recording_with_media_player_example.*
import java.io.IOException

class VideoRecordingWithMediaPlayerExampleActivity : AppCompatActivity(R.layout.activity_video_recording_with_media_player_example),
    SurfaceHolder.Callback, MediaPlayer.OnPreparedListener  {

    private lateinit var mediaplayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // WARNING:
        // Although it's also possible to listen for volume key pressed directly, this is
        // considered poor practice. A user can modify the audio volume in several ways,
        // including the hardware buttons as well as software controls. Triggering volume
        // changes manually based only on the hardware buttons is likely to make your application
        // respond unexpectedly and frustrate your users. Frustrated user lower your application's
        // volume by uninstalling it.
        volumeControlStream = AudioManager.STREAM_MUSIC

        //video_recording_surface_view

        video_recording_surface_view?.apply {
            keepScreenOn = true
            holder?.apply {
                addCallback(this@VideoRecordingWithMediaPlayerExampleActivity)
                setFixedSize(400,300)
            }
        }


        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            start_video_example2_button?.isEnabled = true

            result.data?.let {

                mediaplayer = MediaPlayer.create(this,it.data).apply {
                    setOnPreparedListener(this@VideoRecordingWithMediaPlayerExampleActivity)
                }
            }
        }

        start_video_example2_button?.setOnClickListener {
            it.isEnabled = false
            val launchRecording = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            resultLauncher.launch(launchRecording)
        }


    }

    /*
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
    */

    override fun surfaceCreated(p0: SurfaceHolder) {
        try {

            mediaplayer.setDisplay(p0)
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

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) { }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        mediaplayer.release()
    }

    override fun onPrepared(p0: MediaPlayer?) {
        Log.i("TAG234","On prepared: ${p0}")
        mediaplayer.start()
    }
}