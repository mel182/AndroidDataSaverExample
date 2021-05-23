package com.example.datasaverexampleapp.bluetooth.server

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.bluetooth.base.BluetoothBaseActivity
import com.example.datasaverexampleapp.type_alias.Layout

class BluetoothServerExampleActivity : BluetoothBaseActivity(Layout.activity_bluetooth_server_example)
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Bluetooth Server Example"
    }
}