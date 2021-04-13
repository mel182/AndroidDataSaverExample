package com.example.datasaverexampleapp.camera.controlling_the_camera

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Rect
import android.hardware.Camera
import android.hardware.camera2.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.datasaverexampleapp.base_classes.BaseActivity
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.android.synthetic.main.activity_camera_view.*
import kotlinx.android.synthetic.main.activity_media_player_video_playback.*
import java.lang.IllegalStateException
import java.util.*

class CameraViewActivity : BaseActivity(Layout.activity_camera_view), SurfaceHolder.Callback {

    companion object {
        var cameraId: Int? = null
        var cameraManager: CameraManager? = null
    }

    private var cameraDeviceCallback: CameraDevice.StateCallback? = null
    private var camera:Camera ? = null
    private var deviceCamera:CameraDevice ? = null
    private lateinit var surfaceHolder:SurfaceHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (cameraId == null)
            finish()

        title = "Camera view with id: ${cameraId}"
        openCamera()
    }

    @SuppressLint("MissingPermission")
    private fun openCamera() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            requestPermission(Manifest.permission.CAMERA) { granted ->

                if (granted) {

                    cameraId?.let { id ->

                        cameraDeviceCallback = object : CameraDevice.StateCallback() {
                            override fun onOpened(camera: CameraDevice) {
                                deviceCamera = camera
                                Log.i("TAG","onOpened")
                            }

                            override fun onDisconnected(camera: CameraDevice) {
                                camera.close()
                                deviceCamera = null
                                Log.i("TAG","onDisconnected")
                            }

                            override fun onError(camera: CameraDevice, p1: Int) {
                                // Something went wrong, notify the user
                                Toast.makeText(this@CameraViewActivity,"Failed opening camera",Toast.LENGTH_SHORT).show()
                                camera.close()
                                deviceCamera = null
                                Log.i("TAG","Error opening camera: ${p1}")
                            }
                        }

                        surfaceView?.let { view ->
                            surfaceHolder = view.holder.apply {
                                addCallback(this@CameraViewActivity)
//                                setFixedSize(400,400)
                            }
                        }

                        try {

                            cameraManager?.openCamera(id.toString(), cameraDeviceCallback!!,null)

                        }catch (e :Exception)
                        {
                            e.printStackTrace()
                        }
                    }
                }
            }

        } else {

            try {
                cameraId?.let { id ->
                    camera = Camera.open(id)
                }
            }catch (e :Exception)
            {
                e.printStackTrace()
            }
        }
    }

    private lateinit var captureSession:CameraCaptureSession
    private lateinit var previewCaptureRequest:CaptureRequest

    @SuppressLint("NewApi")
    private fun startCameraCaptureSession()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            Log.i("TAG","deviceCamera: ${deviceCamera}")
            Log.i("TAG","surfaceHolder.isCreating: ${surfaceHolder.isCreating}")

//            if (deviceCamera == null || surfaceHolder.isCreating)
//                return

            val previewSurface = surfaceHolder.surface

            // Create our preview CaptureRequest.Builder
            val mPreviewCaptureRequest = deviceCamera?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            mPreviewCaptureRequest?.addTarget(previewSurface)

            val captureSessionCallback = object : CameraCaptureSession.StateCallback() {

                override fun onConfigured(p0: CameraCaptureSession) {
                    this@CameraViewActivity.captureSession = p0

                    try {

                        mPreviewCaptureRequest?.let {
                            this@CameraViewActivity.captureSession.setRepeatingRequest(it.build(), null,null)
                        }

                    }catch ( e: CameraAccessException){
                        e.printStackTrace()
                        Log.i("TAG","CameraAccessException: ${e.message}")
                    } catch (e : IllegalStateException)
                    {
                        e.printStackTrace()
                        Log.i("TAG","IllegalStateException: ${e.message}")
                    }

                }

                override fun onConfigureFailed(p0: CameraCaptureSession) {
                    Log.i("TAG","onConfigureFailed")
                }
            }

            try {

                deviceCamera?.createCaptureSession(Arrays.asList(previewSurface),captureSessionCallback,null)

            }catch (e : CameraAccessException)
            {
                e.printStackTrace()
                Log.i("TAG","CameraAccessException: ${e.message}")
            }

//            mPreviewCaptureRequest?.addTarget()

        }


    }


    override fun onDestroy() {
        super.onDestroy()
        cameraId = null
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            deviceCamera?.close()
        } else {
            camera?.release()
            camera = null
        }
        deviceCamera = null
        cameraManager = null
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        Log.i("TAG","surfaceCreated")
        startCameraCaptureSession()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        Log.i("TAG","surfaceChanged")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        Log.i("TAG","surfaceDestroyed")
    }
}