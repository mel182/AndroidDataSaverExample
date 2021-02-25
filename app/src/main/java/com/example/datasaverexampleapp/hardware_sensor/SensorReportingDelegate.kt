package com.example.datasaverexampleapp.hardware_sensor

import android.hardware.Sensor
import kotlin.reflect.KProperty

class SensorReportingDelegate : StringDelegate()
{

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        try {
            val type = value.toInt()
            this.value = getSensorReportingType(type)
        } catch (e: NumberFormatException)
        {
            this.value = "Unknown"
        }
    }

    private fun getSensorReportingType(reporting:Int) : String
    {
        return when(reporting)
        {
            Sensor.REPORTING_MODE_CONTINUOUS -> "Continuous"
            Sensor.REPORTING_MODE_ON_CHANGE -> "On change"
            Sensor.REPORTING_MODE_ONE_SHOT -> "One shot"
            Sensor.REPORTING_MODE_SPECIAL_TRIGGER -> "Special trigger"
            else -> "Unknown"
        }
    }
}