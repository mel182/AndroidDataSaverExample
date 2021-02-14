package com.example.datasaverexampleapp.speech_recognition_example

import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class ActivityResultHandler(var activity:AppCompatActivity?)
{
    private var onActivityResult:OnActivityResult? = null
    private var onPermissionResult:OnPermissionResult? = null

    private var permissionRequest : ActivityResultLauncher<String>? = null
    private var activityForResult : ActivityResultLauncher<Intent>? = null

    init {

        permissionRequest = activity?.registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
            onPermissionResult?.onPermissionResult(isGranted)
            onPermissionResult = null
        }

        activityForResult = activity?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onActivityResult?.onActivityResult(result)
            onActivityResult = null
        }

    }

    fun requestPermission(permission:String, onPermissionResult:OnPermissionResult)
    {
        this.onPermissionResult = onPermissionResult
        permissionRequest?.launch(permission)
    }

    fun startActivityForResult(intent:Intent, onActivityResult:OnActivityResult)
    {
        this.onActivityResult = onActivityResult
        activityForResult?.launch(intent)
    }

    fun clear()
    {
        activity = null
    }
}