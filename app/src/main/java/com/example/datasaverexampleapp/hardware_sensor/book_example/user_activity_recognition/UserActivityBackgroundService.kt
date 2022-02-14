package com.example.datasaverexampleapp.hardware_sensor.book_example.user_activity_recognition

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
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
        // To receive updates on the user's current activity, first get an instance of the 'ActivityRecognition.getClient' static method and passing in a Context.
        activityRecognitionClient = ActivityRecognitionClient(this)
        intentService = Intent(this,ActivitiesDetectionService::class.java).apply {
            pendingIntent = PendingIntent.getService(this@UserActivityBackgroundService,1,this, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE else PendingIntent.FLAG_UPDATE_CURRENT)
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

            // To request updates, use the 'requestActivityUpdates' method, passing in a preferred detection
            // interval in millisecond and a Pending Intent that will be fired when a change in user activity
            // is detected.
            // Note: The returned Task can be used to check the success of the call, using the 'addSuccessListener' and
            //       'addOnFailureListener' method to add add On and OnFailure Listener, respectively
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

            // When you no longer need to receive activity change updates, call 'removeActivityUpdates', passing
            // in the pending Intent used to request the update results.
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