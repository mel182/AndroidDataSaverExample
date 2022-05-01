package com.example.datasaverexampleapp

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.NotificationManager
import android.content.*
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.ConnectivityManagerCompat.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.animation.AnimationExampleActivity
import com.example.datasaverexampleapp.animation.animator_set.AnimatorSetActivity
import com.example.datasaverexampleapp.animation.property.PropertyAnimationActivity
import com.example.datasaverexampleapp.appbar.AppBarActivity
import com.example.datasaverexampleapp.appbar.app_bar_bottom_navigation.AppBarBottomNavigationActivity
import com.example.datasaverexampleapp.appbar.drawer_menu.DrawerMenuActivity
import com.example.datasaverexampleapp.appbar.menu.AppBarMenuExampleActivity
import com.example.datasaverexampleapp.appbar.scrolling_techniques.AppBarCollapsingToolbarExampleActivity
import com.example.datasaverexampleapp.appbar.scrolling_techniques.AppBarCollapsingToolbarWithImageExampleActivity
import com.example.datasaverexampleapp.appbar.scrolling_techniques.AppBarToolbarOffscreenActivity
import com.example.datasaverexampleapp.appbar.searchview.SearchViewActivity
import com.example.datasaverexampleapp.appbar.tabs.AppBarTabsActivity
import com.example.datasaverexampleapp.async_recycler_view.AsyncRecyclerViewExampleActivity
import com.example.datasaverexampleapp.battery.Battery
import com.example.datasaverexampleapp.battery.PowerConnectionReceiver
import com.example.datasaverexampleapp.bluetooth.BluetoothExampleActivity
import com.example.datasaverexampleapp.camera.CameraExamplesActivity
import com.example.datasaverexampleapp.cardview.CardViewActivity
import com.example.datasaverexampleapp.concurrency.CoroutineExampleActivity
import com.example.datasaverexampleapp.concurrency.FlowExampleActivity
import com.example.datasaverexampleapp.content_provider.ContentProviderActivity
import com.example.datasaverexampleapp.copy_paste_example.CopyPasteExampleActivity
import com.example.datasaverexampleapp.custom_view.CustomViewActivity
import com.example.datasaverexampleapp.custom_view.CustomViewExampleActivity
import com.example.datasaverexampleapp.data_binding.DataBindingTestActivity
import com.example.datasaverexampleapp.data_binding.earthQuakeExample.EarthQuakeDataBindingExampleActivity
import com.example.datasaverexampleapp.databinding.ActivityMainBinding
import com.example.datasaverexampleapp.dialog.DialogExampleActivity
import com.example.datasaverexampleapp.drawing.DrawExampleActivity
import com.example.datasaverexampleapp.earth_quake_example.EarthQuakeActivity
import com.example.datasaverexampleapp.firebase_real_time.FireBaseRealTimeActivity
import com.example.datasaverexampleapp.fullscreen_example.FullScreenExampleActivity
import com.example.datasaverexampleapp.hardware_sensor.OptionDialogFragment
import com.example.datasaverexampleapp.implicit_delegation_example.ImplicitDelegationActivity
import com.example.datasaverexampleapp.inDefInterfaces.Constants
import com.example.datasaverexampleapp.inDefInterfaces.Shape
import com.example.datasaverexampleapp.intent_example.IntentActivity
import com.example.datasaverexampleapp.kotlin_flow_advance.KotlinFlowAdvanceExampleActivity
import com.example.datasaverexampleapp.location.LocationMapActivity
import com.example.datasaverexampleapp.location.maps.GoogleMapsActivity
import com.example.datasaverexampleapp.motion_layout.MotionLayoutExampleActivity
import com.example.datasaverexampleapp.mvvm_coroutine_flow_livedata.MvvMCoroutinesActivity
import com.example.datasaverexampleapp.notification.NotificationActivity
import com.example.datasaverexampleapp.paging.PagingExampleActivity
import com.example.datasaverexampleapp.preference_datastore.PreferenceDataStoreActivity
import com.example.datasaverexampleapp.protocol_oriented_programming_kotlin.ProtocolOrientedActivity
import com.example.datasaverexampleapp.recycler_view_item_animation.RecyclerViewItemAnimationActivity
import com.example.datasaverexampleapp.resourceTest.ResourceActivity
import com.example.datasaverexampleapp.room_db.RoomDBActivity
import com.example.datasaverexampleapp.shimmer_animation.ShimmerAnimationActivity
import com.example.datasaverexampleapp.snackbar.SnackBarActivity
import com.example.datasaverexampleapp.speech_recognition_example.SpeechRecognitionActivity
import com.example.datasaverexampleapp.storage_manager.StorageManagerExampleActivity
import com.example.datasaverexampleapp.text_to_speech_example.TextToSpeechActivity
import com.example.datasaverexampleapp.type_alias.Layout
import com.example.datasaverexampleapp.vibration.VibrationExampleActivity
import com.example.datasaverexampleapp.video_audio.VideoAudioStreamingActivity
import com.example.datasaverexampleapp.video_audio.foreground_service.ForegroundService
import com.example.datasaverexampleapp.wifi_p2p.Wifi_P2P_ExampleActivity
import com.example.datasaverexampleapp.work_manager.WorkManagerActivity
import java.math.BigInteger


