package com.example.datasaverexampleapp.speech_recognition_example

interface OnPermissionResult {
    fun onPermissionResult(result:Boolean) {}
    fun onPermissionResults(result:Map<String,Boolean>) {}
}