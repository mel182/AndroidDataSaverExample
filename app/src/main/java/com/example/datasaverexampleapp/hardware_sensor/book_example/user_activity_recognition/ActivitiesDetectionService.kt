package com.example.datasaverexampleapp.hardware_sensor.book_example.user_activity_recognition

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity

class ActivitiesDetectionService : IntentService(ActivitiesDetectionService::class.java.simpleName)
{
    override fun onCreate() {
        super.onCreate()
    }

    override fun onHandleIntent(intent: Intent?) {

        intent?.let { it ->
            ActivityRecognitionResult.extractResult(it)?.also { result ->
                val detectedActivities = result.probableActivities.toTypedArray()

                for (activityDetected in detectedActivities)
                {
                    broadcastActivity(activityDetected)
                }
            }
        }
    }

    private fun broadcastActivity(activity:DetectedActivity)
    {
        Intent(Constant.BROADCAST_DETECTED_ACTIVITY)?.apply {
            putExtra("type",activity.type)
            putExtra("confidence",activity.confidence)
            LocalBroadcastManager.getInstance(this@ActivitiesDetectionService).sendBroadcast(this)
        }
    }
}