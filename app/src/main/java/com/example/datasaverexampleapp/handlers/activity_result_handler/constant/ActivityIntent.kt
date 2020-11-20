package com.example.datasaverexampleapp.handlers.activity_result_handler.constant

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class ActivityIntent
{
    companion object {
        const val PICK_CONTACT = Intent.ACTION_PICK
        const val PICK_STAR_SIGN = "StarSignPicker"
        const val PICK_OPEN_DIRECTORY = "PICK_OPEN_DIRECTORY"
    }
}