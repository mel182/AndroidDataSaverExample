package com.example.datasaverexampleapp.storage_manager

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import com.example.datasaverexampleapp.BuildConfig
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.handlers.activity_result_handler.constant.ActivityIntent
import com.example.datasaverexampleapp.handlers.activity_result_handler.interfaces.OnActivityResultCallback
import com.example.datasaverexampleapp.intent_example.IntentBaseActivity
import kotlinx.android.synthetic.main.activity_storage_manager_example.*
import java.io.*

class StorageManagerExampleActivity : IntentBaseActivity() {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage_manager_example)
        this.title = "Storage Manager Example"

        open_directory_example?.setOnClickListener {

            /*
            Allow the user to pick a directory subtree.
            When invoked, the system will display the various DocumentsProvider instances installed on the device,
            letting the user navigate through them.
            Apps can fully manage documents within the returned directory.
            */
            activityForResult(
                ActivityIntent.PICK_OPEN_DIRECTORY,
                object : OnActivityResultCallback {

                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun onActivityResult(resultCode: Int, data: Intent?) {

                        if (resultCode == RESULT_OK) {
                            data?.let {
                                Toast.makeText(
                                    this@StorageManagerExampleActivity,
                                    "document tree: ${it.data}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.i("TAG", "Data content uri: ${it.data}")

                                it.data?.let {
                                    val rawUri = it.toString()
                                    handleDocumentTreeUri(Uri.parse(rawUri))
                                }
                            }
                        } else if (resultCode == RESULT_CANCELED) {
                            Log.i("TAG", "Result cancelled")
                        }
                    }
                }
            )
        }

        file_provider_example?.setOnClickListener {

            val photoDirectory = File(filesDir, "images")
            val imageToShare = File(photoDirectory, "shared_image.png")

            val contentUri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".files",
                imageToShare
            )

            ShareCompat.IntentBuilder.from(this).setType("image/png").setStream(contentUri).startChooser()
        }

        receive_file_from_file_provider_example?.setOnClickListener {

            val uri = ShareCompat.IntentReader.from(this).stream
            var bitmap:Bitmap? = null
            try {
                uri?.let {
                    val inputStream = contentResolver.openInputStream(it)
                    bitmap = BitmapFactory.decodeStream(inputStream)
                    Log.i("TAG", "Bitmap: ${bitmap}")
                }
            }catch (e: IOException)
            {
                Log.i("TAG", "IO Exception: ${e.message}")
            }
        }

        temporary_file_access_example?.setOnClickListener {

            activityForResult(
                ActivityIntent.PICK_IMAGE_FILE_INTENT,
                object : OnActivityResultCallback {

                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun onActivityResult(resultCode: Int, data: Intent?) {

                        if (resultCode == RESULT_OK) {
                            data?.let {
                                Toast.makeText(
                                    this@StorageManagerExampleActivity,
                                    "image file data: ${it.data}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.i("TAG", "Data content uri: ${it.data}")
                            }
                        } else if (resultCode == RESULT_CANCELED) {
                            Log.i("TAG", "Image file result cancelled")
                        }
                    }
                }
            )
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun handleDocumentTreeUri(documentTreeUri: Uri)
    {
        val directory = DocumentFile.fromTreeUri(this, documentTreeUri)

        val files = directory?.listFiles()

        files?.let {

            it.forEach {

                if (it.isDirectory)
                {
                    handleDocumentTreeUri(it.uri)
                } else {

                    try {
                     val inputStream = contentResolver.openInputStream(it.uri)
                     val bufferReader = BufferedReader(InputStreamReader(inputStream))

                        var mLine: String?
                        while (bufferReader.readLine().also { mLine = it } != null) {
                            Log.i("TAG", "Line: ${mLine}\n")
                        }
                        Log.i("TAG", "input stream: ${inputStream}")
                    }catch (e: FileNotFoundException)
                    {
                        Log.i("TAG", "FileNotFoundException: ${e.message}")
                    } catch (e: IOException)
                    {
                        Log.i("TAG", "IOException: ${e.message}")
                    }
                }
            }
        }
    }
}