package com.example.datasaverexampleapp.handlers.permission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.datasaverexampleapp.handlers.permission.entity.AccessFineLocation
import com.example.datasaverexampleapp.handlers.permission.entity.PermissionEntity
import com.example.datasaverexampleapp.handlers.permission.entity.PhoneCallPermission
import com.example.datasaverexampleapp.handlers.permission.entity.ReadContactPermission
import com.example.datasaverexampleapp.handlers.permission.extensions.isPermissionGranted
import com.example.datasaverexampleapp.handlers.permission.interfaces.RequestPermissionCallback
import com.example.datasaverexampleapp.handlers.permission.permissions.Permission

class PermissionRequestHandler(private val activity:Activity)
{
    private val permissionRequestCode = 135
    private var permissionRequestCallback: RequestPermissionCallback? = null

    fun requestPermission(type:String,callback: RequestPermissionCallback)
    {
        this.permissionRequestCallback = callback

        when(type)
        {
            Permission.CALL_PHONE -> {
                requestPermission(PhoneCallPermission())
            }

            Permission.READ_CONTACT -> {
                requestPermission(ReadContactPermission())
            }

            Permission.ACCESS_FINE_LOCATION -> {
                requestPermission(AccessFineLocation())
            }
        }
    }

    private fun requestPermission(permission:PermissionEntity)
    {
        if (!permission.requiredPermission())
        {
            permissionRequestCallback?.onPermissionGranted()
        } else {
            if (isPermissionGranted(permission))
            {
                permissionRequestCallback?.onPermissionGranted()
            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(permission.getManifestPermission()),permissionRequestCode)
            }
        }
    }

    private fun isPermissionGranted(permission:PermissionEntity) : Boolean
    {
        return ContextCompat.checkSelfPermission(activity, permission.getManifestPermission()) == PackageManager.PERMISSION_GRANTED
    }

    fun handlePermissionsResult(requestCode: Int, grantResults: IntArray) {
        when(requestCode){
            permissionRequestCode -> {

                if (grantResults.isPermissionGranted())
                {
                    permissionRequestCallback?.onPermissionGranted()
                } else {
                    permissionRequestCallback?.onPermissionDenied()
                }
            }
        }
    }
}