package com.example.datasaverexampleapp.hardware_sensor.book_example.user_activity_recognition

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.datasaverexampleapp.base_classes.BaseActivity
import com.example.datasaverexampleapp.databinding.ActivityUserRecognizationBinding
import com.example.datasaverexampleapp.type_alias.Drawable
import com.example.datasaverexampleapp.type_alias.Layout
import com.google.android.gms.location.DetectedActivity

// Google Activity recognition API enabled you to understand what activity your users are performing
// in the physical world. By periodically analyzing short burst of data received from device
// Sensors, Activity Recognition attempts to detect what activities the user is performing, including
// walking, driving, cycling and running.
class UserRecognitionActivity : BaseActivity(Layout.activity_user_recognization) {

    private var broadcastReceiver : BroadcastReceiver? = null
    private var activityConfidence by ConfidenceDelegate()
    private var binding: ActivityUserRecognizationBinding? = null

    // You must include the ACTIVITY_RECOGNITION permission in your manifest
    //
    // dependencies
    // implementation 'com.google.android.gms:play-services-location:11.8.0'
    //
    // <uses-permission android:name="com.google.android.gms.permission.ACTIVITY_RECOGNITION" />
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "User Recognition Example"
        binding = DataBindingUtil.setContentView<ActivityUserRecognizationBinding>(
            this, Layout.activity_user_recognization
        )
        proceedWithInitialization()
    }

    private fun proceedWithInitialization()
    {
        broadcastReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {

                intent?.let { userActivityIntent ->

                    if (userActivityIntent.action == Constant.BROADCAST_DETECTED_ACTIVITY)
                    {
                        val type = userActivityIntent.getIntExtra("type",-1)
                        val confidence = userActivityIntent.getIntExtra("confidence",0)
                        handleUserActivity(type, confidence)
                    } else if (userActivityIntent.action == Constant.SERVICE_STATE)
                    {
                        userActivityIntent.getStringExtra("state")?.apply {
                            binding?.stateTextView?.text = this
                        }
                    }
                }
            }
        }

        startTracking()
    }

    override fun onResume() {
        super.onResume()
        broadcastReceiver?.let { receiver ->
            LocalBroadcastManager.getInstance(this).apply {
                registerReceiver(receiver, IntentFilter(Constant.BROADCAST_DETECTED_ACTIVITY))
                registerReceiver(receiver, IntentFilter(Constant.SERVICE_STATE))
            }
        }
    }

    override fun onPause() {
        super.onPause()
        broadcastReceiver?.let { receiver ->
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTracking()
    }

    private fun handleUserActivity(type:Int, confidence:Int)
    {
        // For any Detected Activity, use the 'getType' and 'getConfidence' methods
        // to find the type of activity detected and the percent confidence in that result
        activityConfidence = confidence.toString()
        binding?.apply {
            userActivityTextView.text = when(type)
            {
                DetectedActivity.IN_VEHICLE -> "In vehicle (${activityConfidence})"
                DetectedActivity.ON_BICYCLE -> "On bicycle (${activityConfidence})"
                DetectedActivity.ON_FOOT -> "On foot (${activityConfidence})"
                DetectedActivity.RUNNING -> "Running (${activityConfidence})"
                DetectedActivity.STILL -> "Still (${activityConfidence})"
                DetectedActivity.TILTING -> "Tilting (${activityConfidence})"
                DetectedActivity.WALKING -> "Walking (${activityConfidence})"
                else -> "Unknown (${activityConfidence})"
            }

            val icon = when(type)
            {
                DetectedActivity.IN_VEHICLE -> Drawable.ic_driving
                DetectedActivity.ON_BICYCLE -> Drawable.ic_bicycle
                DetectedActivity.ON_FOOT -> Drawable.ic_walking
                DetectedActivity.RUNNING -> Drawable.ic_running
                DetectedActivity.STILL -> Drawable.ic_still
                DetectedActivity.TILTING -> Drawable.ic_tilting
                DetectedActivity.WALKING -> Drawable.ic_walking
                else -> Drawable.ic_unknown
            }

            activityIcon.setImageResource(icon)
        }
    }

    private fun startTracking()
    {
        val intent = Intent(this@UserRecognitionActivity, UserActivityBackgroundService::class.java)
        startService(intent)
    }

    private fun stopTracking()
    {
        val intent = Intent(this@UserRecognitionActivity, UserActivityBackgroundService::class.java)
        stopService(intent)
    }
}