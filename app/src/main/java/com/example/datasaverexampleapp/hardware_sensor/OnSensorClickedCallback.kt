package com.example.datasaverexampleapp.hardware_sensor

import android.hardware.Sensor

interface OnSensorClickedCallback {
    fun onSensorSelected(selectedSensor:Sensor)
}