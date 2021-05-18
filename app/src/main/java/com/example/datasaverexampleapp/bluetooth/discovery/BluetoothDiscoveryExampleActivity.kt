package com.example.datasaverexampleapp.bluetooth.discovery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.datasaverexampleapp.R

class BluetoothDiscoveryExampleActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_discovery_example)
        title = "Bluetooth discovery Example"
    }
}