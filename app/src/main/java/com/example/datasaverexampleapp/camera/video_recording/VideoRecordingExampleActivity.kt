package com.example.datasaverexampleapp.camera.video_recording

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.MediaController
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.android.synthetic.main.activity_image_capture_intent_example.*
import kotlinx.android.synthetic.main.activity_video_recording_example.*

/**
 * This is an example of intent to record video. It is the easiest and best practice way to initiate the video recording
 * by using the MediaStore.ACTION_VIDEO_CAPTURE action intent.
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
 * Note: This example uses the video view which does not work on all devices. For a production approach use the media player
 *       for playback.
 */
class VideoRecordingExampleActivity : AppCompatActivity(Layout.activity_video_recording_example)
{
    private var resultLauncher : ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            // get activity Result
            result.data?.let {

                videoView?.apply {
                    setMediaController(MediaController(this@VideoRecordingExampleActivity))
                    setVideoURI(it.data)
                    start()
                }
            }
        }

        start_video_example_button?.setOnClickListener {
            it.visibility = View.GONE
            // Generate the Intent
            val launchRecording = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            // Launch the camera app
            resultLauncher?.launch(launchRecording)
        }
    }
}