package com.example.datasaverexampleapp.intent_example

import androidx.appcompat.app.AppCompatActivity
import com.example.datasaverexampleapp.handlers.PermissionRequestHandler
import com.example.datasaverexampleapp.handlers.interfaces.RequestPermissionCallback

abstract class IntentBaseActivity : AppCompatActivity()
{
    var permissionRequestHandler:PermissionRequestHandler? = null

    fun requestPermission(type:String,permissionRequestCallback: RequestPermissionCallback)
    {
        if (permissionRequestHandler == null)
        {
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
    }

    private fun releasePermissionRequestHandler()
    {
        permissionRequestHandler = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        permissionRequestHandler?.handlePermissionsResult(requestCode,grantResults)
    }
}