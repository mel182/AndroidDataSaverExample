package com.example.datasaverexampleapp.bluetooth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.bluetooth.discovery.BluetoothDiscoveryExampleActivity
import kotlinx.android.synthetic.main.activity_bluetooth_example.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bluetooth_examples

/**
 * Bluetooth is a communications protocol designed for short-range, low-bandwidth peer-to-peer communications.
 * Using the bluetooth API's you can search for, and connect to, other Bluetooth devices within range.
 * By initiating a communication link using Bluetooth Sockets, you can then transmit and receive streams
 * of data between devices from devices within your applications.
 *
 * Every bluetooth task are manage by the Bluetooth device adapter. The local Bluetooth adapter is controlled via
 * the 'BluetoothAdapter' class, which represents the host Android device on which your application is running.
 *
 * To access the default Bluetooth Adapter, call 'getDefaultAdapter'. Some Android devices feature multiple Bluetooth
 * adapter, though it is currently only possible to access the default device.
 */
class BluetoothExampleActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_example)
        title = "Bluetooth examples"
        bluetooth_discovery_example?.setOnClickListener {
            val intent = Intent(this, BluetoothDiscoveryExampleActivity::class.java)
            startActivity(intent)

        }
    }
}