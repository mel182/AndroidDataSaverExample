package com.example.datasaverexampleapp.content_provider

import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_content_provider.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContentProviderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_provider)

        launch_button.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch{

                val cursor = contentResolver.query(Uri.parse("content://com.test.provider.datasaverexampleapp/all"),null,null,null,null)

                cursor?.let {

                    if (it.moveToFirst())
                    {
                        Log.i("TAG","it: $it")
                    } else {
                        Log.i("TAG","Result is empty")
                    }
                }
            }
        }
    }
}