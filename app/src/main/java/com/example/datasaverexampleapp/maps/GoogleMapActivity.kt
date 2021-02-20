package com.example.datasaverexampleapp.maps

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.datasaverexampleapp.base_classes.BaseActivity
import com.example.datasaverexampleapp.type_alias.Layout
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_google_map.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class GoogleMapActivity : BaseActivity(Layout.activity_google_map) {

    private var locationCallback:LocationCallback? = null
    private var locationClient:FusedLocationProviderClient? = null


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Google maps Example"
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

                                Log.i("GOOGLE_MAP","onLocationResult: ${result.locations}")

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
                                        Log.i("GOOGLE_MAP","Error formatting time, reason: ${e.message}")
                                        time_textview?.text = "-"
                                    }
                                }
                            }
                        }

                        // Update location
                        val locationRequest = LocationRequest.create()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setInterval(5000) // Update every 5 seconds

                        locationCallback?.let{
                            locationClient?.requestLocationUpdates(locationRequest,it, null)
                        }
                    }

                    this.text = "Stop update location"

                } else if (this.text == "Stop update location") {

                    locationCallback?.let{
                        locationClient?.removeLocationUpdates(it)
                    }
                    locationCallback = null
                    this.text = "Start update location"
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
                                Log.i("GOOGLE_MAP","Error formatting time, reason: ${e.message}")
                                time_textview?.text = "-"
                            }
                        }?: kotlin.run {
                            longitude_textview?.text = "nothing available at the moment"
                            latitude_textview?.text = "nothing available at the moment"
                            altitude_textview?.text = "nothing available at the moment"
                            time_textview?.text = "nothing available at the moment"
                        }
                    }.addOnFailureListener{ exception ->

                        Toast.makeText(this@GoogleMapActivity,"Failed to retrieve location, reason: $exception",Toast.LENGTH_SHORT).show()
                        longitude_textview?.text = "-"
                        latitude_textview?.text = "-"
                        altitude_textview?.text = "-"
                        time_textview?.text = "-"
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        locationCallback?.let{
            locationClient?.removeLocationUpdates(it)
        }
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
                Log.i("GOOGLE_MAP","Google play services api is not available")
            } else {
                Log.i("GOOGLE_MAP","Google play services api is available")
            }
        } else if (result == ConnectionResult.SUCCESS){
            Log.i("GOOGLE_MAP","Google play API connection result success")
        }
    }
}