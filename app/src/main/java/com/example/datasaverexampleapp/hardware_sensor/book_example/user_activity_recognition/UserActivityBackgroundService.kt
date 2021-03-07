package com.example.datasaverexampleapp.hardware_sensor.book_example.user_activity_recognition

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.ActivityRecognitionClient

class UserActivityBackgroundService : Service()
{
    val TAG = "UserActivityBackgroundService"

    private var intentService:Intent? = null
    private var pendingIntent: PendingIntent? = null
    private var activityRecognitionClient:ActivityRecognitionClient? = null

    var binder:IBinder = LocalBinder()

    inner class LocalBinder : Binder() {
        val serverInstance: UserActivityBackgroundService
            get() = this@UserActivityBackgroundService
    }

    override fun onCreate() {
        super.onCreate()
        activityRecognitionClient = ActivityRecognitionClient(this)
        intentService = Intent(this,ActivitiesDetectionService::class.java).apply {
            pendingIntent = PendingIntent.getService(this@UserActivityBackgroundService,1,this,PendingIntent.FLAG_UPDATE_CURRENT)
            this@UserActivityBackgroundService.requestActivityUpdatesButtonHandler()
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    private fun requestActivityUpdatesButtonHandler()
    {
        pendingIntent?.let { activityPendingIntent ->

            activityRecognitionClient?.requestActivityUpdates(1000L,activityPendingIntent)?.apply {

                addOnSuccessListener {
                    Intent(Constant.SERVICE_STATE)?.apply {
                        putExtra("state","Activity requested activity updates")
                        LocalBroadcastManager.getInstance(this@UserActivityBackgroundService).sendBroadcast(this)
                    }
                }

                addOnFailureListener{
                    Intent(Constant.SERVICE_STATE).apply {
                        putExtra("state","User activity updates failed to start, reason: ${it.message}")
                        LocalBroadcastManager.getInstance(this@UserActivityBackgroundService).sendBroadcast(this)
                    }
                }
            }
        }
    }

    private fun removeActivityUpdatesButtonHandler()
    {
        pendingIntent?.let { userActivityPendingIntent ->
            activityRecognitionClient?.removeActivityUpdates(userActivityPendingIntent)?.apply {

                addOnSuccessListener {
                    Intent(Constant.SERVICE_STATE)?.apply {
                        putExtra("state","Removed activity updates successfully!")
                        LocalBroadcastManager.getInstance(this@UserActivityBackgroundService).sendBroadcast(this)
                    }
                }

                addOnFailureListener{
                    Intent(Constant.SERVICE_STATE)?.apply {
                        putExtra("state","Failed to remove activity updates!")
                        LocalBroadcastManager.getInstance(this@UserActivityBackgroundService).sendBroadcast(this)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeActivityUpdatesButtonHandler()
    }
}