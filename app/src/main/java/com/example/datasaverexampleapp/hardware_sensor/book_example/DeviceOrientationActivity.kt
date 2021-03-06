package com.example.datasaverexampleapp.hardware_sensor.book_example

import android.app.PictureInPictureParams
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.FieldClassification
import android.util.Log
import android.view.Surface
import android.view.WindowManager
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_device_orientation.*
import java.lang.StringBuilder
import kotlin.math.roundToInt

class DeviceOrientationActivity : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var accelerometerSensor: Sensor? = null
    private var magneticFieldSensor: Sensor? = null
    private var gyroscopeSensor: Sensor? = null
    private var accelerometerValues:FloatArray? = null
    private var magneticFieldValues:FloatArray? = null

    val nanosecondPerSecond = 1.0f / 100000000.0f
    var lastTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_orientation)
        title = "Device orientation Example"

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magneticFieldSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        gyroscopeSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        sensorManager?.let { manager ->
            accelerometerSensor?.let { sensor ->
                manager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_UI)
            }
            magneticFieldSensor?.let { sensor ->
                manager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_UI)
            }
            gyroscopeSensor?.let { sensor ->
                manager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {

        event?.let { sensorEvent ->

            if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER)
                accelerometerValues = sensorEvent.values

            if (sensorEvent.sensor.type == Sensor.TYPE_MAGNETIC_FIELD)
                magneticFieldValues = sensorEvent.values

            setValuesUsingAccelerometerMagnetometer()

            if (sensorEvent.sensor.type == Sensor.TYPE_GYROSCOPE)
            {
                if (lastTime != 0L)
                {
                    val dT = (sensorEvent.timestamp - lastTime) * nanosecondPerSecond

                    val azimuth = sensorEvent.values[0] * dT
                    val pitch = sensorEvent.values[1] * dT
                    val roll = sensorEvent.values[2] * dT

                    azimuth2_text.text = StringBuilder().append(azimuth.roundToInt()).append("°").toString()
                    pitch2_text.text = StringBuilder().append(pitch.roundToInt()).append("°").toString()
                    roll2_text.text = StringBuilder().append(roll.roundToInt()).append("°").toString()
                }
                lastTime = sensorEvent.timestamp

                determineDeviceOrientation(sensorEvent.values)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { }

    private fun setValuesUsingAccelerometerMagnetometer()
    {
        if (accelerometerValues == null || magneticFieldValues == null)
            return

        val values = FloatArray(3)
        val radians = FloatArray(9)

        SensorManager.getRotationMatrix(radians, null ,accelerometerValues,magneticFieldValues)
        SensorManager.getOrientation(radians,values)

        val azimuth = Math.toDegrees(values[0].toDouble())
        val pitch = Math.toDegrees(values[1].toDouble())
        val roll = Math.toDegrees(values[2].toDouble())

        azimuth_text.text = StringBuilder().append(azimuth.roundToInt()).append("°").toString()
        pitch_text.text = StringBuilder().append(pitch.roundToInt()).append("°").toString()
        roll_text.text = StringBuilder().append(roll.roundToInt()).append("°").toString()
    }

    private fun determineDeviceOrientation(values:FloatArray)
    {
        val displayRotation = windowManager.defaultDisplay.rotation

        var x_axis = SensorManager.AXIS_X
        var y_axis = SensorManager.AXIS_Y

        val inR = FloatArray(16)
        val outR = FloatArray(16)

        when(displayRotation)
        {
            Surface.ROTATION_90 -> {
                x_axis = SensorManager.AXIS_Y
                y_axis = SensorManager.AXIS_X
            }
            Surface.ROTATION_180 -> {
                y_axis = SensorManager.AXIS_MINUS_Y
            }
            Surface.ROTATION_270 -> {
                x_axis = SensorManager.AXIS_MINUS_Y
                y_axis = SensorManager.AXIS_X
            }
        }

        SensorManager.remapCoordinateSystem(inR,x_axis,y_axis,outR)

        // Obtain the new, remapped, orientation value
        val orientation = SensorManager.getOrientation(outR,values)
        device_orientation.text = orientation.joinToString()

        if (orientation.size == 3)
        {
            val azimuth = orientation[0]
            val pitch = orientation[1]
            val roll = orientation[2]

            val azimuthString = StringBuilder().append("z: ").append(azimuth.roundToInt()).append("°").toString()
            val pitchString = StringBuilder().append("x: ").append(pitch.roundToInt()).append("°").toString()
            val rollString = StringBuilder().append("y: ").append(roll.roundToInt()).append("°").toString()

            device_orientation.text = StringBuilder()
                .append(azimuthString)
                .append(" ")
                .append(pitchString)
                .append(" ")
                .append(rollString).toString()
        }

    }
}