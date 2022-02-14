package com.example.datasaverexampleapp.location

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.datasaverexampleapp.base_classes.BaseActivity
import com.example.datasaverexampleapp.location.geocoder.GeoCoderExampleActivity
import com.example.datasaverexampleapp.type_alias.Layout
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_location_example.*
import java.text.SimpleDateFormat
import java.util.*

class LocationMapActivity : BaseActivity(Layout.activity_location_example) {

    private var locationCallback:LocationCallback? = null
    private var locationClient:FusedLocationProviderClient? = null
    private var geofencingClient:GeofencingClient? = null
    private var geofencePendingIntent:PendingIntent? = null

    companion object {
        @JvmStatic
        val TAG = "LOCATION_EXAMPLE"
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Location Example"
        checkIfGooglePlayAPIiSAvailable()

        // Obtaining the current device location requires one of the two 'uses-permission' tags in your manifest,
        // specifying the degree of location accuracy you require, where:
        // FINE: represent high accuracy, and will enable you to receive the most accurate possible location
        //       to the maximum resolution supported by the hardware.
        // COARSE: represent low accuracy, limiting the resolution of returned location results to approximately a
        // city block
        // Add this to your manifest:
        // <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
        // <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
        //
        // Both location permission are marked as dangerous,meaning you need to check for - and if
        // necessary request, and be granted-runtime user permission before you can receive location
        // result.

        fine_location_test_button?.setOnClickListener {

            requestFineLocationPermission{ granted ->

                if (granted)
                {
                    Toast.makeText(this, "Permission granted",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission not granted",Toast.LENGTH_SHORT).show()
                }
            }
        }

        get_fused_location_button?.setOnClickListener {

            getLastLocation()
        }

        update_location_button?.apply {

            setOnClickListener {

                // The 'requestLocationUpdates' method accepts a 'LocationRequest' object that provides information
                // the fused Location Provider uses to determine the most efficient way to return results
                // at the level of accuracy and precision required.
                //
                // To optimize efficiency and minimize cost and power use, you can specify a number of criteria based
                // on your application needs:
                // - 'setPriority': Allows you to indicate the relative importance of reducing battery drain and getting
                //                  accurate results, using one of the following constants:
                //
                //   * PRIORITY_HIGH_ACCURACY:           Indicates that high accuracy is the priority. As a result the FLP
                //                                       will attempt to obtain the most precise location possible at a cost
                //                                       of increased battery drain. This can return results accurate to within
                //                                       a few feet and is typically used for mapping and navigation apps.
                //
                //   * PRIORITY_BALANCED_POWER_ACCURACY: Attempts to balance accuracy and power drain, resulting in precision to
                //                                       within a city block or approximately 100 meters.
                //
                //   * PRIORITY_LOW_POWER:               Indicates that low battery drain is the priority. As a result, coarse location
                //                                       updates at city-level precision of approximately 10 kilometers are acceptable.
                //
                //   * PRIORITY_NO_POWER:                Indicates that your app should not trigger location updates, but should receive
                //                                       location updates caused of other apps.
                //
                // - 'setInterval':        Your preferred rate of updates in milliseconds. This will force the Location Service to attempt to update
                //                         the location at this rate. Updates may be less frequent if it is unable to determine the location, or more
                //                         frequent if other applications are receiving updates more often.
                //
                // - 'setFastestInterval': The fastest update rate your application can support. Specify this if more frequent updates may cause
                //                         UI issues or data overflow within your app.

                if (locationClient == null)
                    getLastLocation()

                if (this.text == "Start update location")
                {
                    if (locationCallback == null)
                    {
                        // You should disable updates whenever possible in your application, especially
                        // in cases where your application isn't visible and Location updates are used
                        // only to update an Activity's UI. You can improve performance further by making
                        // the minimum time and distance between updates as large as possible.
                        locationCallback = object : LocationCallback(){

                            override fun onLocationResult(result: LocationResult) {
                                super.onLocationResult(result)

                                Log.i(TAG,"onLocationResult: ${result.locations}")

                                for (location in result.locations)
                                {
                                    longitude_textview?.text = location.longitude.toString()
                                    latitude_textview?.text = location.latitude.toString()
                                    altitude_textview?.text = location.altitude.toString()

                                    try {
                                        val sdf = SimpleDateFormat("EEE, MMM d, ''yy h:mm:ss a")
                                        val timeString = sdf.format(Date(location.time))
                                        time_textview?.text = timeString
                                    }catch (e:Exception)
                                    {
                                        Log.i(TAG,"Error formatting time, reason: ${e.message}")
                                        time_textview?.text = "-"
                                    }
                                }
                            }
                        }

                        // Update location
                        val locationRequest = LocationRequest.create()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setInterval(5000) // Update every 5 seconds


                        // Check if location settings are compatible with our location request
                        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

                        // Get the settings client
                        val settingsClient = LocationServices.getSettingsClient(this@LocationMapActivity)

                        // Check if the location settings satisfy our requirement
                        settingsClient.checkLocationSettings(builder.build()).apply {

                            addOnSuccessListener(this@LocationMapActivity){
                                // Location settings satisfy the requirements of the location Request.
                                // Request location updates.
                                startUpdateLocation(locationRequest)
                            }

                            addOnFailureListener(this@LocationMapActivity){ exception ->

                                // Extract the status code for the failure from within the Exception
                                if (exception is ApiException)
                                {
                                    when(exception.statusCode)
                                    {
                                        CommonStatusCodes.RESOLUTION_REQUIRED -> {

                                            if (exception is ResolvableApiException)
                                            {
                                                try {
                                                    // Display a user dialog to resolve the location settings issue
                                                    registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){ result ->

                                                        result.data?.let { data ->

                                                            val states = LocationSettingsStates.fromIntent(data)

                                                            when(result.resultCode)
                                                            {
                                                                Activity.RESULT_OK -> {
                                                                    // Requested changes made, request location updates
                                                                    startUpdateLocation(locationRequest)
                                                                }

                                                                Activity.RESULT_CANCELED -> {

                                                                    // Request changes were not made
                                                                    Log.i(TAG,"Requested settings changes declined by user")

                                                                    // Check if any location services are available, and if so request location updates
                                                                    states?.let { state ->

                                                                        if (state.isLocationUsable)
                                                                        {
                                                                            startUpdateLocation(locationRequest)
                                                                        } else {
                                                                            Log.i(TAG,"No location services available")
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }.launch(IntentSenderRequest.Builder(exception.resolution).build())

                                                }catch (exception: IntentSender.SendIntentException)
                                                {
                                                    Log.i(TAG,"Intent sender failed due to: ${exception.message}")
                                                }
                                            }
                                        }

                                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                                            // Location settings issues can't be resolved by user.
                                            // Request location update anyway
                                            Log.i(TAG,"Location settings can't be resolved")
                                            startUpdateLocation(locationRequest)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    this.text = "Stop update location"

                } else if (this.text == "Stop update location") {

                    stopUpdatingLocation()
                    locationCallback = null
                    this.text = "Start update location"
                }
            }
        }

        geofence_button?.apply {

            setOnClickListener {

                if (text == "Add geofence")
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    {
                        requestPermissions(arrayOf(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION, ACCESS_FINE_LOCATION)){ granted ->

                            if (granted)
                                addGeofence()
                        }

                    } else {

                        requestFineLocationPermission { granted ->

                            if (granted)
                                addGeofence()
                        }
                    }
                } else if (text == "Remove geofence")
                {
                    removeGeofence()
                }
            }
        }

        geocoder_example_button?.setOnClickListener {
            val intent = Intent(this, GeoCoderExampleActivity::class.java)
            startActivity(intent)
        }
    }

    private fun removeGeofence()
    {
        geofencePendingIntent?.let { geofencingClient?.removeGeofences(it) }
        geofence_status?.text = "Geofence"
        geofence_button?.text = "Add geofence"
    }

    @SuppressLint("MissingPermission")
    private fun addGeofence()
    {
        geofencingClient = LocationServices.getGeofencingClient(this@LocationMapActivity)
        val newGeoFence = Geofence.Builder()
            .setRequestId(TAG)
            .setCircularRegion(51.4462131,
                3.57172,
                30F) // 30 meter radius
            .setExpirationDuration(Geofence.NEVER_EXPIRE) // Time in millisecond or never expire
            .setLoiteringDelay(10*100) // dwell after 10 seconds
            .setNotificationResponsiveness(10*100) // notify within 10 second. In case you want improve battery consumption,
            // set the notification responsiveness to as slow a value as possible, and increase the size of your Geofence radius to at least 150 meters
            // reducing the need to for the device to check its location
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL)
            .build()

        val geofenceRequest = GeofencingRequest.Builder()
            .addGeofence(newGeoFence)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL)
            .build()

        val intent = Intent(this@LocationMapActivity, GeofencingReceiver::class.java)
        geofencePendingIntent = PendingIntent.getBroadcast(this@LocationMapActivity, 0,intent, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE else PendingIntent.FLAG_UPDATE_CURRENT)

        geofencePendingIntent?.let {

            geofencingClient?.addGeofences(geofenceRequest, it)?.run {

                addOnSuccessListener {
                    geofence_status?.text = "Geofence added!"
                    geofence_button?.text = "Remove geofence"
                }

                addOnFailureListener {

                    it.message?.let {

                        when(it.trim().replace(":",""))
                        {
                            GeofenceStatusCodes.GEOFENCE_INSUFFICIENT_LOCATION_PERMISSION.toString() -> {
                                geofence_status?.text = "Insufficient location permission to perform geofencing operations"
                            }

                            GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE.toString() -> {
                                geofence_status?.text = "Geofence service is not available now"
                            }

                            GeofenceStatusCodes.GEOFENCE_REQUEST_TOO_FREQUENT.toString() -> {
                                geofence_status?.text = "Your app has been adding Geofences too frequently."
                            }

                            GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES.toString() -> {
                                geofence_status?.text = "Your app has registered more than 100 geofences."
                            }

                            GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS.toString() -> {
                                geofence_status?.text = "You have provided more than 5 different PendingIntents to the addGeofences call"
                            }
                        }

                        Log.i(TAG,"Failed adding geofence, reason: $it")

                    }?: kotlin.run {
                        geofence_status?.text = "Adding geofence failed"
                        Log.i(TAG,"Failed adding geofence, reason: ${it.message} ${it.cause}")
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation()
    {
        requestFineLocationPermission{ granted ->

            if (granted)
            {
                LocationServices.getFusedLocationProviderClient(this).apply {
                    locationClient = this
                    lastLocation.addOnSuccessListener {

                        // In some rare situation this can be null
                        it?.let { location ->

                            longitude_textview?.text = location.longitude.toString()
                            latitude_textview?.text = location.latitude.toString()
                            altitude_textview?.text = location.altitude.toString()

                            try {

                                val sdf = SimpleDateFormat("EEE, MMM d, ''yy h:mm a")
                                val timeString = sdf.format(Date(location.time))
                                time_textview?.text = timeString
                            }catch (e:Exception)
                            {
                                Log.i(TAG,"Error formatting time, reason: ${e.message}")
                                time_textview?.text = "-"
                            }
                        }?: kotlin.run {
                            longitude_textview?.text = "nothing available at the moment"
                            latitude_textview?.text = "nothing available at the moment"
                            altitude_textview?.text = "nothing available at the moment"
                            time_textview?.text = "nothing available at the moment"
                        }
                    }.addOnFailureListener{ exception ->

                        Toast.makeText(this@LocationMapActivity,"Failed to retrieve location, reason: $exception",Toast.LENGTH_SHORT).show()
                        longitude_textview?.text = "-"
                        latitude_textview?.text = "-"
                        altitude_textview?.text = "-"
                        time_textview?.text = "-"
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startUpdateLocation(locationRequest:LocationRequest)
    {
        if (isPermissionGranted(ACCESS_FINE_LOCATION))
        {
            locationCallback?.let{
                locationClient?.requestLocationUpdates(locationRequest,it, Looper.getMainLooper())
            }
        }
    }

    private fun stopUpdatingLocation()
    {
        locationCallback?.let{
            locationClient?.removeLocationUpdates(it)
        }
    }

    override fun onStop() {
        super.onStop()
        stopUpdatingLocation()
    }

    private fun requestFineLocationPermission(callback:(Boolean) -> Unit)
    {
        // Request the fine location permission at runtime
        requestPermission(ACCESS_FINE_LOCATION){ granted ->
            callback(granted)
        }
    }

    private fun checkIfGooglePlayAPIiSAvailable()
    {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val result = googleApiAvailability.isGooglePlayServicesAvailable(this)

        if (result != ConnectionResult.SUCCESS)
        {
            if (!googleApiAvailability.isUserResolvableError(result)) // Determines whether an error can be resolved via user action.
            {
                Log.i(TAG,"Google play services api is not available")
            } else {
                Log.i(TAG,"Google play services api is available")
            }
        } else if (result == ConnectionResult.SUCCESS){
            Log.i(TAG,"Google play API connection result success")
        }
    }
}
