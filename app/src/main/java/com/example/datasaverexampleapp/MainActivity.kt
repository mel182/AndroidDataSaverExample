package com.example.datasaverexampleapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.net.ConnectivityManagerCompat.*
import kotlin.math.log

@RequiresApi(Build.VERSION_CODES.N)
class MainActivity : AppCompatActivity() {

    private var dataSaverBroadcastReceiver:BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkActiveNetwork()
        registerBackGroundRestrictedChangeBroadcastReceiver()
    }

    private fun registerBackGroundRestrictedChangeBroadcastReceiver()
    {
        // Broadcast receiver for detecting background Data Saver restrictions change
        dataSaverBroadcastReceiver?.let {}?: kotlin.run {
            this.dataSaverBroadcastReceiver = object: BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    checkActiveNetwork()
                }
            }

            val intentFilter = IntentFilter(ConnectivityManager.ACTION_RESTRICT_BACKGROUND_CHANGED)
            registerReceiver(dataSaverBroadcastReceiver,intentFilter)
        }
    }


    private fun checkActiveNetwork()
    {
        // Check if the active network is metered and determine the restrictions on this app by the user.
        val connectionManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val status = connectionManager.restrictBackgroundStatus

        Log.i("TAG","Get restricted background status: $status")

        //Check if the active network is metered one
        if (connectionManager.isActiveNetworkMetered)
        {
            Log.i("TAG","Active network is metered")
            // Checks user's Data saver preference
            when(connectionManager.restrictBackgroundStatus)
            {
                RESTRICT_BACKGROUND_STATUS_ENABLED -> {
                    // Data saver is enabled and the application shouldn't use the network in background
                    Log.i("TAG","Restricted background status enabled")}
                RESTRICT_BACKGROUND_STATUS_WHITELISTED -> {
                    //Data saver is enabled, but the application is whitelisted. The application should limit the
                    // network request while the Data Saver is enabled even if the application is whitelisted
                    Log.i("TAG","Restricted background status whitelisted")}
                RESTRICT_BACKGROUND_STATUS_DISABLED -> {
                    // Data Saver is disabled
                    Log.i("TAG","Restricted background status disabled")
                }
            }
        } else {
            // Active network is not metered so any network request can be done
            Log.i("TAG","The active network is not metered")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Unregister data saver broadcast receiver
        if (dataSaverBroadcastReceiver != null)
            unregisterReceiver(dataSaverBroadcastReceiver)
    }
}
