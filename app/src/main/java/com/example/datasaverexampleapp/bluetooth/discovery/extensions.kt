package com.example.datasaverexampleapp.bluetooth.discovery

import android.bluetooth.BluetoothAdapter

fun Int?.asScanModeText() : String
{
    return this?.let { mode ->

        when(mode)
        {
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE -> "Connectable discovery"
            BluetoothAdapter.SCAN_MODE_CONNECTABLE -> "Connectable"
            BluetoothAdapter.SCAN_MODE_NONE -> "None"
            else -> ""
        }

    }?:"-"
}