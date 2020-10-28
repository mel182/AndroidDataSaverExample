package com.example.datasaverexampleapp.handlers.permission.entity

import android.Manifest
import android.os.Build

class ReadContactPermission : PermissionEntity {

    override fun getManifestPermission(): String {
        return Manifest.permission.READ_CONTACTS
    }

    override fun requiredPermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }
}