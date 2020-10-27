package com.example.datasaverexampleapp.handlers.extensions

import android.content.pm.PackageManager

fun IntArray?.isPermissionGranted() : Boolean
{
    return this?.let { permissionResultArray ->
        return permissionResultArray[0] == PackageManager.PERMISSION_GRANTED
    }?:false
}