package com.example.datasaverexampleapp.camera.controlling_the_camera

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.hardware.Camera
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Size
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.datasaverexampleapp.base_classes.BaseActivity
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.android.synthetic.main.activity_camera_view.*
import kotlinx.android.synthetic.main.activity_media_player_video_playback.*
import kotlinx.android.synthetic.main.activity_mvv_m_coroutines.*
import java.lang.IllegalArgumentException
import java.util.*

/**
 * This is an camera preview activity example using the surface view which is more battery efficient.
 * Ones you have an open connection to a 'cameraDevice', you can request image data by creating a
 * 'CameraCaptureSession'.
 * Creating a session is an expensive operation, often taking hundreds of milliseconds as a the camera
 * hardware is powered on and configured to handle the 'list' of 'surface' objects that will receive
 * the camera output.
 *
 * Note: Prior to Android 7.0 (API Level 24), each 'surfaceView' was rendered in it own window,
 * separately from the rest of the UI. As a result, unlike view-derrived classes it could not
 * be moved, transformed, or animated. As an alternative for earlier platform versions, the 'TextureView'
 * class offer support for these operations, but it is less battery-efficient.
 *
 * To display a preview, the 'SurfaceHolder.Callback' must be implemented, that listens for the construction of a valid
 * Surface (and ideally set it size with 'setFixedSize'.
 */
@SuppressLint("NewApi")
class CameraViewActivity : BaseActivity(Layout.activity_camera_view), SurfaceHolder.Callback, ImageReader.OnImageAvailableListener {

    companion object {
        var cameraId: Int? = null
        var cameraManager: CameraManager? = null
    }

    private var cameraDeviceCallback: CameraDevice.StateCallback? = null
    private var camera: Camera? = null
    private var deviceCamera: CameraDevice? = null
    private lateinit var surfaceHolder: SurfaceHolder
    private var imageReader: ImageReader? = null
    private var onImageAvailabilityListener: ImageReader.OnImageAvailableListener? = null

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
                                Toast.makeText(
                                    this@CameraViewActivity,
                                    "Failed opening camera",
                                    Toast.LENGTH_SHORT
                                ).show()
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

                        createAndSetImageReader()

