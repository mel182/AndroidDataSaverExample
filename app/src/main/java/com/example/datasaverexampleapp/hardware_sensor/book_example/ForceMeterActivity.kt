@file:Suppress("UNNECESSARY_SAFE_CALL")

package com.example.datasaverexampleapp.hardware_sensor.book_example

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityForceMeterBinding
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

@SuppressLint("SetTextI18n")
class ForceMeterActivity : AppCompatActivity(), SensorEventListener {

    private var sensorManager:SensorManager? = null
    private var currentAcceleration = 0f
    private var maxAcceleration = 0f
    private val calibration = SensorManager.STANDARD_GRAVITY
    private var binding: ActivityForceMeterBinding? = null

    // Because this application is functional only when the host device features an accelerometer
    // sensor, modify the manifest to include a 'uses-feature' node specifying the requirement for accelerometer hardware.
    //
    // <uses-feature android:name="android.hardware.sensor.accelerometer" />
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_force_meter)
        title = "Force meter example"

        binding = DataBindingUtil.setContentView(this, Layout.activity_force_meter)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        Timer("gForceUpdate").apply {
            scheduleAtFixedRate(object : TimerTask(){
                override fun run() {
                    updateGUI()
                }
            },0,100)
        }
    }

    override fun onResume() {
        super.onResume()

        sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let { sensor ->
            sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        }?: kotlin.run {
            binding?.apply {
                acceleration.text = "Sensor not available"
                maxAcceleration.text = ""
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {

        event?.apply {

            val x = values[0].toDouble()
            val y = values[1].toDouble()
            val z = values[2].toDouble()

            val a = Math.abs(Math.pow(x, 2.0) + Math.pow(y, 2.0) + Math.pow(z, 2.0))
            currentAcceleration = Math.abs((a - calibration).toFloat())

            if (currentAcceleration > maxAcceleration)
                maxAcceleration = currentAcceleration
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { }

    private fun updateGUI()
    {
        CoroutineScope(Dispatchers.Main).launch {

            val currentG = getResultString(currentAcceleration/calibration)
            val maxG = getResultString(currentAcceleration/calibration)
            binding?.apply {
                acceleration.text = currentG
                maxAcceleration?.apply {
                    text = maxG
                    invalidate()
                }
            }
        }
    }

    private fun getResultString(value:Float):String
    {
        val number2DigitRound = (value * 100.0).roundToInt() / 100.0
        return StringBuilder().append(number2DigitRound).append("Gs").toString()
    }

}