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
            Sensor.REPORTING_MODE_CONTINUOUS -> "Continuous" // Events are returned at least at the constant rate defined by
                                                             // the rate parameter used when you register a listener.

            Sensor.REPORTING_MODE_ON_CHANGE -> "On change"   // Events are returned only when the value changes, limited to be no more
                                                             // often than the rate parameter used when registered a listener.

            Sensor.REPORTING_MODE_ONE_SHOT -> "One shot"     // Events are reported only once, when the event is detected.

            Sensor.REPORTING_MODE_SPECIAL_TRIGGER -> "Special trigger" // Used by Sensors that have special triggers that aren't
                                                                       // continuous, one-off, nor change-triggered.
            else -> "Unknown"
        }
    }
}