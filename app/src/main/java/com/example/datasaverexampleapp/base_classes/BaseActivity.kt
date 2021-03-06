package com.example.datasaverexampleapp.base_classes

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.datasaverexampleapp.activityRequestHandler.ActivityResultHandler
import com.example.datasaverexampleapp.speech_recognition_example.OnPermissionResult

abstract class BaseActivity(contentView:Int) : AppCompatActivity(contentView)
{
    private var activityResultHandler: ActivityResultHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultHandler = ActivityResultHandler(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        activityResultHandler?.clear()
    }

    open fun requestPermission(permission:String, activityResult:(Boolean) -> Unit)
    {
        activityResultHandler?.requestPermission(permission, object: OnPermissionResult{
            override fun onPermissionResult(result: Boolean) {
                activityResult(result)
            }
        })
    }

    protected open fun isPermissionGranted(permission:String) : Boolean
    {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun isPermissionsGranted(permissions:Array<String>) : Boolean
    {
        var permissionGranted = true

        for (permission in permissions)
        {
            if (!isPermissionGranted(permission))
                permissionGranted = false
        }

        return permissionGranted
    }


    open fun requestPermissions(permissions:Array<String>, activityResult:(Boolean) -> Unit)
    {
        if (isPermissionsGranted(permissions))
        {
            activityResult(true)
            return
        }

        activityResultHandler?.requestPermission(permissions, object: OnPermissionResult{

            override fun onPermissionResults(result: Map<String, Boolean>) {
                super.onPermissionResults(result)

                var requestResult = true

                for (requestResultFound in result)
                {
                    if (!requestResultFound.value)
                        requestResult = requestResultFound.value
                }

                activityResult(requestResult)
            }
        })
    }

    protected open fun requestPermissionList(permissions:Array<String>, activityResultMap:(Map<String, Boolean>) -> Unit)
    {
        activityResultHandler?.requestPermission(permissions, object: OnPermissionResult{

            override fun onPermissionResults(result: Map<String, Boolean>) {
                super.onPermissionResults(result)
                activityResultMap(result)
            }
        })
    }
}