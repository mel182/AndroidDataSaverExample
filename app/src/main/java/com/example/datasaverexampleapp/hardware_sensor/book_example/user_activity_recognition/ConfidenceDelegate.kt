package com.example.datasaverexampleapp.hardware_sensor.book_example.user_activity_recognition

import com.example.datasaverexampleapp.hardware_sensor.general.StringDelegate
import kotlin.reflect.KProperty

class ConfidenceDelegate : StringDelegate()
{

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        super.setValue(thisRef, property, value)

        try {
            val value = value.toInt()
            this.value = getConfidence(value)
        } catch (e: NumberFormatException)
        {
            this.value = "Unknown"
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return this.value
    }

    private fun getConfidence(value:Int) : String
    {
        return if (value == 100 || value > 50) {
            "Correct"
        } else {
            "More likely"
        }
    }
}