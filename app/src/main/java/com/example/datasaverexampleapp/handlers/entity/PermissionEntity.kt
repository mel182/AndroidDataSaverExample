package com.example.datasaverexampleapp.handlers.entity

interface PermissionEntity {
    fun getManifestPermission():String
    fun requiredPermission():Boolean {return true}
}