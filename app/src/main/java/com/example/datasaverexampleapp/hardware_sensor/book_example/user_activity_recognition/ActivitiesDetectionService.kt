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

            // To extract the Activity Recognition Result from the Intent fired when a new user activity has been detected, use the 'extractResult' method.
            ActivityRecognitionResult.extractResult(it)?.also { result ->

                // The returned Activity Recognition Result includes the 'getMostProbableActivity' method that returns a 'DetectActivity'
                // that describes the activity type for which it has the highest confidence that it's being performed.
//                val detectedActivity = result.mostProbableActivity

                // Alternately, you can use the 'getProbableActivities' method to return a list of all the likely activities
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