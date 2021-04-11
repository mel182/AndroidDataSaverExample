package com.example.datasaverexampleapp.camera.image_capture_intent

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_image_capture_intent_example.*
import java.io.File
import java.io.FileOutputStream

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
    private var outputFile : File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_capture_intent_example)
        title = "Camera Intent Example"

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

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

                if (it.hasExtra(THUMBNAIL_DATA_KEY))
                {
                    it.getParcelableExtra<Bitmap>(THUMBNAIL_DATA_KEY)?.apply {

                        picture_result_imageView?.setImageBitmap(this)
                        image_view_title?.text = "Picture taken"
                        take_picture_button?.apply {
                            text = "Take another picture"
                            isEnabled = true
                        }

                        // Save image based on the outfile uri provided
                        try {
                            val outputStream = FileOutputStream(outputFile)
                            compress(Bitmap.CompressFormat.PNG,90,outputStream)
                            outputStream.flush()
                            outputStream.close()
                            Toast.makeText(this@ImageCaptureIntentExampleActivity,"Image stored",Toast.LENGTH_SHORT).show()
                        }catch (e : Exception)
                        {
                            e.printStackTrace()
                        }
                    }?: kotlin.run {
                        image_view_title?.text = "Failed parsing image"
                        take_picture_button?.apply {
                            text = "Take picture"
                            isEnabled = true
                        }
                    }

                } else {
                    image_view_title?.text = "Failed parsing image"
                    take_picture_button?.apply {
                        text = "Take picture"
                        isEnabled = true
                    }
                }
            }?: kotlin.run {
                image_view_title?.text = "Failed parsing image"
                take_picture_button?.apply {
                    text = "Take picture"
                    isEnabled = true
                }
            }
        }

        take_picture_button?.setOnClickListener {

            // Specify the output file
            val mediaDirectory = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                externalMediaDirs[0]
            } else {
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            }

            outputFile = File(mediaDirectory,"intent_picture.jpg")

            // Generate the intent
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(outputFile.toString()))
            }

            // Launch the camera app
            resultLauncher.launch(takePictureIntent)

            take_picture_button?.apply {
                isEnabled = false
            }
        }
    }
}