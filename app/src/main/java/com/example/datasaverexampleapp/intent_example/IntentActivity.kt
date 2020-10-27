package com.example.datasaverexampleapp.intent_example

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.handlers.PermissionRequestHandler
import com.example.datasaverexampleapp.handlers.interfaces.RequestPermissionCallback
import com.example.datasaverexampleapp.handlers.permissions.Permission
import kotlinx.android.synthetic.main.activity_intent.*

class IntentActivity : IntentBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent)

        intent_action_dial_button?.setOnClickListener {

            val phoneNumber = "0631564996"
            val callUri = Uri.parse("tel:${phoneNumber}")
            val callIntent = Intent(Intent.ACTION_DIAL,callUri)
            startActivity(callIntent)
        }

        intent_action_call_button?.setOnClickListener {
            // Remember add:
            // <uses-permission android:name="android.permission.CALL_PHONE"></uses-permission>

            requestPermission(Permission.CALL_PHONE, object : RequestPermissionCallback{

                override fun onPermissionGranted() {
                    val phoneNumber = "0631564996"
                    val callUri = Uri.parse("tel:${phoneNumber}")
                    val callIntent = Intent(Intent.ACTION_CALL, callUri)
                    startActivity(callIntent)
                }

                override fun onPermissionDenied() {
                    Toast.makeText(this@IntentActivity, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}