package com.example.datasaverexampleapp.hardware_sensor

import android.annotation.SuppressLint
import android.hardware.Sensor

@SuppressLint("InlinedApi")
fun Int.isCustomSensor() : Boolean
{
    return (this != Sensor.TYPE_STEP_DETECTOR && this != Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT
        && this != Sensor.TYPE_HEART_RATE && this != Sensor.TYPE_HEART_BEAT
        && this != Sensor.TYPE_SIGNIFICANT_MOTION && this != Sensor.TYPE_STATIONARY_DETECT
        && this != Sensor.TYPE_MOTION_DETECT && this != Sensor.TYPE_POSE_6DOF
        && this != Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR && this != Sensor.TYPE_ROTATION_VECTOR
        && this != Sensor.TYPE_LINEAR_ACCELERATION && this != Sensor.TYPE_GYROSCOPE
        && this != Sensor.TYPE_ACCELEROMETER && this != Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED
        && this != Sensor.TYPE_MAGNETIC_FIELD && this != Sensor.TYPE_PRESSURE
        && this != Sensor.TYPE_GAME_ROTATION_VECTOR && this != Sensor.TYPE_STEP_COUNTER
        && this != Sensor.TYPE_LIGHT && this != Sensor.TYPE_PROXIMITY && this != Sensor.TYPE_GRAVITY
        && this != Sensor.TYPE_ORIENTATION && this != Sensor.TYPE_GYROSCOPE_UNCALIBRATED)
}