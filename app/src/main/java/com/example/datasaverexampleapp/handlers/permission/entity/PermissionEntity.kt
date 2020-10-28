package com.example.datasaverexampleapp.handlers.permission.entity

interface PermissionEntity {
    fun getManifestPermission():String
    fun requiredPermission():Boolean {return true}
}