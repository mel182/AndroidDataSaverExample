package com.example.datasaverexampleapp.utils

import com.example.datasaverexampleapp.application.AppContext

fun String.refinePath() : String = this.replace("${AppContext.appContext.packageName}:res/raw/","")