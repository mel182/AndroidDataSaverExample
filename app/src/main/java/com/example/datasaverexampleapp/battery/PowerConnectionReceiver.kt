package com.example.datasaverexampleapp.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log
import android.widget.Toast

class PowerConnectionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
        val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

        val chargePlug = intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        val isUSBCharging = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
        val isACCharging = chargePlug == BatteryManager.BATTERY_PLUGGED_AC

        Log.i("TAG","on receive power connection called!")

        context?.let {

            if (isCharging)
            {
                // In case the device is charging you should maximize all the operations to be performed
                if (isUSBCharging)
                {
                    Toast.makeText(it,"Charging, battery level $level%, usb charging", Toast.LENGTH_SHORT).show()
                } else if (isACCharging){
                    Toast.makeText(it,"Charging, battery level $level%, ac charging", Toast.LENGTH_SHORT).show()
                }
            } else
            {
                Toast.makeText(it,"Not charging, battery level $level%", Toast.LENGTH_SHORT).show()
            }
        }
    }


}