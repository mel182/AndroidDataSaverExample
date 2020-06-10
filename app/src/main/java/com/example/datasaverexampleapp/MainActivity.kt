package com.example.datasaverexampleapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.ConnectivityManagerCompat.*
import com.example.datasaverexampleapp.battery.Battery
import com.example.datasaverexampleapp.inDefInterfaces.Constants
import com.example.datasaverexampleapp.inDefInterfaces.Shape
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigInteger


@RequiresApi(Build.VERSION_CODES.N)
class MainActivity : AppCompatActivity() {

    // It delay invoking an expression until its value is needed and caches the value to avoid repeated evaluation
    val lazyValue by lazy(LazyThreadSafetyMode.PUBLICATION) {
        BigInteger.valueOf(2).modPow(BigInteger.valueOf(7),BigInteger.valueOf(20))
    }

    private var dataSaverBroadcastReceiver:BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkActiveNetwork()
        registerBackGroundRestrictedChangeBroadcastReceiver()


        private_mode_button.setOnClickListener {

            val filename = "private_mode_file"
            val name = "Private mode file"

            val fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE)
            fileOutputStream.write(name.toByteArray())
            fileOutputStream.close()

            Toast.makeText(this,"File saved in private mode",Toast.LENGTH_SHORT).show()
        }

        append_mode_button.setOnClickListener {

            val filename = "append_mode_file"
            val name = "Append mode file"

            val fileOutputStream = openFileOutput(filename, Context.MODE_APPEND)
            fileOutputStream.write(name.toByteArray())
            fileOutputStream.close()

            Toast.makeText(this,"File saved in append mode",Toast.LENGTH_SHORT).show()
        }

        battery_state_button.setOnClickListener {
            Battery.registerBatteryStatus(this)
        }

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager


        wifi_check.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                val networkCapabilities = connectivityManager.activeNetwork
                val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities)

                activeNetwork?.let { networkCapability ->

                    when
                    {
                        networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {

                            val wifiInfo = wifiManager.connectionInfo
                            val speedMbps = wifiInfo.linkSpeed

                            wifi_info_txt.text = "Wifi connected\nSpeed info (Mbps): $speedMbps"

                        }

                        networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            wifi_info_txt.text = "Cellular transport connected!"
                        }

                        networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            wifi_info_txt.text = "Transport ethernet connected!"
                        }

                        networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> {
                            wifi_info_txt.text = "Transport bluetooth connected!"
                        }
                    }
                }

            } else {
                val activeNetwork = connectivityManager.activeNetworkInfo
                val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

                activeNetwork?.let {
                    val isWifi = it.type == ConnectivityManager.TYPE_WIFI

                    if (isWifi && isConnected)
                    {
                        wifi_info_txt.text = "Wifi connected!"
                    }
                }
            }
        }
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


    //An alternative for memory efficiency for enumerations.
    // Since enums consume a lof of resources since it has to it create an object for each enum option.
    // To make matters worse, the enumeration needs to replicate itself in every process the app is using it.
    // Such operation cost a lot of memory resource in a multiprocess application.

    fun setShape(@Shape mode: Int) { // Shape interface class

        when(mode){
            Constants.RECTANGLE -> println("Rectangle")
            Constants.TRIANGLE -> println("Triangle")
            Constants.CIRCLE -> println("Circle")
            Constants.SQUARE -> println("Square")
        }
    }
}
