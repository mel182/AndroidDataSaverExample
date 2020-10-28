package com.example.datasaverexampleapp.handlers.permission.entity

import android.Manifest

class PhoneCallPermission : PermissionEntity
{
    override fun getManifestPermission(): String {
        return Manifest.permission.CALL_PHONE
    }
}