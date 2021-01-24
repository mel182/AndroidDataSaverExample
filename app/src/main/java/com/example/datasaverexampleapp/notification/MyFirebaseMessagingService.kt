package com.example.datasaverexampleapp.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService()
{
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        Log.i("TAG","onMessageReceived: ${p0}")

    }

    override fun onNewToken(p0: String) {
        Log.i("TAG","Token: ${p0}")
    }
}