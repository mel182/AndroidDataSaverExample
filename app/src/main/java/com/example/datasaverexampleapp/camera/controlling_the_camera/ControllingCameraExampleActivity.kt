package com.example.datasaverexampleapp.camera.controlling_the_camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.datasaverexampleapp.base_classes.BaseActivity
import com.example.datasaverexampleapp.type_alias.Layout

/**
 * This is an example on how the device camera can be controlled programmatically by using the Camera API
 * To access the camera hardware directly, you need to add the CAMERA permission to your application
 * manifest:
 * <uses-permission android:name="android.permission.CAMERA"/>
 *
 * NOTE: For privacy reasons, the camera permission is considered a dangerous permission; as such
 *       it must be requested at run time on Android 6.0 (API Level 23) and higher devices.
 */
class ControllingCameraExampleActivity : BaseActivity(Layout.activity_controlling_camera_example)
{
    private val cameraManager by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSystemService(Context.CAMERA_SERVICE) as CameraManager
        } else {
            null
        }
    }

    private var cameraInfo:Camera.CameraInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Controlling Camera example"

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermission(Manifest.permission.CAMERA) { granted ->

                    if (granted)
                    {
                        getCameraList()
                    }
                }
            } else {
                getCameraList()
            }
        } else {
            Toast.makeText(this,"No camera detect on this device",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCameraList()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            cameraManager?.cameraIdList?.let {

                for (cameraIdFound in it.indices) {

                    cameraManager?.getCameraCharacteristics(it[cameraIdFound])?.apply {

                        val facing = get(CameraCharacteristics.LENS_FACING)
                        val scalerStreamConfigMap = get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        val controlAfAvailableModes = get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES)
                        val orientation = get(CameraCharacteristics.SENSOR_ORIENTATION)

                        Log.i("TAG","facing: ${facing}")
                        Log.i("TAG","scalerStreamConfigMap: ${scalerStreamConfigMap}")
                        Log.i("TAG","controlAfAvailableModes: ${controlAfAvailableModes?.size}\n")

                        controlAfAvailableModes?.let {

                            if ( it.contains(CameraCharacteristics.CONTROL_AF_MODE_OFF))
                            {
                                Log.i("TAG","control AF mode off: available")
                            } else {
                                Log.i("TAG","control AF mode off: -")
                            }

                            if (it.contains(CameraCharacteristics.CONTROL_AF_MODE_AUTO))
                            {
                                Log.i("TAG","Control af mode auto : available")
                            } else {
                                Log.i("TAG","Control af mode auto : -")
                            }

                            if (it.contains(CameraCharacteristics.CONTROL_AF_MODE_MACRO))
                            {
                                Log.i("TAG","control AF macro : available")
                            } else {
                                Log.i("TAG","control AF macro : -")
                            }

                            if (it.contains(CameraCharacteristics.CONTROL_AF_MODE_CONTINUOUS_VIDEO))
                            {
                                Log.i("TAG","control AF continuous video : available")
                            } else {
                                Log.i("TAG","control AF continuous video : -")
                            }

                            if (it.contains(CameraCharacteristics.CONTROL_AF_MODE_CONTINUOUS_PICTURE))
                            {
                                Log.i("TAG","control AF continuous picture : available")
                            } else {
                                Log.i("TAG","control AF continuous picture : -")
                            }

                            if (it.contains(CameraCharacteristics.CONTROL_AF_MODE_EDOF))
                            {
                                Log.i("TAG","Control af mode EDOF : available")
                            } else {
                                Log.i("TAG","Control af mode EDOF : -")
                            }
                        }

                        Log.i("TAG","orientation: ${orientation}")
                    }
                }
            }


        } else {

            cameraInfo = Camera.CameraInfo()

//            camera_count_tv?.text = Camera.getNumberOfCameras().toString()

            Log.i("TAG","Numbers of camera: ${Camera.getNumberOfCameras()}")

            for ( i in 0 until Camera.getNumberOfCameras())
            {
                Log.i("TAG","Camera num: $i")

                //The direction that the camera faces. It should be CAMERA_FACING_BACK or CAMERA_FACING_FRONT.
                Camera.getCameraInfo(i,cameraInfo)
                Log.i("TAG","|--------- Camera info --------|")
                Log.i("TAG","Camera facing: ${cameraInfo?.facing}")
                /*
                The orientation of the camera image. The value is the angle that the camera image needs to be rotated clockwise so it shows correctly on the display in its natural orientation.
                It should be 0, 90, 180, or 270.
                */
                Log.i("TAG","Camera orientation: ${cameraInfo?.orientation}")

                Log.i("TAG","|--------- Camera info --------|")
            }
        }
        /*
CameraManager myCamera = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
final int numCameras = Camera.getNumberOfCameras();
Toast.makeText(StartPage.this, numCameras + " cameras", Toast.LENGTH_SHORT).show();
for(int i = 0; i <= numCameras; i++){
    Log.d("cameraNum", "Camera "+i)
    CameraInfo cameraInfo = new CameraInfo();
    Camera.getCameraInfo(i, cameraInfo);
}
        */
    }
}