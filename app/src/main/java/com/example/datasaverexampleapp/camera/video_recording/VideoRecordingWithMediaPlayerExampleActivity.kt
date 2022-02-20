
package com.example.datasaverexampleapp.camera.video_recording

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.SurfaceHolder
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityVideoRecordingWithMediaPlayerExampleBinding
import com.example.datasaverexampleapp.type_alias.Layout
import java.io.IOException

/*
 * This is an example of intent to record video. It is the easiest and best practice way to initiate the video recording
 * by using the MediaStore.ACTION_VIDEO_CAPTURE action intent. In this example the media player will be used for
 * video playback.
 *
 * Starting this intent will launches a new activity with a video recorder app that's is capable of allowing
 * users to start, stop, review and retake their video. When they're satisfied, a URI to the recorded video is provided to
 * your activity as the data parameter of the returned Intent.
 *
 * A video capture action Intent can contain the following three optional extras:
 * - MediaStore.EXTRA_OUTPUT         - By default, the video recorded by the video capture action will
 *                                     be stored in the default Media Store. If you want to record it elsewhere,
 *                                     you can specify an alternative URI using this extra.
 *
 * - MediaStore.EXTRA_VIDEO_QUALITY - The video capture action allows you to specify an image quality using an
 *                                    integer value. There are currently two possible values: 0 for low (MMS) quality
 *                                    videos, or 1 for high (full resolution) videos. By default, the high-resolution
 *                                    mode is used.
 *
 * - MediaStore.EXTRA_DURATION_LIMIT - The maximum length of the recorded video (in seconds).
 *
 * Note: This is a better example that can be used in production since it works in most devices.
 */
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

        DataBindingUtil.setContentView<ActivityVideoRecordingWithMediaPlayerExampleBinding>(
            this, Layout.activity_video_recording_with_media_player_example
        ).apply {

            // surface view that will be used for the video playback.
            videoRecordingSurfaceView.apply {
                keepScreenOn = true
                holder?.apply {
                    addCallback(this@VideoRecordingWithMediaPlayerExampleActivity)
                    setFixedSize(400,300)
                }
            }

            val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

                startVideoExample2Button.isEnabled = true

                result.data?.let {

                    mediaplayer = MediaPlayer.create(this@VideoRecordingWithMediaPlayerExampleActivity,it.data).apply {
                        setOnPreparedListener(this@VideoRecordingWithMediaPlayerExampleActivity)
                    }
                }
            }

            startVideoExample2Button.setOnClickListener {
                it.isEnabled = false
                val launchRecording = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                resultLauncher.launch(launchRecording)
            }
        }
    }

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