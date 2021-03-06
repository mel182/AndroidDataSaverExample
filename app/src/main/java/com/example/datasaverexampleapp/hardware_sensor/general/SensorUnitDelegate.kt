package com.example.datasaverexampleapp.hardware_sensor.general

import android.hardware.Sensor
import java.lang.NumberFormatException
import kotlin.reflect.KProperty

class SensorUnitDelegate : StringDelegate()
{
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String)
    {
        try {
            val type = value.toInt()
            this.value = getSensorUnit(type)
        } catch (e: NumberFormatException)
        {
            this.value = "-"
        }
    }

    private fun getSensorUnit(type:Int) : String
    {
        return when(type)
        {
            Sensor.TYPE_ACCELEROMETER,Sensor.TYPE_GRAVITY,Sensor.TYPE_LINEAR_ACCELERATION -> "m/s\u00B2"
            Sensor.TYPE_MAGNETIC_FIELD,Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED -> "μT"
            Sensor.TYPE_ORIENTATION -> "°"
            Sensor.TYPE_GYROSCOPE, Sensor.TYPE_GYROSCOPE_UNCALIBRATED -> "rad/s"
            Sensor.TYPE_LIGHT -> "lx"
            Sensor.TYPE_PRESSURE -> "mbar"
            Sensor.TYPE_TEMPERATURE, Sensor.TYPE_AMBIENT_TEMPERATURE -> "°C"
            Sensor.TYPE_PROXIMITY -> "cm"
            Sensor.TYPE_ROTATION_VECTOR -> "-"
            Sensor.TYPE_RELATIVE_HUMIDITY -> "%"
            Sensor.TYPE_GAME_ROTATION_VECTOR -> " ( x * sin(θ/2))"
            Sensor.TYPE_SIGNIFICANT_MOTION -> ""
            Sensor.TYPE_STEP_DETECTOR -> "steps"
            Sensor.TYPE_STEP_COUNTER -> ""
            Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT -> ""
            Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR -> "-"
            Sensor.TYPE_HEART_RATE -> "bpm"
            Sensor.TYPE_POSE_6DOF -> "sin(θ/2)"
            Sensor.TYPE_STATIONARY_DETECT -> ""
            Sensor.TYPE_MOTION_DETECT -> ""
            Sensor.TYPE_HEART_BEAT -> ""
            else ->  ""
        }
    }
}