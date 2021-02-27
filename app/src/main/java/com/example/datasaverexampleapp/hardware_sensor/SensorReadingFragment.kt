package com.example.datasaverexampleapp.hardware_sensor

import android.Manifest.permission.BODY_SENSORS
import android.animation.Animator
import android.animation.AnimatorInflater
import android.hardware.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.application.AppContext
import com.example.datasaverexampleapp.base_classes.BaseFragment
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.android.synthetic.main.activity_property_animation.*
import kotlinx.android.synthetic.main.fragment_sensor_reading.*
import kotlinx.android.synthetic.main.item_bottom_sheet_layout.*
import java.text.DecimalFormat


/**
 * A simple [Fragment] subclass.
 * Use the [SensorReadingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SensorReadingFragment(val sensor: Sensor, val sensorManager: SensorManager) :
    BaseFragment(Layout.fragment_sensor_reading), SensorEventListener {

    private val TAG = "SENSOR_READING"
    private var sensorValueUnit by SensorUnitDelegate()
    private val decimalFormat by lazy { DecimalFormat("0.0") }
    private var animator: Animator? = null
    private var triggerEventListener : TriggerEventListener? = null
    private var sensorEventCallback : SensorEventCallback? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sensorValueUnit = sensor.type.toString()
        animator = AnimatorInflater.loadAnimator(
            AppContext.appContext,
            R.animator.object_animator_xml_example
        ).apply {
            setTarget(reading_text)
            interpolator = DecelerateInterpolator()
        }
        animator?.start()

        max_range_text?.text = getSensorValueUnit(sensor.maximumRange)
        resolution_text?.text = getSensorValueUnit(sensor.resolution)
        min_delay_text?.text = "${sensor.minDelay} μs"
        power_text?.text = "${sensor.power} mA"

        if (sensor.type == Sensor.TYPE_STEP_DETECTOR)
        {
            sensor_measurement?.text = "Step detector sensor"
        } else if (sensor.type == Sensor.TYPE_SIGNIFICANT_MOTION)
        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {

                triggerEventListener = object : TriggerEventListener() {
                    override fun onTrigger(event: TriggerEvent?) {

                        event?.values.apply {
                            sensor_measurement?.text = "Significant motion detected!"
                        }
                    }
                }
                sensor_measurement?.text = "Trigger in special events...."
                sensorManager.requestTriggerSensor(triggerEventListener,sensor)
            } else {
                sensor_measurement?.text = "Unsupported"
            }
        } else if (sensor.type == Sensor.TYPE_GAME_ROTATION_VECTOR)
        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                sensorEventCallback = object : SensorEventCallback() {

                    override fun onSensorChanged(event: SensorEvent?) {
                        super.onSensorChanged(event)

                        event?.values.apply {

                            if (this?.size == 3) {
                                val xAxis = this[0]
                                val yAxis = this[1]
                                val zAxis = this[2]

                                sensor_measurement?.text = StringBuilder()
                                    .append("x: ")
                                    .append(getSensorValueUnit(decimalFormat.format(xAxis)))
                                    .append("\ny: ")
                                    .append(getSensorValueUnit(decimalFormat.format(yAxis)))
                                    .append("\nz: ")
                                    .append(getSensorValueUnit(decimalFormat.format(zAxis))).toString()
                            }
                        }
                    }

                    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                        super.onAccuracyChanged(sensor, accuracy)
                        setAccuracy(accuracy)
                        max_range_text?.text = getSensorValueUnit(sensor?.maximumRange)
                        resolution_text?.text = getSensorValueUnit(sensor?.resolution)
                        min_delay_text?.text = "${sensor?.minDelay} μs"
                        power_text?.text = "${sensor?.power} mA"
                    }
                }
                sensorManager.registerListener(sensorEventCallback, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            } else {
                sensor_measurement?.text = "Unsupported"
            }
        } else if (sensor.type == Sensor.TYPE_HEART_RATE)
        {
            if (isPermissionGranted(BODY_SENSORS))
            {
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            } else {
                requestPermission(BODY_SENSORS){ granted ->

                    if (granted)
                        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
                }
            }

        } else {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }


        //




    }

    override fun onSensorChanged(event: SensorEvent?) {

        event?.values?.apply {

            when (sensor.type) {
                Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GRAVITY, Sensor.TYPE_LINEAR_ACCELERATION -> {

                    if (this.size == 3) {
                        val xAxisForce = this[0]
                        val yAxisForce = this[1]
                        val zAxisForce = this[2]

                        sensor_measurement?.text = StringBuilder()
                            .append("Acceleration forces:\nx: ")
                            .append(getSensorValueUnit(decimalFormat.format(xAxisForce)))
                            .append("\ny: ")
                            .append(getSensorValueUnit(decimalFormat.format(yAxisForce)))
                            .append("\nz: ")
                            .append(getSensorValueUnit(decimalFormat.format(zAxisForce))).toString()

                    }
                }

                Sensor.TYPE_ACCELEROMETER_UNCALIBRATED -> {

                    if (this.size == 6) {
                        val xAxisAcceleration = this[0]
                        val yAxisAcceleration = this[1]
                        val zAxisAcceleration = this[2]

                        val xAxisAccelerationWithEstimate = this[3]
                        val yAxisAccelerationWithEstimate = this[4]
                        val zAxisAccelerationWithEstimate = this[5]

                        sensor_measurement?.text = StringBuilder()
                            .append("Acceleration forces:\nx: ")
                            .append(getSensorValueUnit(decimalFormat.format(xAxisAcceleration)))
                            .append("\ny: ")
                            .append(getSensorValueUnit(decimalFormat.format(yAxisAcceleration)))
                            .append(
                                "\nz: "
                            )
                            .append(getSensorValueUnit(decimalFormat.format(zAxisAcceleration)))
                            .append(
                                "\nx (estimate): "
                            )
                            .append(
                                getSensorValueUnit(
                                    decimalFormat.format(
                                        xAxisAccelerationWithEstimate
                                    )
                                )
                            )
                            .append("\ny (estimate): ")
                            .append(decimalFormat.format(yAxisAccelerationWithEstimate))
                            .append("\nz (estimate): ")
                            .append(
                                getSensorValueUnit(
                                    decimalFormat.format(
                                        zAxisAccelerationWithEstimate
                                    )
                                )
                            ).toString()
                    }
                }

                Sensor.TYPE_GYROSCOPE -> {

                    if (this.size == 3) {
                        val xAxisForce = this[0]
                        val yAxisForce = this[1]
                        val zAxisForce = this[2]

                        sensor_measurement?.text = StringBuilder()
                            .append("Rotation:\nx: ")
                            .append(getSensorValueUnit(decimalFormat.format(xAxisForce)))
                            .append("\ny: ")
                            .append(getSensorValueUnit(decimalFormat.format(yAxisForce)))
                            .append("\nz: ")
                            .append(getSensorValueUnit(decimalFormat.format(zAxisForce))).toString()
                    }
                }

                Sensor.TYPE_GYROSCOPE_UNCALIBRATED -> {

                    if (this.size == 6) {
                        val xAxisForce = this[0]
                        val yAxisForce = this[1]
                        val zAxisForce = this[2]

                        val xAxisForceDrift = this[3]
                        val yAxisForceDrift = this[4]
                        val zAxisForceDrift = this[5]

                        sensor_measurement?.text = StringBuilder()
                            .append("Rotation:\n x: ")
                            .append(getSensorValueUnit(decimalFormat.format(xAxisForce)))
                            .append("\ny: ")
                            .append(getSensorValueUnit(decimalFormat.format(yAxisForce)))
                            .append("\nz: ")
                            .append(getSensorValueUnit(decimalFormat.format(zAxisForce)))
                            .append("\nx (drift): ")
                            .append(getSensorValueUnit(decimalFormat.format(xAxisForceDrift)))
                            .append("\ny (drift): ")
                            .append(decimalFormat.format(yAxisForceDrift))
                            .append("\nz (drift): ")
                            .append(getSensorValueUnit(decimalFormat.format(zAxisForceDrift)))
                            .toString()
                    }
                }

                Sensor.TYPE_MAGNETIC_FIELD -> {

                    if (this.size == 3) {
                        val geomagneticXAxis = this[0]
                        val geomagneticYAxis = this[1]
                        val geomagneticZAxis = this[2]

                        sensor_measurement?.text = StringBuilder()
                            .append("Geomagnetic:\nx: ")
                            .append(getSensorValueUnit(decimalFormat.format(geomagneticXAxis)))
                            .append("\ny: ")
                            .append(getSensorValueUnit(decimalFormat.format(geomagneticYAxis)))
                            .append("\nz: ")
                            .append(getSensorValueUnit(decimalFormat.format(geomagneticZAxis)))
                            .toString()
                    }
                }

                Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED -> {

                    if (this.size == 6) {
                        val geomagneticXAxis = this[0]
                        val geomagneticYAxis = this[1]
                        val geomagneticZAxis = this[2]

                        val geomagneticXAxisWithEstimate = this[3]
                        val geomagneticYAxisWithEstimate = this[4]
                        val geomagneticZAxisWithEstimate = this[5]

                        sensor_measurement?.text = StringBuilder()
                            .append("Geomagnetic:\nx: ")
                            .append(getSensorValueUnit(decimalFormat.format(geomagneticXAxis)))
                            .append("\ny: ")
                            .append(getSensorValueUnit(decimalFormat.format(geomagneticYAxis)))
                            .append(
                                "\nz: "
                            )
                            .append(getSensorValueUnit(decimalFormat.format(geomagneticZAxis)))
                            .append(
                                "\nx (estimate): "
                            )
                            .append(
                                getSensorValueUnit(
                                    decimalFormat.format(
                                        geomagneticXAxisWithEstimate
                                    )
                                )
                            )
                            .append("\ny (estimate): ")
                            .append(geomagneticYAxisWithEstimate).append("\nz (estimate): ")
                            .append(
                                getSensorValueUnit(
                                    decimalFormat.format(
                                        geomagneticZAxisWithEstimate
                                    )
                                )
                            ).toString()
                    }
                }

                Sensor.TYPE_PRESSURE -> {

                    if (this.isNotEmpty()) {
                        val pressureReading = decimalFormat.format(this[0])
                        sensor_measurement?.text = StringBuilder().append(
                            getSensorValueUnit(
                                pressureReading
                            )
                        ).toString()
                    }
                }

                Sensor.TYPE_STEP_COUNTER -> {

                    if (this.isNotEmpty()) {
                        val pressureReading = decimalFormat.format(this[0])
                        sensor_measurement?.text = StringBuilder().append(
                            getSensorValueUnit(
                                pressureReading
                            )
                        ).toString()
                    }
                }

                Sensor.TYPE_LIGHT -> {

                    if (this.isNotEmpty()) {
                        val illuminanceReading = decimalFormat.format(this[0])
                        sensor_measurement?.text = StringBuilder().append(
                            getSensorValueUnit(
                                illuminanceReading
                            )
                        ).toString()
                    }
                }

                Sensor.TYPE_PROXIMITY -> {

                    if (this.isNotEmpty()) {
                        val distance = decimalFormat.format(this[0])
                        sensor_measurement?.text = StringBuilder().append(
                            getSensorValueUnit(
                                distance
                            )
                        ).toString()
                    }
                }

                Sensor.TYPE_HEART_RATE -> {

                    if (this.isNotEmpty())
                    {
                        val distance = decimalFormat.format(this[0])
                        sensor_measurement?.text = StringBuilder().append(
                            getSensorValueUnit(
                                distance
                            )
                        ).toString()
                    }
                }

                Sensor.TYPE_ORIENTATION -> {

                    if (this.isNotEmpty())
                    {
                        if (this.size == 3) {
                            val xAxis = this[0]
                            val yAxis = this[1]
                            val zAxis = this[2]

                            sensor_measurement?.text = StringBuilder()
                                .append("x: ")
                                .append(getSensorValueUnit(decimalFormat.format(xAxis)))
                                .append("\ny: ")
                                .append(getSensorValueUnit(decimalFormat.format(yAxis)))
                                .append("\nz: ")
                                .append(getSensorValueUnit(decimalFormat.format(zAxis))).toString()

                        }
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.i(TAG, "on accuracy changed, sensor: $sensor, accuracy: $accuracy")
        setAccuracy(accuracy)
        max_range_text?.text = getSensorValueUnit(sensor?.maximumRange)
        resolution_text?.text = getSensorValueUnit(sensor?.resolution)
        min_delay_text?.text = "${sensor?.minDelay} μs"
        power_text?.text = "${sensor?.power} mA"
    }

    private fun getSensorValueUnit(value: Any?): String = StringBuilder().append(value).append(" ").append(
        sensorValueUnit
    ).toString()

    private fun setAccuracy(accuracy: Int) {
        when (accuracy) {
            SensorManager.SENSOR_STATUS_NO_CONTACT -> {
                accuracy_text?.apply {
                    text = "No contact"
                    setTextColor(ContextCompat.getColor(AppContext.appContext, R.color.alert_red))
                }
            }

            SensorManager.SENSOR_STATUS_UNRELIABLE -> {
                accuracy_text?.apply {
                    text = "Unreliable"
                    setTextColor(ContextCompat.getColor(AppContext.appContext, R.color.alert_red))
                }
            }

            SensorManager.SENSOR_STATUS_ACCURACY_LOW -> {
                accuracy_text?.apply {
                    text = "Low accuracy"
                    setTextColor(ContextCompat.getColor(AppContext.appContext, R.color.alert_red))
                }
            }

            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> {
                accuracy_text?.apply {
                    text = "Medium accuracy"
                    setTextColor(
                        ContextCompat.getColor(
                            AppContext.appContext,
                            R.color.horizon_sky_to
                        )
                    )
                }
            }

            SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> {
                accuracy_text?.apply {
                    text = "High accuracy"
                    setTextColor(ContextCompat.getColor(AppContext.appContext, R.color.green))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        animator?.end()
        animator = null
        sensorManager.unregisterListener(this)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
        {
            triggerEventListener?.let { triggerEvent ->
                sensorManager.cancelTriggerSensor(triggerEvent,sensor)
            }
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

            sensorEventCallback?.let { sensorEventCallback ->
                sensorManager.unregisterListener(sensorEventCallback)
            }
        }
    }
}