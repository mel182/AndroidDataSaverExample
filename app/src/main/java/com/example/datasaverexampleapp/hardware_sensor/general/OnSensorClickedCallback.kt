package com.example.datasaverexampleapp.hardware_sensor.general

import android.hardware.Sensor

interface OnSensorClickedCallback {
    fun onSensorSelected(selectedSensor:Sensor)
}