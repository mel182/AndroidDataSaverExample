package com.example.datasaverexampleapp.handlers.activity_result_handler.interfaces

import android.content.Intent

interface OnActivityResultCallback {
    fun onActivityResult(resultCode: Int, data: Intent?)
}