@file:Suppress("UNNECESSARY_SAFE_CALL")

package com.example.datasaverexampleapp.camera

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.camera.controlling_the_camera.ControllingCameraExampleActivity
import com.example.datasaverexampleapp.camera.image_capture_intent.ImageCaptureIntentExampleActivity
import com.example.datasaverexampleapp.camera.video_recording.VideoRecordingExampleActivity
import com.example.datasaverexampleapp.databinding.ActivityCameraExamplesBinding
import com.example.datasaverexampleapp.type_alias.Layout

class CameraExamplesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_examples)
        title = "Camera Example"

        DataBindingUtil.setContentView<ActivityCameraExamplesBinding>(
            this, Layout.activity_camera_examples
        ).apply {

            cameraIntentButton.setOnClickListener {
                val intent = Intent(this@CameraExamplesActivity, ImageCaptureIntentExampleActivity::class.java)
                startActivity(intent)
            }

            controllingCameraExampleButton.setOnClickListener {
                val intent = Intent(this@CameraExamplesActivity, ControllingCameraExampleActivity::class.java)
                startActivity(intent)
            }

            videoRecordingExampleButton?.setOnClickListener {
                val intent = Intent(this@CameraExamplesActivity, VideoRecordingExampleActivity::class.java)
                startActivity(intent)
            }

            videoRecordingWithMediaPlayerExampleButton.setOnClickListener {
                val intent = Intent(this@CameraExamplesActivity, VideoRecordingExampleActivity::class.java)
                startActivity(intent)
            }
        }
    }
}