                        try {
                            cameraManager?.openCamera(id.toString(), cameraDeviceCallback!!, null)

                            take_picture_button?.setOnClickListener {
                                takePicture()
                            }

                        } catch (e: Exception) {
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
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun createAndSetImageReader()
    {
        //TODO: Continue with implementation of image reader by retrieving the surface view height and width
        val viewTreeObserver = surfaceView.viewTreeObserver
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalFocusChangeListener, ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalFocusChanged(p0: View?, p1: View?) {}

            override fun onGlobalLayout() {

                surfaceView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                Log.i("TAG", "Measure width: ${surfaceView.measuredWidth}")
                Log.i("TAG", "Measure height: ${surfaceView.measuredHeight}")

                imageReader = ImageReader.newInstance(surfaceView.measuredWidth,surfaceView.measuredHeight,ImageFormat.JPEG,2).apply {
                    setOnImageAvailableListener({ imageReader ->

                        imageReader.acquireNextImage().use { image ->

                            for (planeFound in image.planes) {

                                val buffer = planeFound.buffer
                                val data = ByteArray(buffer.remaining())
                                buffer.get(data)
                                Log.i("TAG","Buffer data: ${data}")
                            }
                        }
                    },null) // optional handler
                }
            }
        })
    }

    private fun takePicture() {
        //TODO: Continue implementing the take picture function
        Toast.makeText(this, "Take picture", Toast.LENGTH_SHORT).show()

        imageReader?.let { reader ->
            try {

                val takePictureBuilder = deviceCamera?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                takePictureBuilder?.addTarget(reader.surface)

                takePictureBuilder?.let { captureBuilder ->
                    captureSession.capture(captureBuilder.build(),null,null)
                }

            }catch (e:CameraAccessException)
            {
                Log.i("TAG","Error capture the photo: ${e.reason}")
            } catch (e:IllegalArgumentException)
            {
                Log.i("TAG","Illegal argument exception: ${e.message}")
            }
        }
    }

    //region Set optimal preview size
    fun setOptimalPreviewSize(id: String)
    {
        Log.i("TAG", "Optimal size")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            val characteristics =  cameraManager?.getCameraCharacteristics(id)
            val supportedSizes = characteristics?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)?.getOutputSizes(
                ImageFormat.JPEG
            )

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

                    //region Image reader
                    ImageReader.newInstance(previewWidth,previewHeight,ImageFormat.JPEG,2).apply {
                        setOnImageAvailableListener(this@CameraViewActivity,null)
                    }
                    //endregion

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
                    layoutParams.height =
                        if (orientation == 90 || orientation == 270) smallestOne.width else smallestOne.height
                    layoutParams.width =
                        if (orientation == 90 || orientation == 270) smallestOne.height else smallestOne.width

                    surfaceView.layoutParams = layoutParams
                }
            })

        } else {
            Log.i("TAG", "Lollipop and lower!")
        }
    }

    private class ComparableByArea : Comparator<Size>
    {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun compare(p0: Size, p1: Size): Int {
            return (p0.height * p0.width) - (p1.height * p1.width)
        }
    }
    //endregion

    private lateinit var captureSession:CameraCaptureSession
    private lateinit var previewCaptureRequest:CaptureRequest

    @SuppressLint("NewApi")
    private fun startCameraCaptureSession()
    {
        // Once the session is configure, you can proceed to display data by passing in a 'CaptureRequest'
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Log.i("TAG", "deviceCamera: ${deviceCamera}")
            Log.i("TAG", "surfaceHolder.isCreating: ${surfaceHolder.isCreating}")

            val previewSurface = surfaceHolder.surface

            // Create our preview CaptureRequest.Builder
            val mPreviewCaptureRequest = deviceCamera?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            mPreviewCaptureRequest?.apply {
                addTarget(previewSurface)


                cameraId?.let {

                    val characteristics =  cameraManager?.getCameraCharacteristics(it.toString())

                    // Check if auto-mode is supported
                    characteristics?.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES)?.let { availableModes ->

                        if (availableModes.isNotEmpty())
                        {
                            // The CaptureRequest.Builder provide a set of common default, this is also where you
                            // can set the auto-focus ()template provide

                            // IMPORTANT: In case the auto-focus is set after setting 'setRepeatingRequest' you must call the
                            // 'setRepeatingRequest' again.

                            //Auto-focus set!
                            set(
                                CaptureRequest.CONTROL_AF_MODE,
                                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                            )
                        } else {
                            // Does not support auto-focus
                        }

                    }?: kotlin.run {
                        // Does not support auto-focus
                    }
                }
            }

            val captureSessionCallback = object : CameraCaptureSession.StateCallback() {

                override fun onConfigured(p0: CameraCaptureSession) {
                    this@CameraViewActivity.captureSession = p0

                    try {

                        // the 'setRepeatingRequest' indicates that you'd like to repeatedly capture new frames.
                        mPreviewCaptureRequest?.let {
                            this@CameraViewActivity.captureSession.setRepeatingRequest(
                                it.build(),
                                null,
                                null
                            )
                        }

                    }catch (e: CameraAccessException){
                        e.printStackTrace()
                        Log.i("TAG", "CameraAccessException: ${e.message}")
                    } catch (e: IllegalStateException)
                    {
                        e.printStackTrace()
                        Log.i("TAG", "IllegalStateException: ${e.message}")
                    }
                }

                override fun onConfigureFailed(p0: CameraCaptureSession) {
                    Log.i("TAG", "onConfigureFailed")
                }
            }

            try {

                deviceCamera?.createCaptureSession(
                    mutableListOf(previewSurface),
                    captureSessionCallback, null
                )

            }catch (e: CameraAccessException)
            {
                e.printStackTrace()
                Log.i("TAG", "CameraAccessException: ${e.message}")
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

    override fun onImageAvailable(reader: ImageReader?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            reader?.acquireNextImage().use { image ->

                image?.let {

                    val imagePlanesList = it.planes
                    if (imagePlanesList.isNotEmpty())
                    {
                        val buffer = imagePlanesList[0].buffer
                        val data = ByteArray(buffer.remaining())
                        buffer[data]
                        // saveImage
                    }


                }


            }
        }


        /*
        fun omImageAvailable(reader: ImageReader) {
        reader.acquireNextImage().use { image -> }
    }
        */

    }
}