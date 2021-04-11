package com.example.datasaverexampleapp.camera

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.camera.controlling_the_camera.ControllingCameraExampleActivity
import com.example.datasaverexampleapp.camera.image_capture_intent.ImageCaptureIntentExampleActivity
import kotlinx.android.synthetic.main.activity_camera_examples.*

class CameraExamplesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_examples)
        title = "Camera Example"

        camera_intent_button?.setOnClickListener {
            val intent = Intent(this, ImageCaptureIntentExampleActivity::class.java)
            startActivity(intent)
        }

        controlling_camera_example_button?.setOnClickListener {
            val intent = Intent(this, ControllingCameraExampleActivity::class.java)
            startActivity(intent)
        }

    }
}