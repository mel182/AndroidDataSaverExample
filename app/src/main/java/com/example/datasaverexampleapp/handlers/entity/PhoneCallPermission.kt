package com.example.datasaverexampleapp.handlers.entity

import android.Manifest

class PhoneCallPermission : PermissionEntity
{
    override fun getManifestPermission(): String {
        return Manifest.permission.CALL_PHONE
    }
}