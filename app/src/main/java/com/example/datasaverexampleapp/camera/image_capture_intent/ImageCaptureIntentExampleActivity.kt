@file:Suppress("UNUSED_VARIABLE", "DEPRECATION", "VARIABLE_WITH_REDUNDANT_INITIALIZER","UNUSED_ANONYMOUS_PARAMETER",
    "UNNECESSARY_SAFE_CALL"
)

package com.example.datasaverexampleapp.camera.image_capture_intent

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.BuildConfig
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityImageCaptureIntentExampleBinding
import com.example.datasaverexampleapp.dialog.AppCompatDialogFragmentExample
import com.example.datasaverexampleapp.type_alias.Layout
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


/**
 * This is an example on how to use the MediaStore.ACTION_IMAGE_CAPTURE intent to take
 * a picture from within your application.
 * This launches a camera application to take the photo, providing the full suite of camera
 * functionality without you having  to rewrite the native camera application yourself.
 *
 * NOTE: This Intent is not intented to be used as a fallback if the user denies the
 *       CAMERA permission in your app. Users declining a permission is a clear signal
 *       that they don't want your app to use this feature, and you must respect that.
 *
 */
class ImageCaptureIntentExampleActivity : AppCompatActivity() {

    private val THUMBNAIL_DATA_KEY = "data"
    private var outputFile: Uri? = null

