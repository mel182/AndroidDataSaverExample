package com.example.datasaverexampleapp.bluetooth.base

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.core.content.ContextCompat
import com.example.datasaverexampleapp.activityRequestHandler.ActivityResultHandler
import com.example.datasaverexampleapp.handlers.activity_result_handler.constant.ActivityIntent
import com.example.datasaverexampleapp.handlers.permission.interfaces.RequestPermissionCallback
import com.example.datasaverexampleapp.intent_example.IntentBaseActivity
import com.example.datasaverexampleapp.speech_recognition_example.OnActivityResult
import kotlinx.android.synthetic.main.activity_bluetooth_discovery_example.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

/**
 * The local Bluetooth adapter is controlled via the 'BluetoothAdapter' class, which represents the host
 * Android device on which your application is running.
 * To read any of local Bluetooth Adapter properties, initiate discovery, or find bonded devices, you need
 * to include the 'Bluetooth' permission in your application manifest:
 * '<uses-permission android:name="android.permission.BLUETOOTH"/>'
 * Bluetooth scans can be used to gather information about the user's current location, so use of Bluetooth
 * also requires tje ACCESS_COARSE_LOCATION of ACCESS_FINE_LOCATION permission to be declared in your manifest:
 * '<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>'
 *
 * To modify any of local device properties, the BLUETOOTH_ADMIN permission is also required:
 * '<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>'
 *
 * To access the default Bluetooth Adapter, call 'getDefaultAdapter'
 */
