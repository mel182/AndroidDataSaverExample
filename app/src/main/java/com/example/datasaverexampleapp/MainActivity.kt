package com.example.datasaverexampleapp

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.*
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.ConnectivityManagerCompat.*
import com.example.datasaverexampleapp.animation.AnimationExampleActivity
import com.example.datasaverexampleapp.appbar.AppBarActivity
import com.example.datasaverexampleapp.appbar.app_bar_bottom_navigation.AppBarBottomNavigationActivity
import com.example.datasaverexampleapp.appbar.drawer_menu.DrawerMenuActivity
import com.example.datasaverexampleapp.appbar.menu.AppBarMenuExampleActivity
import com.example.datasaverexampleapp.appbar.scrolling_techniques.AppBarCollapsingToolbarExampleActivity
import com.example.datasaverexampleapp.appbar.scrolling_techniques.AppBarCollapsingToolbarWithImageExampleActivity
import com.example.datasaverexampleapp.appbar.scrolling_techniques.AppBarToolbarOffscreenActivity
import com.example.datasaverexampleapp.appbar.searchview.SearchViewActivity
import com.example.datasaverexampleapp.appbar.tabs.AppBarTabsActivity
import com.example.datasaverexampleapp.background_task.WorkManagerActivity
import com.example.datasaverexampleapp.battery.Battery
import com.example.datasaverexampleapp.battery.PowerConnectionReceiver
import com.example.datasaverexampleapp.cardview.CardViewActivity
import com.example.datasaverexampleapp.concurrency.CoroutineExampleActivity
import com.example.datasaverexampleapp.concurrency.FlowExampleActivity
import com.example.datasaverexampleapp.content_provider.ContentProviderActivity
import com.example.datasaverexampleapp.custom_view.CustomViewActivity
import com.example.datasaverexampleapp.custom_view.CustomViewExampleActivity
import com.example.datasaverexampleapp.data_binding.DataBindingTestActivity
import com.example.datasaverexampleapp.data_binding.earthQuakeExample.EarthQuakeDataBindingExampleActivity
import com.example.datasaverexampleapp.dialog.DialogExampleActivity
import com.example.datasaverexampleapp.earth_quake_example.EarthQuakeActivity
import com.example.datasaverexampleapp.firebase_real_time.FireBaseRealTimeActivity
import com.example.datasaverexampleapp.fullscreen_example.FullScreenExampleActivity
import com.example.datasaverexampleapp.inDefInterfaces.Constants
import com.example.datasaverexampleapp.inDefInterfaces.Shape
import com.example.datasaverexampleapp.intent_example.IntentActivity
import com.example.datasaverexampleapp.mvvm_coroutine_flow_livedata.MvvMCoroutinesActivity
import com.example.datasaverexampleapp.notification.NotificationActivity
import com.example.datasaverexampleapp.protocol_oriented_programming_kotlin.ProtocolOrientedActivity
import com.example.datasaverexampleapp.resourceTest.ResourceActivity
import com.example.datasaverexampleapp.room_db.RoomDBActivity
import com.example.datasaverexampleapp.snackbar.SnackBarActivity
import com.example.datasaverexampleapp.speech_recognition_example.SpeechRecognitionActivity
import com.example.datasaverexampleapp.storage_manager.StorageManagerExampleActivity
import com.example.datasaverexampleapp.text_to_speech_example.TextToSpeechActivity
import com.example.datasaverexampleapp.vibration.VibrationExampleActivity
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

//        val displayMetrics = DisplayMetrics()
//        windowManager.defaultDisplay.getMetrics(displayMetrics)
//
//        var width = displayMetrics.widthPixels
//        var height = displayMetrics.heightPixels
//
//        Log.i("TAG","Width: ${width}")
//        Log.i("TAG","Height: ${height}")
//        Log.i("TAG","Height 2: ${height/2}")