@Suppress("DEPRECATION")
@RequiresApi(Build.VERSION_CODES.N)
class MainActivity : AppCompatActivity() {

    // It delay invoking an expression until its value is needed and caches the value to avoid repeated evaluation
    val lazyValue by lazy(LazyThreadSafetyMode.PUBLICATION) {
        BigInteger.valueOf(2).modPow(BigInteger.valueOf(7),BigInteger.valueOf(20))
    }

    private var dataSaverBroadcastReceiver:BroadcastReceiver? = null
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this, Layout.activity_main
        )

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

        binding?.apply {


            privateModeButton.setOnClickListener {

                val filename = "private_mode_file"
                val name = "Private mode file"

                val fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE)
                fileOutputStream.write(name.toByteArray())
                fileOutputStream.close()

                Toast.makeText(this@MainActivity,"File saved in private mode",Toast.LENGTH_SHORT).show()
            }

            appendModeButton.setOnClickListener {

                val filename = "append_mode_file"
                val name = "Append mode file"

                val fileOutputStream = openFileOutput(filename, Context.MODE_APPEND)
                fileOutputStream.write(name.toByteArray())
                fileOutputStream.close()

                Toast.makeText(this@MainActivity,"File saved in append mode",Toast.LENGTH_SHORT).show()
            }

            batteryStateButton.setOnClickListener {
                Battery.registerBatteryStatus(this@MainActivity)
            }

            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

            wifiCheck.setOnClickListener {

                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {

                        val request = NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()
                        val networkCallback = object : ConnectivityManager.NetworkCallback() {

                            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                                super.onCapabilitiesChanged(network, networkCapabilities)
                                val wifiInfo = networkCapabilities.transportInfo as WifiInfo
                                val speedMbps = wifiInfo.linkSpeed
                                wifiInfoTxt.text = "Wifi connected\nSpeed info (Mbps): $speedMbps"
                            }
                        }

                        getSystemService(ConnectivityManager::class.java).apply {
                            requestNetwork(request, networkCallback)
                            registerNetworkCallback(request, networkCallback)
                        }
                    }

                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                        val networkCapabilities = connectivityManager.activeNetwork
                        val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities)

                        activeNetwork?.let { networkCapability ->

                            when {
                                networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {

                                    val wifiInfo = wifiManager.connectionInfo
                                    val speedMbps = wifiInfo.linkSpeed

                                    wifiInfoTxt.text = "Wifi connected\nSpeed info (Mbps): $speedMbps"
                                }

                                networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                                    wifiInfoTxt.text = "Cellular transport connected!"
                                }

                                networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                                    wifiInfoTxt.text = "Transport ethernet connected!"
                                }

                                networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> {
                                    wifiInfoTxt.text = "Transport bluetooth connected!"
                                }
                            }
                        }

                    }
                    else -> {
                        val activeNetwork = connectivityManager.activeNetworkInfo
                        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

                        activeNetwork?.let {

                            if (isConnected) {
                                val wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                                val mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

                                if (wifi != null && mobile != null) {
                                    if (wifi.isAvailable) {
                                        wifiInfoTxt.text = "Wifi connected!"
                                    } else if (mobile.isAvailable) {
                                        wifiInfoTxt.text = "Mobile connected!"
                                    }

                                    if (wifi.isAvailable || mobile.isAvailable) {
                                        // Perform task
                                    }
                                }

                            } else {
                                wifiInfoTxt.text = "Not Connected!"
                            }
                        }
                    }
                }
            }

            val receiver = ComponentName(applicationContext, PowerConnectionReceiver::class.java)

            val packageManager = packageManager

            // Does not work with devices with Android N and above since the broadcast need to be registered in the manifest
            broadcastToggle.setOnClickListener {

                if (it.isActivated)
                {
                    packageManager.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP)

                    broadcastToggle.text = "Broadcast disabled"

                } else {
                    packageManager.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP)

                    broadcastToggle.text = "Broadcast enabled"
                }
            }

            earthQuake.setOnClickListener {
                val intent = Intent(this@MainActivity,EarthQuakeActivity::class.java)
                startActivity(intent)
            }

            resourceTest.setOnClickListener {
                val resourceTestActivityIntent = Intent(this@MainActivity,ResourceActivity::class.java)
                startActivity(resourceTestActivityIntent)
            }

            animationTest.setOnClickListener {
                val intent = Intent(this@MainActivity, AnimationExampleActivity::class.java)
                startActivity(intent)
            }

            dataBindingTest.setOnClickListener {
                val intent = Intent(this@MainActivity, DataBindingTestActivity::class.java)
                startActivity(intent)
            }

            dataBindingListExample.setOnClickListener {
                val intent = Intent(this@MainActivity, EarthQuakeDataBindingExampleActivity::class.java)
                startActivity(intent)
            }

            compoundViewControlExample.setOnClickListener {
                val intent = Intent(this@MainActivity, CustomViewActivity::class.java)
                startActivity(intent)
            }

            customViewExample.setOnClickListener {
                val intent = Intent(this@MainActivity, CustomViewExampleActivity::class.java)
                startActivity(intent)
            }

            intentTest.setOnClickListener {
                val intent = Intent(this@MainActivity, IntentActivity::class.java)
                startActivity(intent)
            }

            // ------------------------ Check for intent for deep link -----------------------------------
            intent?.let {
                val data = it.data
                data?.let { dataUri ->
                    Toast.makeText(this@MainActivity, "data: ${dataUri}", Toast.LENGTH_SHORT).show()

                    if (dataUri.toString() == "http://example.com")
                    {
                        val intent = Intent(this@MainActivity, IntentActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
            // -------------------------------------------------------------------------------------------

            coroutineExample.setOnClickListener {
                val intent = Intent(this@MainActivity, CoroutineExampleActivity::class.java)
                startActivity(intent)
            }

            flowExample.setOnClickListener {
                val intent = Intent(this@MainActivity, FlowExampleActivity::class.java)
                startActivity(intent)
            }

            mvvmCoroutinesFlow.setOnClickListener {
                val intent = Intent(this@MainActivity, MvvMCoroutinesActivity::class.java)
                startActivity(intent)
            }

            storageManagerExample.setOnClickListener {
                val intent = Intent(this@MainActivity, StorageManagerExampleActivity::class.java)
                startActivity(intent)
            }

            roomDatabaseExample.setOnClickListener {
                val intent = Intent(this@MainActivity, RoomDBActivity::class.java)
                startActivity(intent)
            }

            protocolOrientedExample.setOnClickListener {
                val intent = Intent(this@MainActivity, ProtocolOrientedActivity::class.java)
                startActivity(intent)
            }

            firebaseRealTimeDatabaseExample.setOnClickListener {
                val intent = Intent(this@MainActivity, FireBaseRealTimeActivity::class.java)
                startActivity(intent)
            }

            contentProviderExample.setOnClickListener {
                val intent = Intent(this@MainActivity, ContentProviderActivity::class.java)
                startActivity(intent)
            }

            backgroundTaskExample.setOnClickListener {
                val intent = Intent(this@MainActivity, WorkManagerActivity::class.java)
                startActivity(intent)
            }

            notificationExample.setOnClickListener {
                val intent = Intent(this@MainActivity, NotificationActivity::class.java)
                startActivity(intent)
            }

            appBarExample.setOnClickListener {
                val intent = Intent(this@MainActivity, AppBarActivity::class.java)
                startActivity(intent)
            }

            cardViewExample.setOnClickListener {
                val intent = Intent(this@MainActivity, CardViewActivity::class.java)
                startActivity(intent)
            }

            appBarMenuExample.setOnClickListener {
                val intent = Intent(this@MainActivity, AppBarMenuExampleActivity::class.java)
                startActivity(intent)
            }

            searchviewExample.setOnClickListener {
                val intent = Intent(this@MainActivity, SearchViewActivity::class.java)
                startActivity(intent)
            }

            toolbarOffscreenExample.setOnClickListener {
                val intent = Intent(this@MainActivity, AppBarToolbarOffscreenActivity::class.java)
                startActivity(intent)
            }

            collapseToolbarExample.setOnClickListener {
                val intent = Intent(this@MainActivity, AppBarCollapsingToolbarExampleActivity::class.java)
                startActivity(intent)
            }

            collapseToolbarWithImageExample.setOnClickListener {
                val intent = Intent(this@MainActivity, AppBarCollapsingToolbarWithImageExampleActivity::class.java)
                startActivity(intent)
            }

            appBarWithTabs.setOnClickListener {
                val intent = Intent(this@MainActivity, AppBarTabsActivity::class.java)
                startActivity(intent)
            }

            appBarBottomNavigation.setOnClickListener {
                val intent = Intent(this@MainActivity, AppBarBottomNavigationActivity::class.java)
                startActivity(intent)
            }

            drawerMenuExample.setOnClickListener {
                val intent = Intent(this@MainActivity, DrawerMenuActivity::class.java)
                startActivity(intent)
            }

            dialogExample.setOnClickListener {
                val intent = Intent(this@MainActivity, DialogExampleActivity::class.java)
                startActivity(intent)
            }

            snackBarExample.setOnClickListener {
                val intent = Intent(this@MainActivity, SnackBarActivity::class.java)
                startActivity(intent)
            }

            textToSpeechExample.setOnClickListener {
                val intent = Intent(this@MainActivity, TextToSpeechActivity::class.java)
                startActivity(intent)
            }

            speechRecognitionExample.setOnClickListener {
                val intent = Intent(this@MainActivity, SpeechRecognitionActivity::class.java)
                startActivity(intent)
            }

            vibrationExample.setOnClickListener {
                val intent = Intent(this@MainActivity, VibrationExampleActivity::class.java)
                startActivity(intent)
            }

            fullScreenExample.setOnClickListener {
                val intent = Intent(this@MainActivity, FullScreenExampleActivity::class.java)
                startActivity(intent)
            }

            propertyAnimationExample.setOnClickListener {
                val intent = Intent(this@MainActivity, PropertyAnimationActivity::class.java)
                startActivity(intent)
            }

            animatorSetExample.setOnClickListener {
                val intent = Intent(this@MainActivity, AnimatorSetActivity::class.java)
                startActivity(intent)
            }

            drawExamples.setOnClickListener {
                val intent = Intent(this@MainActivity, DrawExampleActivity::class.java)
                startActivity(intent)
            }

            copyPasteExamples.setOnClickListener {
                val intent = Intent(this@MainActivity, CopyPasteExampleActivity::class.java)
                startActivity(intent)
            }

            onclickDelegationExamples.setOnClickListener {
                val intent = Intent(this@MainActivity, ImplicitDelegationActivity::class.java)
                startActivity(intent)
            }

            locationExamples.setOnClickListener {
                val intent = Intent(this@MainActivity, LocationMapActivity::class.java)
                startActivity(intent)
            }

            googleMapsExamples.setOnClickListener {
                val intent = Intent(this@MainActivity, GoogleMapsActivity::class.java)
                startActivity(intent)
            }

            hardwareSensorExamples.setOnClickListener {
                OptionDialogFragment(this@MainActivity).show(supportFragmentManager,null)
            }

            videoAudioStreamingExamples.setOnClickListener {
                val intent = Intent(this@MainActivity, VideoAudioStreamingActivity::class.java)
                startActivity(intent)
            }

            cameraExamples.setOnClickListener {
                val intent = Intent(this@MainActivity, CameraExamplesActivity::class.java)
                startActivity(intent)
            }

            bluetoothExamples.setOnClickListener {
                val intent = Intent(this@MainActivity, BluetoothExampleActivity::class.java)
                startActivity(intent)
            }

            wifiP2pExamples.setOnClickListener {
                val intent = Intent(this@MainActivity, Wifi_P2P_ExampleActivity::class.java)
                startActivity(intent)
            }

            paging3Example.setOnClickListener {
                val intent = Intent(this@MainActivity, PagingExampleActivity::class.java)
                startActivity(intent)
            }

            shimmerAnimationExample.setOnClickListener {
                val intent = Intent(this@MainActivity, ShimmerAnimationActivity::class.java)
                startActivity(intent)
            }

            recyclerViewItemAnimationExample.setOnClickListener {
                val intent = Intent(this@MainActivity, RecyclerViewItemAnimationActivity::class.java)
                startActivity(intent)
            }

            preferenceDatastoreExample.setOnClickListener {
                val intent = Intent(this@MainActivity, PreferenceDataStoreActivity::class.java)
                startActivity(intent)
            }

            kotlinFlowAdvanceExample.setOnClickListener {
                val intent = Intent(this@MainActivity, KotlinFlowAdvanceExampleActivity::class.java)
                startActivity(intent)
            }

            motionLayoutExample.setOnClickListener {
                val intent = Intent(this@MainActivity, MotionLayoutExampleActivity::class.java)
                startActivity(intent)
            }

            asyncRecyclerviewExample.setOnClickListener {
                val intent = Intent(this@MainActivity, AsyncRecyclerViewExampleActivity::class.java)
                startActivity(intent)
            }

            cancelNotificationIfNeeded(ForegroundService.NOTIFICATION_ID)
        }
    }

    private fun cancelNotificationIfNeeded(notificationID:Int)
    {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationID)
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
