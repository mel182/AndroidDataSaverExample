@file:Suppress("DEPRECATION")

package com.example.datasaverexampleapp.hardware_sensor.general

import android.hardware.Sensor
import java.lang.NumberFormatException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SensorTypeDelegate : ReadWriteProperty<Any?, String>
{
    private var value: String = ""

    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return value
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String)
    {
        try {
            val type = value.toInt()
            this.value = getSensorType(type)
        } catch (e:NumberFormatException)
        {
            this.value = "Unknown"
        }
    }


    private fun getSensorType(type:Int) : String
    {
        return when(type)
        {
            Sensor.TYPE_ACCELEROMETER -> "Accelerometer"
            Sensor.TYPE_MAGNETIC_FIELD -> "Magnetic field"
            Sensor.TYPE_ORIENTATION -> "Orientation"
            Sensor.TYPE_GYROSCOPE -> "Gyroscope"
            Sensor.TYPE_LIGHT -> "Light"
            Sensor.TYPE_PRESSURE -> "Pressure"
            Sensor.TYPE_TEMPERATURE -> "Temperature"
            Sensor.TYPE_PROXIMITY -> "Proximity"
            Sensor.TYPE_GRAVITY -> "Gravity"
            Sensor.TYPE_LINEAR_ACCELERATION -> "Linear acceleration"
            Sensor.TYPE_ROTATION_VECTOR -> "Rotation vector"
            Sensor.TYPE_RELATIVE_HUMIDITY -> "Relative Humidity"
            Sensor.TYPE_AMBIENT_TEMPERATURE -> "Ambient Temperature"
            Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED -> "Magnetic field uncalibrated"
            Sensor.TYPE_GAME_ROTATION_VECTOR -> "Game rotation vector"
            Sensor.TYPE_GYROSCOPE_UNCALIBRATED -> "Gyroscope uncalibrated"
            Sensor.TYPE_SIGNIFICANT_MOTION -> "Significant motion"
            Sensor.TYPE_STEP_DETECTOR -> "Step detector"
            Sensor.TYPE_STEP_COUNTER -> "Step counter"
            Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT -> "Low latency offbody detect"
            Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR -> "Geomagnetic Rotation Vector"
            Sensor.TYPE_HEART_RATE -> "Heart rate"
            Sensor.TYPE_POSE_6DOF -> "Pose 6D0F"
            Sensor.TYPE_STATIONARY_DETECT -> "Stationary detect"
            Sensor.TYPE_MOTION_DETECT -> "Motion detect"
            Sensor.TYPE_HEART_BEAT -> "Heart beat"
            else ->  CUSTOM_SENSOR
        }
    }
}