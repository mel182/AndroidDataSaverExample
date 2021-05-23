package com.example.datasaverexampleapp.handlers.permission.entity

class AccessFineLocation : PermissionEntity {
    override fun getManifestPermission(): String = android.Manifest.permission.ACCESS_FINE_LOCATION
}