package com.example.datasaverexampleapp.handlers.permission.interfaces

interface RequestPermissionCallback {
    fun onPermissionGranted()
    fun onPermissionDenied()
}