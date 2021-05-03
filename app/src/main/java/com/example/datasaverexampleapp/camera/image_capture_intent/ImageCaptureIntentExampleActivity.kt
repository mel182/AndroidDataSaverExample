package com.example.datasaverexampleapp.camera.image_capture_intent

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.datasaverexampleapp.BuildConfig
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_image_capture_intent_example.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
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
    private var outputFile : Uri? = null

    private var outputFilePath:String? = null
    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_capture_intent_example)
        title = "Camera Intent Example"

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

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
                    // Contains thumbnail images
                    it.getParcelableExtra<Bitmap>(THUMBNAIL_DATA_KEY)?.apply {

                        picture_result_imageView?.setImageBitmap(this)
                        image_view_title?.text = "Picture taken"
                        take_picture_button?.apply {
                            text = "Take another picture"
                            isEnabled = true
                        }

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

                        }catch (e: Exception)
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

                    // If there is no thumbnail image data, the image
                    // will have been stored in the target output URI.

                    image_view_title?.text = "Failed parsing image"
                    take_picture_button?.apply {
                        text = "Take picture"
                        isEnabled = true
                    }
                }
            }?: kotlin.run {

                // If there is no thumbnail image data, the image
                // will have been stored in the target output URI.

                outputFilePath?.let { path ->


                    val exif = ExifInterface(path)

                    val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
//                    val bitmapOrientation = exifInDegrees(orientation)
//
//
//                    Log.i("TAG","Bitmap orientation: ${ exifInDegrees(orientation)}")
//                    Log.i("TAG","Display orientation: ${ resources.configuration.orientation}")


//                    val width = 400
//                    val height = 300

                    val bitmapOptions = BitmapFactory.Options().apply {

                        inJustDecodeBounds = true
                        BitmapFactory.decodeFile(path, this)

                        val width : Int = outWidth
                        val height : Int = outHeight

                        // Determine how much to scale down the image
//                        val scaleFactor : Int = Math.max(1, Math.min(width/500, height/500))

                        // Decode the image file into a Bitmap sized to fill the View
                        inJustDecodeBounds = false
//                        inSampleSize = scaleFactor
                    }

                    BitmapFactory.decodeFile(path, bitmapOptions)?.also { bitmap ->


                        val modifiedBitmap = when (orientation){

                            ExifInterface.ORIENTATION_ROTATE_90 -> rotate(bitmap,90F)
                            ExifInterface.ORIENTATION_ROTATE_180 -> rotate(bitmap,180F)
                            ExifInterface.ORIENTATION_ROTATE_270 -> rotate(bitmap,270F)
                            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flip(bitmap,true,false)
                            ExifInterface.ORIENTATION_FLIP_VERTICAL -> flip(bitmap,false,true)
                            else -> bitmap
                        }

                        picture_result_imageView?.setImageBitmap(modifiedBitmap)
                    }
//
//                    val factoryOptions = BitmapFactory.Options()
//                    factoryOptions.inJustDecodeBounds = true
//                    BitmapFactory.decodeFile(path,factoryOptions)
//
//                    val scaleFactor = Math.min(factoryOptions.outWidth/width, factoryOptions.outHeight/height)
//
//                    factoryOptions.inJustDecodeBounds = false
//                    factoryOptions.inSampleSize = scaleFactor
//
//                    val bitmap = BitmapFactory.decodeFile(path,factoryOptions)
//                    picture_result_imageView?.setImageBitmap(bitmap)
//
//                    take_picture_button?.apply {
//                        text = "Take picture"
//                        isEnabled = true
//                    }

                }?: kotlin.run {
                    image_view_title?.text = "Failed parsing image"
                    take_picture_button?.apply {
                        text = "Take picture"
                        isEnabled = true
                    }
                }
            }
        }

        take_picture_button?.setOnClickListener {

            dispatchTakePictureIntent()

            // Specify the output file
//            val mediaDirectory = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                externalMediaDirs[0]
//            } else {
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//            }

//            val mediaDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

//            try {
//
//                //            outputFile = File(mediaDirectory,"intent_picture.jpg")
////                outputFile = File.createTempFile("intent_picture",".jpg",mediaDirectory)
//
//                val photoDirectory = File(filesDir,"images")
//                val imageFile = File(photoDirectory,"intent_picture.png")
//
//
////            val path = outputFile?.absolutePath
//
//                // Generate the intent
////                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
////
////                takePictureIntent.resolveActivity(packageManager)?.let {
////
////
////
//////                    outputFile?.let {
////
//////                        val provider = "${BuildConfig.APPLICATION_ID}.files"
//////
//////                        outputFile = FileProvider.getUriForFile(this,provider,imageFile)
//////
//////                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFile)
//////
//////                        resultLauncher?.launch(takePictureIntent)
//////
//////                        take_picture_button?.isEnabled = false
////
//////                    }
////
//////                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(outputFile.toString()))
////                }
//
//                /*
//                static final int REQUEST_TAKE_PHOTO = 1;
//
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//                ...
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                                                      "com.example.android.fileprovider",
//                                                      photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }
//                */
//
//
////            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//
//                // Launch the camera app
////                resultLauncher.launch(takePictureIntent)
//                /*
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
//                */
//
////                take_picture_button?.isEnabled = false
//
//            }catch (e:Exception)
//            {
//                e.printStackTrace()
//            }
        }
    }

//    private fun exifInDegrees(exifOrientation:Int) : Int
//    {
//        return when(exifOrientation)
//        {
//            ExifInterface.ORIENTATION_ROTATE_90 -> 90
//            ExifInterface.ORIENTATION_ROTATE_180 -> 180
//            ExifInterface.ORIENTATION_ROTATE_270 -> 270
//            else -> 0
//        }
//    }


    private fun rotate(bitmap:Bitmap, degree:Float) : Bitmap
    {
        val matrix = Matrix().apply {
            postRotate(degree)
        }
        return Bitmap.createBitmap(bitmap, 0,0,bitmap.width,bitmap.height,matrix,true)
    }

    private fun flip(bitmap:Bitmap, horizontal:Boolean, vertical : Boolean) : Bitmap
    {
        val matrix = Matrix().apply {
            preScale(if (horizontal) -1F else 1F, if (vertical) -1F else 1F)
        }
        return Bitmap.createBitmap(bitmap, 0,0,bitmap.width,bitmap.height,matrix,true)
    }

    private fun createImageFile() : File?
    {
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

        }catch (e: IOException)
        {
            e.printStackTrace()
        }

        return null
    }

    private fun dispatchTakePictureIntent() {

        createImageFile()?.let { imageFile ->

            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure that there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go
//                    val photoFile: File? = try {
//                        createImageFile()
//                    } catch (ex: IOException) {
//                        // Error occurred while creating the File
//                        ex.printStackTrace()
//                        null
//                    }
                    // Continue only if the File was successfully created
                    imageFile.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "${BuildConfig.APPLICATION_ID}.files",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        resultLauncher?.launch(takePictureIntent)
                        take_picture_button?.isEnabled = false
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            }

        }?: kotlin.run {
            Log.i("TAG", "Error creating image file!")
        }

    }
}