package com.example.datasaverexampleapp.camera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.camera.controlling_the_camera.ControllingCameraExampleActivity
import com.example.datasaverexampleapp.camera.image_capture_intent.ImageCaptureIntentExampleActivity
import com.example.datasaverexampleapp.camera.picture_camera2_activity.PictureCameraActivity
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

        controlling_camera2_example_button?.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val intent = Intent(this, PictureCameraActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this,"Unsupported os!",Toast.LENGTH_SHORT).show()
            }
        }

    }
}