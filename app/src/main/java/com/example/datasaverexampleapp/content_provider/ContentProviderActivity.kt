package com.example.datasaverexampleapp.content_provider

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityContentProviderBinding
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContentProviderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_provider)

        DataBindingUtil.setContentView<ActivityContentProviderBinding>(
            this, Layout.activity_content_provider
        ).apply {

            launchButton.setOnClickListener {

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
}