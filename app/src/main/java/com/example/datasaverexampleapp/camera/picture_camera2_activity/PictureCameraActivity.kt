@file:Suppress("UNNECESSARY_SAFE_CALL")

package com.example.datasaverexampleapp.camera.picture_camera2_activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import com.example.datasaverexampleapp.base_classes.BaseActivity
import com.example.datasaverexampleapp.camera.picture_camera_fragment.PictureCameraFragment
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.android.synthetic.main.activity_picture_camera.*

class PictureCameraActivity : BaseActivity(Layout.activity_picture_camera)
{
    companion object {
        var cameraId: Int? = null
        var orientation: Int? = null
    }

    @SuppressLint("UnsupportedChromeOsCameraSystemFeature")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (cameraId == null || orientation == null)
        {
            finish()
            return
        }


        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // Camera detected!
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermission(Manifest.permission.CAMERA) { granted ->

                    if (granted)
                    {
                        container?.let { containerId ->
                            supportFragmentManager?.beginTransaction()
                                .replace(containerId.id, PictureCameraFragment(cameraId.toString()))
                                .commit()
                        }
                    } else {
                        Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        } else {
            Toast.makeText(this, "No camera detect on this device", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        cameraId = null
        orientation = null
        super.onDestroy()
    }
}