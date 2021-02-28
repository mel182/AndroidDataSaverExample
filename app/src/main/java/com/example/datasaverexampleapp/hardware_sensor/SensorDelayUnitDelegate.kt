package com.example.datasaverexampleapp.hardware_sensor

import java.lang.NumberFormatException
import kotlin.reflect.KProperty

class SensorDelayUnitDelegate : StringDelegate() {

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String)
    {
        try {
            val delayValue = value.toInt()
            this.value = StringBuilder().append(delayValue).append(" μs").toString()
        } catch (e: NumberFormatException)
        {
            this.value = "-"
        }
    }
}