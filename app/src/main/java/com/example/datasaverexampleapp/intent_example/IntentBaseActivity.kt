@file:Suppress("DEPRECATION")

package com.example.datasaverexampleapp.intent_example

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.datasaverexampleapp.handlers.activity_result_handler.ActivityForResultHandler
import com.example.datasaverexampleapp.handlers.activity_result_handler.interfaces.OnActivityResultCallback
import com.example.datasaverexampleapp.handlers.permission.PermissionRequestHandler
import com.example.datasaverexampleapp.handlers.permission.interfaces.RequestPermissionCallback

abstract class IntentBaseActivity : AppCompatActivity()
{
    private var permissionRequestHandler: PermissionRequestHandler? = null
    private var activityForResultHandler: ActivityForResultHandler? = null

    fun requestPermission(type:String,permissionRequestCallback: RequestPermissionCallback)
    {
        if (permissionRequestHandler == null)
            permissionRequestHandler = PermissionRequestHandler(this)

        permissionRequestHandler?.requestPermission(type = type, callback = object: RequestPermissionCallback{

            override fun onPermissionGranted() {
                permissionRequestCallback.onPermissionGranted()
                releasePermissionRequestHandler()
            }

            override fun onPermissionDenied() {
                permissionRequestCallback.onPermissionDenied()
                releasePermissionRequestHandler()
            }
        })
    }

    fun activityForResult(type:String,callback: OnActivityResultCallback)
    {
        activityForResultHandler = ActivityForResultHandler(this)
        activityForResultHandler?.startActivityForResult(type,object : OnActivityResultCallback {

            override fun onActivityResult(resultCode: Int, data: Intent?) {
                callback.onActivityResult(resultCode,data)
                activityForResultHandler = null
            }
        })
    }

    private fun releasePermissionRequestHandler()
    {
        permissionRequestHandler = null
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        permissionRequestHandler?.handlePermissionsResult(requestCode,grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityForResultHandler?.onActivityResult(requestCode,resultCode,data)
    }
}