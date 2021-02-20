package com.example.datasaverexampleapp.activityRequestHandler

import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.datasaverexampleapp.speech_recognition_example.OnActivityResult
import com.example.datasaverexampleapp.speech_recognition_example.OnPermissionResult

class ActivityResultHandler(var activity:AppCompatActivity?)
{
    private var onActivityResult: OnActivityResult? = null
    private var onPermissionResult: OnPermissionResult? = null

    private var permissionRequest : ActivityResultLauncher<String>? = null
    private var permissionRequestArray : ActivityResultLauncher<Array<String>>? = null
    private var activityForResult : ActivityResultLauncher<Intent>? = null

    init {

        permissionRequest = activity?.registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
            onPermissionResult?.onPermissionResult(isGranted)
            onPermissionResult = null
        }

        permissionRequestArray = activity?.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ resultList ->
            onPermissionResult?.onPermissionResults(result = resultList)
            onPermissionResult = null
        }

        activityForResult = activity?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onActivityResult?.onActivityResult(result)
            onActivityResult = null
        }

    }

    fun requestPermission(permission:String, onPermissionResult: OnPermissionResult)
    {
        activity?.let { context ->

            if (ActivityCompat.checkSelfPermission(context,permission) != PERMISSION_GRANTED)
            {
                this.onPermissionResult = onPermissionResult
                permissionRequest?.launch(permission)
            } else {
                onPermissionResult.onPermissionResult(true)
            }

        }?: kotlin.run {
            onPermissionResult.onPermissionResult(false)
        }
    }

    fun requestPermission(permissions:Array<String>, onPermissionResult: OnPermissionResult)
    {
        activity?.let { context ->

            if (permissions.isEmpty())
                onPermissionResult.onPermissionResult(false)

            var requestingPermission = false

            for (permission in permissions)
            {
                if (ActivityCompat.checkSelfPermission(context, permission) != PERMISSION_GRANTED)
                {
                    this.onPermissionResult = onPermissionResult
                    permissionRequestArray?.launch(permissions)
                    requestingPermission = true
                    break
                }
            }

            if (!requestingPermission)
                onPermissionResult.onPermissionResult(true)

        }?: kotlin.run {
            onPermissionResult.onPermissionResult(false)
        }
    }

    fun startActivityForResult(intent:Intent, onActivityResult: OnActivityResult)
    {
        this.onActivityResult = onActivityResult
        activityForResult?.launch(intent)
    }

    fun clear()
    {
        activity = null
    }
}