//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.i("TAG", "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//
//            // Log and toast
////            val msg = getString(R.string.msg_token_fmt, token)
//            Log.i("TAG", "Token: ${token}")
//            Toast.makeText(baseContext, "Token created!", Toast.LENGTH_SHORT).show()
//        })

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

                    if (isConnected)
                    {
                        val wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                        val mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)


                        if (wifi.isAvailable)
                        {
                            wifi_info_txt.text = "Wifi connected!"
                        } else if (mobile.isAvailable)
                        {
                            wifi_info_txt.text = "Mobile connected!"
                        }

                        if (wifi.isAvailable || mobile.isAvailable)
                        {
                            // Perform task
                        }
                    } else {
                        wifi_info_txt.text = "Not Connected!"
                    }
                }
            }
        }

        val receiver = ComponentName(applicationContext, PowerConnectionReceiver::class.java)

        val packageManager = packageManager

        // Does not work with devices with Android N and above since the broadcast need to be registered in the manifest
        broadcast_toggle.setOnClickListener {

            if (it.isActivated)
            {
                packageManager.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP)

                broadcast_toggle.text = "Broadcast disabled"

            } else {
                packageManager.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP)

                broadcast_toggle.text = "Broadcast enabled"
            }
        }

        earth_quake.setOnClickListener {
            val intent = Intent(this,EarthQuakeActivity::class.java)
            startActivity(intent)
        }

        resource_test?.setOnClickListener {
            val resourceTestActivityIntent = Intent(this,ResourceActivity::class.java)
            startActivity(resourceTestActivityIntent)
        }

        animation_test?.setOnClickListener {
            val intent = Intent(this, AnimationExampleActivity::class.java)
            startActivity(intent)
        }

        data_binding_test?.setOnClickListener {
            val intent = Intent(this, DataBindingTestActivity::class.java)
            startActivity(intent)
        }

        data_binding_list_example?.setOnClickListener {
            val intent = Intent(this, EarthQuakeDataBindingExampleActivity::class.java)
            startActivity(intent)
        }

        compound_view_control_example?.setOnClickListener {
            val intent = Intent(this, CustomViewActivity::class.java)
            startActivity(intent)
        }

        custom_view_example?.setOnClickListener {
            val intent = Intent(this, CustomViewExampleActivity::class.java)
            startActivity(intent)
        }

        intent_test?.setOnClickListener {
            val intent = Intent(this, IntentActivity::class.java)
            startActivity(intent)
        }

        // ------------------------ Check for intent for deep link -----------------------------------
        intent?.let {
            val data = it.data
            data?.let { dataUri ->
                Toast.makeText(this, "data: ${dataUri}", Toast.LENGTH_SHORT).show()

                if (dataUri.toString() == "http://example.com")
                {
                    val intent = Intent(this, IntentActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        // -------------------------------------------------------------------------------------------

        coroutine_example?.setOnClickListener {
            val intent = Intent(this, CoroutineExampleActivity::class.java)
            startActivity(intent)
        }

        flow_example.setOnClickListener {
            val intent = Intent(this, FlowExampleActivity::class.java)
            startActivity(intent)
        }

        mvvm_coroutines_flow?.setOnClickListener {
            val intent = Intent(this, MvvMCoroutinesActivity::class.java)
            startActivity(intent)
        }

        storage_manager_example?.setOnClickListener {
            val intent = Intent(this, StorageManagerExampleActivity::class.java)
            startActivity(intent)
        }

        room_database_example?.setOnClickListener {
            val intent = Intent(this, RoomDBActivity::class.java)
            startActivity(intent)
        }

        protocol_oriented_example?.setOnClickListener {
            val intent = Intent(this, ProtocolOrientedActivity::class.java)
            startActivity(intent)
        }

        firebase_real_time_database_example?.setOnClickListener {
            val intent = Intent(this, FireBaseRealTimeActivity::class.java)
            startActivity(intent)
        }

        content_provider_example.setOnClickListener {
            val intent = Intent(this, ContentProviderActivity::class.java)
            startActivity(intent)
        }

        background_task_example.setOnClickListener {
            val intent = Intent(this, WorkManagerActivity::class.java)
            startActivity(intent)
        }

        notification_example.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        app_bar_example.setOnClickListener {
            val intent = Intent(this, AppBarActivity::class.java)
            startActivity(intent)
        }

        card_view_example?.setOnClickListener {
            val intent = Intent(this, CardViewActivity::class.java)
            startActivity(intent)
        }

        app_bar_menu_example?.setOnClickListener {
            val intent = Intent(this, AppBarMenuExampleActivity::class.java)
            startActivity(intent)
        }

        searchview_example?.setOnClickListener {
            val intent = Intent(this, SearchViewActivity::class.java)
            startActivity(intent)
        }

        toolbar_offscreen_example?.setOnClickListener {
            val intent = Intent(this, AppBarToolbarOffscreenActivity::class.java)
            startActivity(intent)
        }

        collapse_toolbar_example?.setOnClickListener {
            val intent = Intent(this, AppBarCollapsingToolbarExampleActivity::class.java)
            startActivity(intent)
        }

        collapse_toolbar_with_image_example?.setOnClickListener {
            val intent = Intent(this, AppBarCollapsingToolbarWithImageExampleActivity::class.java)
            startActivity(intent)
        }

        app_bar_with_tabs?.setOnClickListener {
            val intent = Intent(this, AppBarTabsActivity::class.java)
            startActivity(intent)
        }


        app_bar_bottom_navigation?.setOnClickListener {
            val intent = Intent(this, AppBarBottomNavigationActivity::class.java)
            startActivity(intent)
        }

        drawer_menu_example?.setOnClickListener {
            val intent = Intent(this, DrawerMenuActivity::class.java)
            startActivity(intent)
        }

        dialog_example?.setOnClickListener {
            val intent = Intent(this, DialogExampleActivity::class.java)
            startActivity(intent)
        }

        snack_bar_example?.setOnClickListener {
            val intent = Intent(this, SnackBarActivity::class.java)
            startActivity(intent)
        }

        text_to_speech_example?.setOnClickListener {
            val intent = Intent(this, TextToSpeechActivity::class.java)
            startActivity(intent)
        }

        speech_recognition_example?.setOnClickListener {
            val intent = Intent(this, SpeechRecognitionActivity::class.java)
            startActivity(intent)
        }

        vibration_example?.setOnClickListener {
            val intent = Intent(this, VibrationExampleActivity::class.java)
            startActivity(intent)
        }

        full_screen_example?.setOnClickListener {
            val intent = Intent(this, FullScreenExampleActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        val myProcess = RunningAppProcessInfo()
        ActivityManager.getMyMemoryState(myProcess)

        Log.i("TAG","last trim level: ${myProcess.lastTrimLevel}") // To get time level
        Log.i("TAG","Process importance: ${myProcess.importance}")

        // For API level support for 14 and lower, use the 'onLowMemory' handler as a fallback that's roughly equivalent to the TRIM_MEMORY_COMPLETE level.
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
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

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        // Application is a candidate for termination
        if (level >= ComponentCallbacks2.TRIM_MEMORY_COMPLETE)
        {
            // Release all possible resource to avoid immediate termination
        } else if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE)
        {
            // Releasing resources now will and make your app less likely
            // to be terminated
        } else if (level >= ComponentCallbacks2.TRIM_MEMORY_BACKGROUND)
        {
            // Release resources that are easy to recover
            // Application is no longer visible
        } else if (level >= ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN)
        {
            // Your application no longer has any visible UI. Free any resources
            // associated with maintaining your UI.
            // Application is running and not a candidate for termination
        } else if (level >= ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL)
        {
            // The system will now begin killing background processes
            // Release non-critical resources now to prevent performance degradation
            // and reduce the chance of other apps being terminated
        } else if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE)
        {
            // Release resources here to alleviate system memory pressure and improve overall system performance
        } else if (level >= ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW)
        {
            // The system is beginning to feel memory pressure.
        }
    }
}
