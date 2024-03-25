package com.jetpackcompose.biometricauth

import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager

class BiometricPromptManager(
    private val activity: AppCompatActivity
) {

    fun showBiometricPrompt(
        title:String,
        description:String
    ) {
        val manager = BiometricManager.from(activity)
        //val
    }


}