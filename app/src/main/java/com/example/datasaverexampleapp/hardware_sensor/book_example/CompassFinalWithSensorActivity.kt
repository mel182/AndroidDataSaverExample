@file:Suppress("DEPRECATION")

package com.example.datasaverexampleapp.hardware_sensor.book_example

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityCompassExampleFinalBinding
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class CompassFinalWithSensorActivity : AppCompatActivity(), SensorEventListener
{
    private var sensorManager:SensorManager? = null
    private var screenRotation:Int = 0
    private var newestValue = FloatArray(3)
    private var compassTimer:Timer? = null
    private var binding: ActivityCompassExampleFinalBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass_example_final)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        screenRotation = windowManager.defaultDisplay.rotation

        val period = 1000L/60L
        compassTimer = Timer("compassUpdate").apply {

            scheduleAtFixedRate(object : TimerTask(){
                override fun run() {
                    updateGUI()
                }
            },0,period)
        }

        binding = DataBindingUtil.setContentView(
            this, Layout.activity_compass_example_final
        )

    }

    override fun onResume() {
        super.onResume()
        sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)?.apply {
            sensorManager?.registerListener(this@CompassFinalWithSensorActivity, this,SensorManager.SENSOR_DELAY_FASTEST )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
        compassTimer?.cancel()
    }

    private fun updateOrientation(values:FloatArray)
    {
        binding?.apply {
            compassView.apply {
                bearing = values[0]
                pitch = values[1]
                roll = values[2]
                invalidate()
            }
        }
    }

    private fun calculateOrientation(values:FloatArray) : FloatArray
    {
        var rotationMatrix = FloatArray(9)
        var remappedMatrix = FloatArray(9)
        var orientationMatrix = FloatArray(3)

        // Determine the rotation matrix
        SensorManager.getQuaternionFromVector(rotationMatrix,values)

        // remap the coordinate based on the natural device orientation
        var x_axis = SensorManager.AXIS_X
        var y_axis = SensorManager.AXIS_Y

        when(screenRotation)
        {
            Surface.ROTATION_90 -> {
                x_axis = SensorManager.AXIS_Y
                y_axis = SensorManager.AXIS_MINUS_X
            }
            Surface.ROTATION_180 -> {
                y_axis = SensorManager.AXIS_MINUS_Y
            }
            Surface.ROTATION_270 -> {
                x_axis = SensorManager.AXIS_MINUS_Y
                y_axis = SensorManager.AXIS_X
            }
        }

        SensorManager.remapCoordinateSystem(rotationMatrix,x_axis,y_axis,remappedMatrix)

        // Obtain the current, corrected orientation
        SensorManager.getOrientation(remappedMatrix,orientationMatrix)

        // Convert from Radians to Degrees
        values[0] = Math.toDegrees(orientationMatrix[0].toDouble()).toFloat()
        values[1] = Math.toDegrees(orientationMatrix[1].toDouble()).toFloat()
        values[2] = Math.toDegrees(orientationMatrix[2].toDouble()).toFloat()

        return values
    }

    private fun updateGUI()
    {
        CoroutineScope(Dispatchers.Main).launch {
            updateOrientation(newestValue)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {

        event?.let { sensorEvent ->
            newestValue = calculateOrientation(sensorEvent.values)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { }
}