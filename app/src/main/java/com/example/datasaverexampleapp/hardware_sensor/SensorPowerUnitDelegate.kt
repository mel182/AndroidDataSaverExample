package com.example.datasaverexampleapp.hardware_sensor

import java.lang.NumberFormatException
import kotlin.reflect.KProperty

class SensorPowerUnitDelegate : StringDelegate()
{
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        super.setValue(thisRef, property, value)

        try {
            val powerUsage = value.toFloat()
            this.value = StringBuilder().append(powerUsage).append(" mA").toString()
        } catch (e: NumberFormatException)
        {
            this.value = "-"
        }
    }
}