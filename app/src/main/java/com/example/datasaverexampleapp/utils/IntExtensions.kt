package com.example.datasaverexampleapp.utils

import android.util.TypedValue
import com.example.datasaverexampleapp.application.AppContext


fun Int.refinePath() : String {

    val value = TypedValue()
    AppContext.appContext.resources.getValue(this, value, true)

    return value.string.toString().replace("res/raw/","")
}