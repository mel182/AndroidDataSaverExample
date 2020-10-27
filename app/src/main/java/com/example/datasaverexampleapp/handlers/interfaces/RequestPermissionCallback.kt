package com.example.datasaverexampleapp.handlers.interfaces

interface RequestPermissionCallback {
    fun onPermissionGranted()
    fun onPermissionDenied()
}