package com.example.datasaverexampleapp.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.widget.Toast

object Battery {

    private lateinit var context: Context

    private val mBatInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)

            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

            val chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
            val isUSBCharging = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
            val isACCharging = chargePlug == BatteryManager.BATTERY_PLUGGED_AC

            if (isCharging)
            {
                // In case the device is charging you should maximize all the operations to be performed
                if (isUSBCharging)
                {
                    Toast.makeText(context,"Charging, battery level $level%, usb charging", Toast.LENGTH_SHORT).show()
                } else if (isACCharging){
                    Toast.makeText(context,"Charging, battery level $level%, ac charging", Toast.LENGTH_SHORT).show()
                }

            } else
            {
                Toast.makeText(context,"Not charging, battery level $level%", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun registerBatteryStatus(context:Context)
    {
        this.context = context
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        this.context.registerReceiver(mBatInfoReceiver,intentFilter)
        Toast.makeText(Battery.context,"Battery status registered!", Toast.LENGTH_SHORT).show()
    }
}