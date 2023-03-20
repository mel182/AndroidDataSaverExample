@file:Suppress("UNNECESSARY_SAFE_CALL")

package com.example.datasaverexampleapp.camera.picture_camera2_activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils.replace
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.base_classes.BaseActivity
import com.example.datasaverexampleapp.camera.picture_camera_fragment.PictureCameraFragment
import com.example.datasaverexampleapp.databinding.ActivityPictureCameraBinding
import com.example.datasaverexampleapp.type_alias.Layout

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

        DataBindingUtil.setContentView<ActivityPictureCameraBinding>(
            this, Layout.activity_picture_camera
        ).apply {

            if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                // Camera detected!
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermission(Manifest.permission.CAMERA) { granted ->

                        if (granted)
                        {
                            container?.let { containerId ->
                                supportFragmentManager?.beginTransaction()
                                    ?.replace(containerId.id, PictureCameraFragment(cameraId.toString()))
                                    ?.commit()
                            }
                        } else {
                            Toast.makeText(this@PictureCameraActivity, "Permission denied!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
            } else {
                Toast.makeText(this@PictureCameraActivity, "No camera detect on this device", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        cameraId = null
        orientation = null
        super.onDestroy()
    }
}