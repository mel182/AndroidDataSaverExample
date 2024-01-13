package com.jetpackcompose.deeplink

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}