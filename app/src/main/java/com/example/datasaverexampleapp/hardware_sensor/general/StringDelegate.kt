package com.example.datasaverexampleapp.hardware_sensor.general

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class StringDelegate : ReadWriteProperty<Any?, String>
{
    var value: String = ""

    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return value
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String)
    {
        this.value = value
    }

}