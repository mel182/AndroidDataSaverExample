package com.example.datasaverexampleapp.base_classes

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

abstract class BaseFragment(contentView:Int) : Fragment(contentView)
{
    protected open fun requestPermission(permission:String, activityResult:(Boolean) -> Unit)
    {
        if (activity is BaseActivity)
            (activity as BaseActivity).requestPermission(permission, activityResult)
    }

    protected open fun isPermissionGranted(permission:String) : Boolean
    {
        return if (activity is AppCompatActivity) {
            ActivityCompat.checkSelfPermission(activity as AppCompatActivity, permission) == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
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

    protected open fun requestPermissions(permissions:Array<String>, activityResult:(Boolean) -> Unit)
    {
        if (isPermissionsGranted(permissions))
        {
            activityResult(true)
            return
        }

        if (activity is BaseActivity)
            (activity as BaseActivity).requestPermissions(permissions, activityResult)
    }
}