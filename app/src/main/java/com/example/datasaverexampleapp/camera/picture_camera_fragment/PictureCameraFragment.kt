@file:Suppress("DEPRECATION")

package com.example.datasaverexampleapp.camera.picture_camera_fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.*
import android.hardware.camera2.*
import android.media.ImageReader
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.datasaverexampleapp.base_classes.BaseFragment
import com.example.datasaverexampleapp.camera.controlling_the_camera.*
import com.example.datasaverexampleapp.databinding.FragmentPictureCameraBinding
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 * Use the [PictureCameraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("UNNECESSARY_SAFE_CALL")
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PictureCameraFragment(private val cameraId: String) : BaseFragment(Layout.fragment_picture_camera), TextureView.SurfaceTextureListener, ImageReader.OnImageAvailableListener
{
    private val orientations = hashMapOf(
        ROTATION_0_INDEX to 90,
        ROTATION_90_INDEX to 0,
        ROTATION_180_INDEX to 270,
        ROTATION_270_INDEX to 180
    )
    private val maxPreviewWidth = 1920
    private val maxPreviewHeight = 1080
    private var captureSession:CameraCaptureSession? = null
    private var cameraDevice:CameraDevice? = null
    private var previewSize:Size? = null
    private var imageReader:ImageReader? = null // image reader that handles still image capture
    private var file: File? = null // This is the output the image capture
    private var previewRequestBuilder:CaptureRequest.Builder? = null // Capture request builder for the camera preview
    private var previewRequest:CaptureRequest? = null // Capture generated by the 'previewRequestBuilder'
    private var state : Int = STATE_PREVIEW // The current state of camera state for taking pictures.
    private val cameraOpenCloseLock : Semaphore = Semaphore(1) // to prevent the app from exiting before closing the camera

    private var flashSupported : Boolean = false
    private var sensorOrientation : Int = 0

    private val cameraStateCallback = object : CameraDevice.StateCallback() {

        // This method is called when the camera is opened.  We start camera preview here.
        override fun onOpened(p0: CameraDevice) {
            cameraOpenCloseLock.release()
            cameraDevice = p0
            createCameraPreviewSession()
        }

        // The method called when a camera device is no longer available for use.
        override fun onDisconnected(p0: CameraDevice) {
            cameraOpenCloseLock.release();
            cameraDevice?.close();
            cameraDevice = null;
        }

        // The method called when a camera device has encountered a serious error.
        override fun onError(p0: CameraDevice, p1: Int) {
            cameraOpenCloseLock.release();
            cameraDevice?.close();
            cameraDevice = null;
            activity?.finish()
        }
    }

    private val captureCallback = object : CameraCaptureSession.CaptureCallback(){

        // This method is called when an image capture makes partial forward progress; some
        // (but not all) results from an image capture are available.
        override fun onCaptureProgressed(session: CameraCaptureSession, request: CaptureRequest, partialResult: CaptureResult)
        {
            process(partialResult)
        }

        // This method is called when an image capture has fully completed and all the result metadata is available.
        override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult)
        {
            process(result)
        }
    }

    private fun process(result: CaptureResult)
    {
        when(state)
        {
            STATE_PREVIEW -> {
                // We have nothing to do when the camera is working normally
            }

            STATE_WAITING_LOCK -> {

                result.get(CaptureResult.CONTROL_AF_STATE)?.let { autoFocusState ->

                    if (autoFocusState == CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED || autoFocusState == CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED) {
                        runPrecaptureSequence()
                    }

                } ?: kotlin.run {
                    captureStillPicture();
                }
            }

            STATE_WAITING_PRECAPTURE -> {
                val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
                if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE || aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED)
                    state = STATE_WAITING_NON_PRECAPTURE
            }

            STATE_WAITING_NON_PRECAPTURE -> {

                val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
                if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                    state = STATE_PICTURE_TAKEN
                    captureStillPicture()
                }
            }
        }
    }

    private fun showToast(message: String)
    {
        activity?.let {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(it, message, Toast.LENGTH_LONG).show()
            }
        }
    }


    companion object {

        private fun chooseOptimalSize(
            choices: Array<Size>,
            textureViewWidth: Int,
            textViewHeight: Int,
            maxWidth: Int,
            maxHeight: Int,
            aspectRatio: Size
        ) : Size
        {
            // Collect the supported resolutions that are at least as big as preview Surface
            val bigEnough:ArrayList<Size> = ArrayList()

            // Collect the supported resolutions that are smaller that the preview Surface
            val notBigEnough:ArrayList<Size> = ArrayList()

            val width = aspectRatio.width
            val height = aspectRatio.height

            for (option in choices) {

                if (option.width <= maxWidth && option.height <= maxHeight && option.height == (option.width * height / width))
                {
                    if (option.width >= textureViewWidth && option.height >= textViewHeight)
                    {
                        bigEnough.add(option)
                    } else {
                        notBigEnough.add(option)
                    }
                }
            }

            // Pick the smallest of those big enough. If there is no one big enough, pick the largest of those not big enough
            return when {
                bigEnough.isNotEmpty() -> {
                    Collections.min(bigEnough, CompareSizesByArea())
                }
                notBigEnough.isNotEmpty() -> {
                    Collections.max(notBigEnough, CompareSizesByArea())
                }
                else -> {
                    choices[0]
                }
            }
        }

        class CompareSizesByArea : Comparator<Size>
        {
            override fun compare(size1: Size?, size2: Size?): Int {

                // We cast here to ensure the multiplications won't overflow
                return if (size1 != null && size2 != null) {
                    java.lang.Long.signum(
                        (size1.width * size1.height).toLong() -
                                (size2.width * size2.height).toLong()
                    )
                } else{
                    0
                }
            }
        }
    }

    private var captureBitmap : Bitmap? = null
    private var binding: FragmentPictureCameraBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {

            binding = DataBindingUtil.setContentView<FragmentPictureCameraBinding>(
                it, Layout.fragment_picture_camera
            ).apply {

                acceptButton.setOnClickListener {
                    saveImage()
                    resetCaptureState()
                }

                closeButton.setOnClickListener {
                    resetCaptureState()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        binding?.apply {

            autoFitView.apply {

                if (isAvailable)
                {
                    openCamera(width, height)
                } else {
                    surfaceTextureListener = this@PictureCameraFragment
                }
            }
        }
    }

    override fun onPause() {
        closeCamera()
        super.onPause()
    }

    @SuppressLint("MissingPermission")
    private fun openCamera(width: Int, height: Int)
    {
        requestPermission(Manifest.permission.CAMERA) { granted ->

            if (granted) {

                setCameraOutput(width, height)
                configureTransform(width, height)

                activity?.let {

                    try {

                        val cameraManager = it.getSystemService(Context.CAMERA_SERVICE) as CameraManager

                        if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS))
                        {
                            Toast.makeText(
                                activity,
                                "Time out waiting to lock camera opening.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@let
                        }

                        cameraManager.openCamera(cameraId, cameraStateCallback, null)

                        binding?.takePictureButton?.setOnClickListener {
                            Toast.makeText(activity, "Processing.... please wait!", Toast.LENGTH_SHORT).show()
                            takePicture()
                        }

                    }catch (e: CameraAccessException)
                    {
                        e.printStackTrace()
                    }catch (e: InterruptedException)
                    {
                        Log.i(
                            "TAG",
                            "Interrupted while trying to lock camera opening, reason: ${e.message}"
                        )
                    }
                }
            }
        }
    }

    private fun saveImage()
    {
        captureBitmap?.let { bitmap ->

            // Save to picture directory
            val path  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val filename = "${System.currentTimeMillis()}.JPG" // File name: 'current_milli'.JPG
            val file = File(path,filename)

            try {
                val fileOutputStream = FileOutputStream(file)
                fileOutputStream?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }

                activity?.apply {
                    sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE))
                    Uri.parse("file://${filename}")
                }

                showToast("Image saved in Picture folder")
            } catch (e :IOException)
            {
                Log.i("TAG","Failed creating new file, reason: ${e.message}")
            }
        }
    }

    private fun resetCaptureState()
    {
        binding?.apply {
            previewContainer.visibility = View.GONE
            samplePreview.setImageDrawable(null)
        }
        captureBitmap = null
    }

    private fun takePicture()
    {
        lockFocus()
    }

    private fun closeCamera()
    {
        try {

            cameraOpenCloseLock.acquire()
            captureSession?.let {
                it.close()
            }
            captureSession = null

            cameraDevice?.let {
                it.close()
            }
            cameraDevice = null

            imageReader?.let {
                it.close()
            }
            imageReader = null

        } catch (e: InterruptedException)
        {
            Log.i("TAG", "Interrupted while trying to lock camera closing, reason: ${e.message}")
        } finally {
            cameraOpenCloseLock.release()
        }
    }

    private fun setCameraOutput(width: Int, height: Int)
    {
        activity?.let {

            val cameraManager = it.getSystemService(Context.CAMERA_SERVICE) as CameraManager

            try {

                val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
                val configurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

                configurationMap?.let {

                    val outputSizeList : ArrayList<Size> = ArrayList()
                    for (outputSizeFound in configurationMap.getOutputSizes(ImageFormat.JPEG)) {
                        outputSizeList.add(outputSizeFound)
                    }

                    val largest = Collections.max(outputSizeList, CompareSizesByArea())
                    imageReader = ImageReader.newInstance(
                        largest.width,
                        largest.height,
                        ImageFormat.JPEG,
                        2
                    )
                    imageReader?.setOnImageAvailableListener(this, null)

                    // find out if we need to swap dimension to get the preview size relative to sensor coordinate

                    activity?.let {
                        val displayRotation = it.windowManager.defaultDisplay.rotation

                        sensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)?:0
                        var swappedDimensions = false

                        when(displayRotation)
                        {
                            ROTATION_0_INDEX, ROTATION_180_INDEX -> {
                                if (sensorOrientation == 90 || sensorOrientation == 270) {
                                    swappedDimensions = true
                                }
                            }

                            ROTATION_90_INDEX, ROTATION_270_INDEX -> {
                                if (sensorOrientation == 0 || sensorOrientation == 180) {
                                    swappedDimensions = true
                                }
                            }
                        }

                        val displaySize = Point()
                        activity?.windowManager?.defaultDisplay?.getSize(displaySize)

                        var rotatedPreviewWidth = width
                        var rotatedPreviewHeight = height
                        var maxPreviewWidth = displaySize.x
                        var maxPreviewHeight = displaySize.y

                        if (swappedDimensions)
                        {
                            rotatedPreviewWidth = height
                            rotatedPreviewHeight = width
                            maxPreviewWidth = displaySize.y
                            maxPreviewHeight = displaySize.x
                        }

                        if (maxPreviewWidth > this.maxPreviewWidth)
                            maxPreviewWidth = this.maxPreviewWidth

                        if (maxPreviewHeight > this.maxPreviewHeight)
                            maxPreviewHeight = this.maxPreviewHeight

                        // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
                        // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
                        // garbage capture data.
                        chooseOptimalSize(
                            configurationMap.getOutputSizes(SurfaceTexture::class.java),
                            rotatedPreviewWidth,
                            rotatedPreviewHeight,
                            maxPreviewWidth,
                            maxPreviewHeight,
                            largest
                        ).apply {

                            previewSize = this

                            // We fit the aspect ratio of TextureView to the size of preview we picked.
                            resources.configuration.orientation.let { orientation ->

                                binding?.apply {
                                    if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                                    {
                                        autoFitView.setAspectRatio(width, height)
                                    } else {
                                        autoFitView.setAspectRatio(height, width)
                                    }
                                }

                                // Check if the flash is supported
                                val available = cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
                                flashSupported = available != null
                            }
                        }
                    }
                }
            }catch (e: CameraAccessException)
            {
                e.printStackTrace()
            } catch (e: NullPointerException)
            {
                e.printStackTrace()
            }
        }
    }

    private fun configureTransform(viewWidth: Int, viewHeight: Int)
    {
        if ( activity != null && previewSize != null)
        {
            val rotation = activity?.windowManager?.defaultDisplay?.rotation
            val matrix = Matrix()
            val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
            val bufferRect = RectF(
                0f,
                0f,
                previewSize!!.height.toFloat(),
                previewSize!!.width.toFloat()
            )

            val centerX = viewRect.centerX()
            val centerY = viewRect.centerY()

            when(rotation)
            {
                ROTATION_90_INDEX, ROTATION_270_INDEX -> {
                    bufferRect.offset(
                        centerX - bufferRect.centerX(),
                        centerY - bufferRect.centerY()
                    )
                    matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
                    val scale = Math.max(
                        (viewHeight / previewSize!!.height).toFloat(),
                        (viewWidth / previewSize!!.width).toFloat()
                    )
                    matrix.postScale(scale, scale, centerX, centerY)
                    matrix.postRotate((90 * (rotation - 2)).toFloat(), centerX, centerY)
                }

                ROTATION_180_INDEX -> {
                    matrix.postRotate(180.toFloat(), centerX, centerY)
                }
            }

            binding?.autoFitView?.setTransform(matrix)
        }
    }

    private fun createCameraPreviewSession()
    {
        try {

            binding?.autoFitView?.surfaceTexture?.let { texture ->

                // We configure the size of default buffer to be the size of camera preview we want.
                texture.setDefaultBufferSize(previewSize!!.width, previewSize!!.height)

                // This is the output Surface we need to start preview.
                val surface = Surface(texture)

                // We set up a CaptureRequest.Builder with the output Surface.
                previewRequestBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                previewRequestBuilder?.addTarget(surface)

                cameraDevice?.let {

                    // Here, we create a CameraCaptureSession for camera preview.
                    it.createCaptureSession(
                        Arrays.asList(surface, imageReader?.surface),
                        object : CameraCaptureSession.StateCallback() {
                            override fun onConfigured(@NonNull cameraCaptureSession: CameraCaptureSession)
                            {
                                // When the session is ready, we start displaying the preview.
                                captureSession = cameraCaptureSession
                                try {
                                    // Auto focus should be continuous for camera preview.
                                    previewRequestBuilder?.set(
                                        CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                                    )
                                    // Flash is automatically enabled when necessary.
                                    setAutoFlash(previewRequestBuilder)

                                    // Finally, we start displaying the camera preview.
                                    previewRequest = previewRequestBuilder?.build()
                                    captureSession?.setRepeatingRequest(
                                        previewRequest!!,
                                        captureCallback, null
                                    )

                                } catch (e: CameraAccessException) {
                                    e.printStackTrace()
                                }
                            }

                            override fun onConfigureFailed(@NonNull cameraCaptureSession: CameraCaptureSession) {
                                showToast("Failed")
                            }
                        }, null
                    )
                }
            }
        } catch (e: CameraAccessException)
        {
            e.printStackTrace()
        }
    }

    private fun lockFocus()
    {
        try {
            // This is how to tell the camera to lock focus.
            previewRequestBuilder?.set(
                CaptureRequest.CONTROL_AF_TRIGGER,
                CameraMetadata.CONTROL_AF_TRIGGER_START
            )
            state = STATE_WAITING_LOCK
            previewRequestBuilder?.let {
                captureSession?.capture(it.build(), captureCallback, null)
            }
        } catch (e: CameraAccessException)
        {
            e.printStackTrace()
        }
    }

    private fun runPrecaptureSequence()
    {
        try {
            // This is how to tell the camera to trigger.
            previewRequestBuilder?.set(
                CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START
            )

            state = STATE_WAITING_PRECAPTURE

            previewRequestBuilder?.let {
                captureSession?.capture(it.build(), captureCallback, null)
            }
        } catch (e: CameraAccessException)
        {
            e.printStackTrace()
        }
    }

    private fun captureStillPicture()
    {
        try {

            if (activity == null || cameraDevice == null || imageReader == null)
                return

            // This is the CaptureRequest.Builder that we use to take a picture.
            val captureBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureBuilder.addTarget(imageReader!!.surface)

            // Use the same AE and AF modes as the preview.
            captureBuilder.set(
                CaptureRequest.CONTROL_AF_MODE,
                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
            )
            setAutoFlash(captureBuilder)

            // Orientation
            activity?.windowManager?.defaultDisplay?.rotation?.let { orientation ->
                captureBuilder.set(
                    CaptureRequest.JPEG_ORIENTATION,
                    getSensorOrientation(orientation)
                )
            }

            // A callback object for tracking the progress of a CaptureRequest submitted to the camera device.
            val callback = object: CameraCaptureSession.CaptureCallback() {

                // This method is called when an image capture has fully completed and all
                // the result metadata is available.
                override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
                    unlockFocus()
                }
            }

            captureSession?.apply {
                stopRepeating()
                abortCaptures()
                capture(captureBuilder.build(), callback, null)
            }

        } catch (e: CameraAccessException)
        {
            e.printStackTrace()
        }
    }

    // Get current sensor orientation
    private fun getSensorOrientation(rotation: Int) : Int
    {
        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
        // We have to take that into account and rotate JPEG properly.
        // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
        return (orientations[rotation]?:0 + sensorOrientation + 270) % 360
    }

    private fun unlockFocus() {
        try {

            // Reset the auto-focus trigger
            previewRequestBuilder?.set(
                CaptureRequest.CONTROL_AF_TRIGGER,
                CameraMetadata.CONTROL_AF_TRIGGER_CANCEL
            )
            setAutoFlash(previewRequestBuilder)
            previewRequestBuilder?.let {
                captureSession?.capture(it.build(), captureCallback, null)
            }
            state = STATE_PREVIEW
            previewRequest?.let {
                captureSession?.setRepeatingRequest(it, captureCallback, null)
            }
        } catch (e: CameraAccessException)
        {
            e.printStackTrace()
        }
    }

    private fun setAutoFlash(requestBuilder: CaptureRequest.Builder?)
    {
        if (flashSupported)
            requestBuilder?.set(
                CaptureRequest.CONTROL_AE_MODE,
                CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
            )
    }

    override fun onSurfaceTextureAvailable(p0: SurfaceTexture, width: Int, height: Int) {
         openCamera(width, height)
    }

    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, width: Int, height: Int) {
         configureTransform(width, height)
    }

    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
        return true
    }

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture) { }

    // On image reader listener
    // The onImageAvailable is called per image basis, that is, callback
    // fires for every new frame available from ImageReader
    override fun onImageAvailable(imageReader: ImageReader?) {

        imageReader?.let { reader ->

            // Acquire the latest Image from the ImageReader's queue, dropping
            // older images. Returns null if no new image is available.
            reader.acquireLatestImage().use {

                val planes = it.planes
                if (planes.isNotEmpty())
                {
                    val buffer = planes[0].buffer
                    val data = ByteArray(buffer.remaining())
                    buffer.get(data)

                    captureBitmap = BitmapFactory.decodeByteArray(data,0,data.size)

                    captureBitmap?.let { bitmap ->
                        captureBitmap = rotateImage(bitmap)
                        binding?.previewContainer?.visibility = View.VISIBLE
                        binding?.samplePreview?.setImageBitmap(captureBitmap!!)
                    }?: kotlin.run {
                        showToast("Failed processing image")
                    }
                }
            }
        }
    }


    // Rotate image to the corresponding screen orientation.
    // This is because on most device the image appear in landscape mode even if it was taken in portrait mode.
    private fun rotateImage(source:Bitmap) : Bitmap
    {
        val cameraManager = activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        val matrix = Matrix()

        cameraManager?.let {

            val lensFacing = it.getCameraCharacteristics(cameraId).get(CameraCharacteristics.LENS_FACING)

            if (lensFacing == CameraCharacteristics.LENS_FACING_FRONT)
            {
                // Prevent front facing camera from flipping the image
                matrix.preScale(-1.0f,1.0f)
            }
        }

        matrix.postRotate(90f)
        return Bitmap.createBitmap(source,0,0,source.width,source.height,matrix,true)
    }
}