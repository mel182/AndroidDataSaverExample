package com.example.datasaverexampleapp.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofencingReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        intent?.let {

            val geofencingEvent = GeofencingEvent.fromIntent(it)

            if (geofencingEvent.hasError())
            {
                val errorCode = geofencingEvent.errorCode
                val errorMessage = GeofenceStatusCodes.getStatusCodeString(errorCode)

                Log.i(LocationMapActivity.TAG,"Geofencing error: ${errorMessage}")
            } else {

                // Get the transition type
                val geofencingTransition = geofencingEvent.geofenceTransition

                when(geofencingTransition)
                {
                    Geofence.GEOFENCE_TRANSITION_DWELL -> {
                        Log.i(LocationMapActivity.TAG, " Transition type: Geofence transition dwell")
                    }

                    Geofence.GEOFENCE_TRANSITION_ENTER -> {
                        Log.i(LocationMapActivity.TAG, " Transition type: Geofence transition enter")
                    }

                    Geofence.GEOFENCE_TRANSITION_EXIT -> {
                        Log.i(LocationMapActivity.TAG, " Transition type: Geofence transition exit")
                    }

                    else -> {

                        if (geofencingTransition.toLong() == Geofence.NEVER_EXPIRE)
                        {
                            Log.i(LocationMapActivity.TAG, " Transition type: Geofence never expire")
                        }
                    }
                }

                // A single even van trigger multiple geofences
                // Get the geofences that were triggered
                val triggeredGeofences = geofencingEvent.triggeringGeofences

                for (geofencing in triggeredGeofences)
                {
                    Log.i(LocationMapActivity.TAG,"Geofencing triggered: ${geofencing}")
                }
            }
        }




    }
}