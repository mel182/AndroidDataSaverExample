package com.example.datasaverexampleapp.handlers.activity_result_handler.constant

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class ActivityIntent
{
    companion object {
        const val PICK_CONTACT = Intent.ACTION_PICK
        const val PICK_STAR_SIGN = "StarSignPicker"
        const val PICK_OPEN_DIRECTORY = "PICK_OPEN_DIRECTORY"
        const val PICK_IMAGE_FILE_INTENT = "PICK_IMAGE_FILE_INTENT"
        const val ENABLE_BLUETOOTH = "android.bluetooth.adapter.action.REQUEST_ENABLE"
    }
}