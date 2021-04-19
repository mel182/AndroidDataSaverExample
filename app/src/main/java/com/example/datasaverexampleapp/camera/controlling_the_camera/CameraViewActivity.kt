package com.example.datasaverexampleapp.camera.controlling_the_camera

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.ImageFormat
import android.graphics.Rect
import android.hardware.Camera
import android.hardware.camera2.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintSet
import com.example.datasaverexampleapp.base_classes.BaseActivity
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.android.synthetic.main.activity_camera_view.*
import kotlinx.android.synthetic.main.activity_media_player_video_playback.*
import kotlinx.android.synthetic.main.activity_mvv_m_coroutines.*
import java.lang.IllegalStateException
import java.util.*
import kotlin.Comparator

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            requestPermission(Manifest.permission.CAMERA) { granted ->

                if (granted) {

                    cameraId?.let { id ->

                        cameraDeviceCallback = object : CameraDevice.StateCallback() {
                            override fun onOpened(camera: CameraDevice) {
                                deviceCamera = camera
                                //The method called when a camera device has finished opening.
                            }

                            override fun onDisconnected(camera: CameraDevice) {
                                camera.close()
                                deviceCamera = null
                                //The method called when a camera device is no longer available for use.
                            }

                            override fun onError(camera: CameraDevice, p1: Int) {
                                // Something went wrong, notify the user
                                // The method called when a camera device has encountered a serious error.
                                Toast.makeText(this@CameraViewActivity,"Failed opening camera",Toast.LENGTH_SHORT).show()
                                camera.close()
                                deviceCamera = null
                            }

                            override fun onClosed(camera: CameraDevice) {
                                // The method called when a camera device has been closed with 'CameraDevice.close()'.
                            }
                        }

                        surfaceView?.let { view ->
                            surfaceHolder = view.holder.apply {
                                addCallback(this@CameraViewActivity)
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


    fun setOptimalPreviewSize(id:String)
    {
        Log.i("TAG","Optimal size")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            val characteristics =  cameraManager?.getCameraCharacteristics(id)
            val supportedSizes = characteristics?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)?.getOutputSizes(ImageFormat.JPEG)

            val viewTreeObserver = surfaceView.viewTreeObserver
            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalFocusChangeListener,
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalFocusChanged(p0: View?, p1: View?) {
                }

                override fun onGlobalLayout() {
                    surfaceView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    Log.i("TAG", "Measure width: ${surfaceView.measuredWidth}")
                    Log.i("TAG", "Measure height: ${surfaceView.measuredHeight}")

                    val previewWidth = surfaceView.measuredWidth
                    val previewHeight = surfaceView.measuredHeight

                    val bigEnough = ArrayList<Size>()

                    supportedSizes?.forEach {
                        Log.i("TAG", "Camera size found: ${it}")

                        for (option in supportedSizes) {
                            if (option.width <= previewWidth && option.height <= previewHeight) {
                                bigEnough.add(option)
                            }
                        }
                    }


                    val smallestOne = Collections.max(bigEnough, ComparableByArea())

                    val orientation = characteristics?.get(CameraCharacteristics.SENSOR_ORIENTATION)
                    val layoutParams = surfaceView.layoutParams
                    layoutParams.height =  if (orientation == 90 || orientation == 270 ) smallestOne.width else smallestOne.height
                    layoutParams.width = if (orientation == 90 || orientation == 270) smallestOne.height else smallestOne.width

                    surfaceView.layoutParams = layoutParams
                }
            })

        } else {
            Log.i("TAG","Lollipop and lower!")
        }
    }

    private class ComparableByArea : Comparator<Size>
    {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun compare(p0: Size, p1: Size): Int {
            return (p0.height * p0.width) - (p1.height * p1.width)
        }
    }

    private lateinit var captureSession:CameraCaptureSession
    private lateinit var previewCaptureRequest:CaptureRequest

    @SuppressLint("NewApi")
    private fun startCameraCaptureSession()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Log.i("TAG","deviceCamera: ${deviceCamera}")
            Log.i("TAG","surfaceHolder.isCreating: ${surfaceHolder.isCreating}")

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            deviceCamera?.close()
        } else {
            camera?.release()
            camera = null
        }
        deviceCamera = null
        cameraManager = null
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        // This is called immediately after the surface is first created.
        startCameraCaptureSession()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        //This is called immediately after any structural changes (format or size) have been made to the surface.
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        //This is called immediately before a surface is being destroyed.
    }
}