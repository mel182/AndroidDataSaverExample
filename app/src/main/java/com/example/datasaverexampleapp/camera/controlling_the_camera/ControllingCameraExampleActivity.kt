@file:Suppress("DEPRECATION")

package com.example.datasaverexampleapp.camera.controlling_the_camera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.base_classes.BaseActivity
import com.example.datasaverexampleapp.camera.picture_camera2_activity.PictureCameraActivity
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.android.synthetic.main.activity_controlling_camera_example.*

/**
 * This is an example on how the device camera can be controlled programmatically by using the Camera API
 * To access the camera hardware directly, you need to add the CAMERA permission to your application
 * manifest:
 * <uses-permission android:name="android.permission.CAMERA"/>
 *
 * NOTE: For privacy reasons, the camera permission is considered a dangerous permission; as such
 *       it must be requested at run time on Android 6.0 (API Level 23) and higher devices.
 */
class ControllingCameraExampleActivity : BaseActivity(Layout.activity_controlling_camera_example) {
    private val cameraManager by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSystemService(Context.CAMERA_SERVICE) as CameraManager
        } else {
            null
        }
    }

    private var cameraInfo: Camera.CameraInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Controlling Camera example"

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // Camera detected!
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermission(Manifest.permission.CAMERA) { granted ->

                    if (granted) {
                        getCameraList()
                    }
                }
            } else {
                getCameraList()
            }
        } else {
            Toast.makeText(this, "No camera detect on this device", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCameraList() {
        val cameraInfoList = ArrayList<CameraInfoListItem>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            cameraManager?.cameraIdList?.let {

                for (cameraIdFound in it.indices) {

                    cameraManager?.getCameraCharacteristics(it[cameraIdFound])?.apply {

                        val scalerStreamConfigMap = get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        val controlAfAvailableModes = get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES) // Return the auto-focus modes that are available.
                        val orientation = get(CameraCharacteristics.SENSOR_ORIENTATION) // Returns the orientation of the sensor tha the output image needs to
                                                                                        // be rotate to be upright on the device screen in its screen in its native orientation.
                        val cameraFacing = when (get(CameraCharacteristics.LENS_FACING)) { // Direction the camera faces relative to device screen. Possible values : FRONT (0), BACK (1), EXTERNAL(2)
                            CameraCharacteristics.LENS_FACING_BACK -> Facing.BACK
                            CameraCharacteristics.LENS_FACING_FRONT -> Facing.FRONT
                            else -> Facing.EXTERNAL
                        }

                        val item = CameraInfoListItem(
                            id = cameraIdFound,
                            orientation = orientation ?: -1,
                            facing = cameraFacing,
                            controlAutoFocusModes = controlAfAvailableModes ?: IntArray(0), // Returns a 'StreamConfigurationMap', which stores the output formats
                                                                                                 // and sizes supported by this camera that you use to set the appropriate
                                                                                                 // preview size and image capture size.
                            scalerStreamMap = scalerStreamConfigMap
                        )

                        if (!cameraInfoList.contains(item))
                            cameraInfoList.add(item)
                    }
                }
            }
        } else {

            cameraInfo = Camera.CameraInfo()

            for (cameraIDFound in 0 until Camera.getNumberOfCameras())
            {
                Camera.getCameraInfo(cameraIDFound, cameraInfo)

                cameraInfo?.let {

                    val facing = if (it.facing == Camera.CameraInfo.CAMERA_FACING_BACK) Facing.BACK else Facing.FRONT //The direction that the camera faces. It should be CAMERA_FACING_BACK or CAMERA_FACING_FRONT.

                    val item = CameraInfoListItem(
                        id = cameraIDFound,
                        orientation = it.orientation, // The orientation of the camera image. The value is the angle that the camera image needs to be rotated clockwise so it shows correctly on the display in its
                                                      // natural orientation. It should be 0, 90, 180, or 270.
                        facing = facing,
                        canDisableShutterSound = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) it.canDisableShutterSound else false, // Whether the shutter sound can be disabled. On some devices,
                                                                                                                                                        // the camera shutter sound cannot be turned off through enableShutterSound.
                                                                                                                                                        // This field can be used to determine whether a call to disable the shutter sound will succeed.
                        controlAutoFocusModes = IntArray(0),
                        scalerStreamMap = null
                    )

                    if (!cameraInfoList.contains(item))
                        cameraInfoList.add(item)
                }
            }
        }

        camera_list?.apply {
            adapter = CameraListAdapter(cameraInfoList){
                CameraViewActivity.cameraId = it.id
                CameraViewActivity.cameraManager = cameraManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val intent = Intent(this@ControllingCameraExampleActivity, PictureCameraActivity::class.java)
                    PictureCameraActivity.cameraId = it.id
                    PictureCameraActivity.orientation = it.orientation
                    startActivity(intent)
                } else {
                    Toast.makeText(this@ControllingCameraExampleActivity,"Unsupported os!",Toast.LENGTH_SHORT).show()
                }
            }

            layoutManager = LinearLayoutManager(this@ControllingCameraExampleActivity).apply {
                val dividerItemDecoration =
                    DividerItemDecoration(context, orientation)
                addItemDecoration(dividerItemDecoration)
            }
        }
    }
}