abstract class BluetoothBaseActivity(private val layout: Int) : IntentBaseActivity() {
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var activityResultHandler: ActivityResultHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        registerReceiver(
            bluetoothStateReceiver,
            IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        )
        registerReceiver(
            bluetoothStateReceiver,
            IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        )
        registerReceiver(
            bluetoothStateReceiver,
            IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        )
        registerReceiver(
            bluetoothStateReceiver,
            IntentFilter(BluetoothDevice.ACTION_FOUND)
        )
        activityResultHandler = ActivityResultHandler(this)
    }

    /**
     * The bluetooth Adapter properties can be read and changed only if the Bluetooth Adapter is
     * currently turned on-that is, if its device state is enabled.
     */
    protected fun isBluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled ?: false

    /**
     * Get bluetooth hardware address
     * Note: if bluetooth is turned off, it will returns null
     */
    protected fun getBluetoothAddress(): String = bluetoothAdapter?.address ?: ""

    /*
     * Get Bluetooth adapter friendly name
     * Note: if bluetooth is turned off, it will returns null
     */
    protected fun getFriendlyName(): String = bluetoothAdapter?.name ?: ""

    /*
     * Set Bluetooth adapter friendly name
     * Note: if you have the BLUETOOTH_ADMIN permission, you can change the friendly name of the Bluetooth Adapter
     *       using the 'setName' method.
     */
    protected fun setFriendlyName(name: String) {
        bluetoothAdapter?.name = name
    }

    /*
     * Note: To find more detailed description of the current Bluetooth Adapter state, use the 'getState'
     *      method, which will return one of the following 'BluetoothAdapter' constants.
     * States:
     *    - BluetoothAdapter.STATE_TURNING_ON
     *    - BluetoothAdapter.STATE_ON
     *    - BluetoothAdapter.STATE_TURNING_OFF
     *    - BluetoothAdapter.STATE_OFF
     */
    protected fun getBluetoothAdapterState(): Int = bluetoothAdapter?.state ?: -1

    /**
     * Request bluetooth enabled
     */
    protected fun enabledBluetooth(enabled: (Boolean) -> Unit) {
        val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        activityResultHandler?.startActivityForResult(
            enableBluetoothIntent,
            object : OnActivityResult {
                override fun onActivityResult(result: ActivityResult) {
                    if (result.resultCode == RESULT_OK) {
                        enabled(true)
                    } else {
                        enabled(false)
                    }
                }
            })
    }

    /**
     * Enabled bluetooth discovery
     */
    protected fun enabledDiscovery(enabled: (Boolean) -> Unit) {
        val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        activityResultHandler?.startActivityForResult(
            enableBluetoothIntent,
            object : OnActivityResult {
                override fun onActivityResult(result: ActivityResult) {
                    when (result.resultCode) {
                        RESULT_OK -> enabled(true)
                        RESULT_CANCELED -> {
                            // Discovery cancelled by user
                            enabled(false)
                        }
                        else -> enabled(true)
                    }
                }
            }
        )
    }

    protected fun getBluetoothScanMode(): Int = bluetoothAdapter?.scanMode ?: -1

    /**
     * Request bluetooth enabled
     */
    protected fun requestBluetoothEnablePermission(enabled: (Boolean) -> Unit) {
        requestPermission(ActivityIntent.ENABLE_BLUETOOTH, object : RequestPermissionCallback {

            override fun onPermissionGranted() {
                enabled(true)
            }

            override fun onPermissionDenied() {
                enabled(false)
            }
        })
    }

    protected fun cancelDiscovery()
    {
        // The discovery process consumes significant resources, so you should be sure to cancel a discover in progress
        // using the 'cancelDiscovery' method, prior to attempting to connect with any discovered devices.
        bluetoothAdapter?.let { adapter ->
            if (adapter.isEnabled && adapter.isDiscovering)
                adapter.cancelDiscovery()
        }
    }

    protected fun listeningUsingRfcommWithServiceRecord(name:String, uuid:UUID) : BluetoothServerSocket?
    {
        try {
            val serverSocket = bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord(name, uuid)

            CoroutineScope(Dispatchers.IO).launch {

                try {
                    serverSocket?.accept()
                } catch (e:IOException)
                {
                    e.printStackTrace()
                }
            }

            return serverSocket

        }catch (e:IOException)
        {
            e.printStackTrace()
        }
        return null
    }

    protected fun startDiscovery(succeed : (Boolean) -> Unit)
    {
        // You must include the ACCESS_COARSE_LOCATION permission in the application manifest and request
        // it as runtime permission before performing device discovery.
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            bluetoothAdapter?.let { adapter ->

                // You must check if the local Bluetooth Adapter is already performing a discovery scan by using the
                // 'isDiscovering' method.
                if (adapter.isEnabled && !adapter.isDiscovering)
                {
                    // To initiate the discovery process, call 'startDiscovery' on the Bluetooth Adapter.
                    //
                    // Note: The discovery process is asynchronous. Android broadcast Intents to notify you of the start
                    //       and end of discovery, as well as notifying you of remote devices discovered during the scan.
                    //
                    // The discovery process consumes significant resources, so you can should be sure to cancel a discovery
                    // in progress using the 'cancelDiscovery' method, prior to attempting to connect with any discovered devices.
                    adapter.startDiscovery()
                    succeed(true)
                } else {
                    succeed(false)
                }
            }?: kotlin.run {
                succeed(false)
            }
        } else {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, object : RequestPermissionCallback{
                override fun onPermissionGranted() {

                    bluetoothAdapter?.let { adapter ->

                        // You must check if the local Bluetooth Adapter is already performing a discovery scan by using the
                        // 'isDiscovering' method.
                        if (adapter.isEnabled && !adapter.isDiscovering)
                        {
                            // To initiate the discovery process, call 'startDiscovery' on the Bluetooth Adapter.
                            //
                            // Note: The discovery process is asynchronous. Android broadcast Intents to notify you of the start
                            //       and end of discovery, as well as notifying you of remote devices discovered during the scan.
                            adapter.startDiscovery()
                            succeed(true)
                        } else {
                            succeed(false)
                        }
                    }?: kotlin.run {
                        succeed(false)
                    }
                }

                override fun onPermissionDenied() {
                    succeed(false)
                }
            })
        }
    }

    open fun onBluetoothStateChanged(state: Int) {}
    open fun onBluetoothDiscoveryStarted() {}
    open fun onBluetoothDiscoveryEnded() {}
    open fun onBluetoothRemoteDeviceFound(name:String?, device:BluetoothDevice?) {}

    private val bluetoothStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            intent?.let { intent ->

                intent.action?.apply {

                    if (this == BluetoothAdapter.ACTION_STATE_CHANGED) {
                        when (intent.getIntExtra(
                            BluetoothAdapter.EXTRA_STATE,
                            BluetoothAdapter.ERROR
                        )) {
                            BluetoothAdapter.STATE_OFF -> onBluetoothStateChanged(BluetoothAdapter.STATE_OFF)
                            BluetoothAdapter.STATE_TURNING_OFF -> onBluetoothStateChanged(
                                BluetoothAdapter.STATE_TURNING_OFF
                            )
                            BluetoothAdapter.STATE_ON -> onBluetoothStateChanged(BluetoothAdapter.STATE_ON)
                            BluetoothAdapter.STATE_TURNING_ON -> onBluetoothStateChanged(
                                BluetoothAdapter.STATE_TURNING_ON
                            )
                        }
                    } else if (this == BluetoothAdapter.ACTION_DISCOVERY_STARTED)
                    {
                        onBluetoothDiscoveryStarted()
                    } else if (this == BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
                    {
                        onBluetoothDiscoveryEnded()
                    } else if (this == BluetoothDevice.ACTION_FOUND)
                    {
                        // The broadcast in general also includes name of remote device in an extra indexed as 'BluetoothDevice.EXTRA_NAME'
                        //
                        val remoteDeviceName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME)
                        val remoteDevice = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        onBluetoothRemoteDeviceFound(remoteDeviceName, remoteDevice)
                    }


                    // // The broadcast in general also includes name of remote device in an extra indexed as 'BluetoothDevice.EXTRA_NAME'
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        bluetoothStateReceiver?.let { receiver ->
            unregisterReceiver(receiver)
        }
    }
}