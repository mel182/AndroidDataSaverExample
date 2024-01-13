package com.jetpackcompose.deeplink.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings

fun Activity.openAppSettings() {

    Intent(
        Settings.ACTION_APPLICATION_SETTINGS,
        Uri.fromParts("package",packageName,null)
    ).also(::startActivity)

}