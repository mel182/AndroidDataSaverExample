package com.example.datasaverexampleapp.camera.video_recording

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.MediaController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.android.synthetic.main.activity_image_capture_intent_example.*
import kotlinx.android.synthetic.main.activity_video_recording_example.*

class VideoRecordingExampleActivity : AppCompatActivity(Layout.activity_video_recording_example)
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

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
            val launchRecording = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            resultLauncher.launch(launchRecording)
        }
    }
}