    private var outputFilePath: String? = null
    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private var binding: ActivityImageCaptureIntentExampleBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_capture_intent_example)
        title = "Camera Intent Example"

        binding = DataBindingUtil.setContentView<ActivityImageCaptureIntentExampleBinding>(
            this, Layout.activity_image_capture_intent_example
        ).apply {

            resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

                    // Once the user is satisfied with the image, the result is returned to your application
                    // within the Intent received.
                    //
                    // By default, the picture taken will be returned as a thumbnail, available as a raw bitmap
                    // within the 'data' extra within the returned Intent.
                    //
                    // To obtain a full image, you must specify a target URI.
                    // NOTE: On most device obtain full-size image does not work accordingly
                    //
                    result.data?.let {

                        if (it.hasExtra(THUMBNAIL_DATA_KEY)) {
                            // Contains thumbnail images
                            it.getParcelableExtra<Bitmap>(THUMBNAIL_DATA_KEY)?.apply {

                                pictureResultImageView.setImageBitmap(this)
                                imageViewTitle.text = "Picture taken"
                                takePictureButton.isEnabled = true

                                // Save image based on the outfile uri provided
                                try {

                                    outputFile?.let { uri ->

                                        uri.path?.let { path ->
                                            val outputStream = FileOutputStream(File(path))
                                            compress(Bitmap.CompressFormat.PNG, 90, outputStream)
                                            outputStream.flush()
                                            outputStream.close()
                                            Toast.makeText(
                                                this@ImageCaptureIntentExampleActivity,
                                                "Image stored",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            } ?: kotlin.run {
                                imageViewTitle.text = "Failed parsing image"
                                takePictureButton.isEnabled = true
                            }

                        } else {

                            // If there is no thumbnail image data, the image
                            // will have been stored in the target output URI.

                            imageViewTitle.text = "Failed parsing image"
                            takePictureButton.isEnabled = true
                        }
                    } ?: kotlin.run {

                        // If there is no thumbnail image data, the image
                        // will have been stored in the target output URI.

                        outputFilePath?.let { path ->

                            val exif = ExifInterface(path)

                            val orientation = exif.getAttributeInt(
                                ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_NORMAL
                            )
//                    val bitmapOrientation = exifInDegrees(orientation)
//
//                    Log.i("TAG","Bitmap orientation: ${ exifInDegrees(orientation)}")
//                    Log.i("TAG","Display orientation: ${ resources.configuration.orientation}"
//                    val width = 400
//                    val height = 300

                            val bitmapOptions = BitmapFactory.Options().apply {

                                inJustDecodeBounds = true
                                BitmapFactory.decodeFile(path, this)

                                val width: Int = outWidth
                                val height: Int = outHeight

                                // Determine how much to scale down the image
//                        val scaleFactor : Int = Math.max(1, Math.min(width/500, height/500))

                                // Decode the image file into a Bitmap sized to fill the View
                                inJustDecodeBounds = false
//                        inSampleSize = scaleFactor
                            }

                            BitmapFactory.decodeFile(path, bitmapOptions)?.also { bitmap ->

                                val modifiedBitmap = when (orientation) {

                                    ExifInterface.ORIENTATION_ROTATE_90 -> rotate(bitmap, 90F)
                                    ExifInterface.ORIENTATION_ROTATE_180 -> rotate(bitmap, 180F)
                                    ExifInterface.ORIENTATION_ROTATE_270 -> rotate(bitmap, 270F)
                                    ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flip(
                                        bitmap,
                                        true,
                                        false
                                    )
                                    ExifInterface.ORIENTATION_FLIP_VERTICAL -> flip(bitmap, false, true)
                                    else -> bitmap
                                }

                                pictureResultImageView.setImageBitmap(modifiedBitmap)
                                imageViewTitle.text = "Full-size picture taken"
                                takePictureButton.isEnabled = true
                                saveImage(modifiedBitmap)
                            }

                        } ?: kotlin.run {
                            imageViewTitle.text = "Failed parsing image"
                            takePictureButton.isEnabled = true
                        }
                    }
                }

            takePictureButton.setOnClickListener {
                val dialogFragment = AppCompatDialogFragmentExample().apply {

                    setTitle("Camera option")
                    setMessage("Select camera option")
                    setPositiveButton("Thumbnail")
                    setNegativeButton("Full size quality")
                    setListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                resultLauncher?.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                            }

                            DialogInterface.BUTTON_NEGATIVE -> {
                                dispatchTakePictureIntent()
                            }
                        }
                    }
                }
                dialogFragment.show(supportFragmentManager, null)
            }
        }
    }

    private fun saveImage(bitmap: Bitmap) {

        // Save to picture directory
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            val filename = "image_test_${System.currentTimeMillis()}.jpg"
            var fos: OutputStream? = null
            var imageUri:Uri? = null

            //use application context to get contentResolver
            val contentResolver = application.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE,"image/jpg")
                put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES)
                put(MediaStore.Video.Media.IS_PENDING,1)
            }

            contentResolver.also { resolver ->
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)
                fos = imageUri?.let { outputStream -> resolver.openOutputStream(outputStream) }
            }

            fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 70, it) }

            contentValues.clear()
            contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)

            imageUri?.let {
                contentResolver.update(it,contentValues,null,null)
                Toast.makeText(this, "Image saved in Picture folder", Toast.LENGTH_LONG).show()
            }

        } else {
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val filename = "image_capture_${System.currentTimeMillis()}.JPG" // File name: 'image_capture_current_milli'.JPG
            val file = File(path, filename)
            try {
                val fileOutputStream = FileOutputStream(file)
                fileOutputStream?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE))
                Uri.parse("file://${filename}")
                Toast.makeText(this, "Image saved in Picture folder", Toast.LENGTH_LONG).show()
            } catch (e: IOException) {
                Log.i("TAG", "Failed creating new file, reason: ${e.message}")
            }
        }
    }

    private fun rotate(bitmap: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix().apply {
            postRotate(degree)
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun flip(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap {
        val matrix = Matrix().apply {
            preScale(if (horizontal) -1F else 1F, if (vertical) -1F else 1F)
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File? {
        try {

            getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.let {

                // Create an image file name
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//                val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
                return File.createTempFile(
                    "JPEG_${timeStamp}_", /* prefix */
                    ".jpg", /* suffix */
                    it /* directory */
                ).apply {
                    // Save a file: path for use with ACTION_VIEW intents
                    outputFilePath = absolutePath
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    private fun dispatchTakePictureIntent() {

        binding?.apply {

            createImageFile()?.let { imageFile ->

                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    // Ensure that there's a camera activity to handle the intent
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        // Create the File where the photo should go

                        // Continue only if the File was successfully created
                        imageFile.also {
                            val photoURI: Uri = FileProvider.getUriForFile(
                                this@ImageCaptureIntentExampleActivity,
                                "${BuildConfig.APPLICATION_ID}.files",
                                it
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            resultLauncher?.launch(takePictureIntent)
                            takePictureButton?.isEnabled = false
                        }
                    }
                }

            } ?: kotlin.run {
                Log.i("TAG", "Error creating image file!")
            }
        }
    }
}