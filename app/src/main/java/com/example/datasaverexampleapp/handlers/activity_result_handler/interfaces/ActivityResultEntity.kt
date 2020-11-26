package com.example.datasaverexampleapp.handlers.activity_result_handler.interfaces

import android.content.Intent

interface ActivityResultEntity {
    fun getAction(): String
    fun activityIntent(): Intent
    fun getType(): String { return "" }
}