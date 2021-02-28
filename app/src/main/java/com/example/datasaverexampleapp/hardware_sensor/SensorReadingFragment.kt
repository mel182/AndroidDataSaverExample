package com.example.datasaverexampleapp.hardware_sensor

import android.Manifest.permission.BODY_SENSORS
import android.animation.Animator
import android.animation.AnimatorInflater
import android.hardware.*
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.annotation.RequiresApi
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
class SensorReadingFragment(private var sensor: Sensor?, private var sensorManager: SensorManager?) :
    BaseFragment(Layout.fragment_sensor_reading), SensorEventListener {

    private val TAG = "SENSOR_READING"
    private var sensorValueUnit by SensorUnitDelegate()
    private val decimalFormat by lazy { DecimalFormat("0.0") }
    private var animator: Animator? = null
    private var triggerEventListener : TriggerEventListener? = null
    private var sensorEventCallback : SensorEventCallback? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sensorValueUnit = sensor?.type.toString()
        animator = AnimatorInflater.loadAnimator(
            AppContext.appContext,
            R.animator.object_animator_xml_example
        ).apply {
            setTarget(reading_text)
            interpolator = DecelerateInterpolator()
        }
        animator?.start()

        setSensorAccuracyData(sensor)

        if (sensor?.type == Sensor.TYPE_STEP_DETECTOR)
        {
            sensor_measurement?.text = "Step detector sensor"
        } else if (sensor?.type == Sensor.TYPE_SIGNIFICANT_MOTION)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

                triggerEventListener = object : TriggerEventListener() {
                    override fun onTrigger(event: TriggerEvent?) {

                        event?.values.apply {
                            sensor_measurement?.text = "Significant motion detected!"
                        }
                    }
                }
                sensor_measurement?.text = "Trigger in special events...."
                requestTriggerSensor()
            } else {
                sensor_measurement?.text = "Unsupported"
            }
        } else if (sensor?.type == Sensor.TYPE_HEART_RATE)
        {
            if (isPermissionGranted(BODY_SENSORS))
            {
                requestSensorListener()
            } else {
                requestPermission(BODY_SENSORS){ granted ->

                    if (granted)
                        requestSensorListener()
                }
            }
        } else {

            sensor?.let { measureSensor ->

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    if (measureSensor.isAdditionalInfoSupported) {

                        sensorEventCallback = object : SensorEventCallback() {

                            override fun onSensorChanged(event: SensorEvent?) {
                                super.onSensorChanged(event)
                                event?.values?.apply {
                                    updateMeasurementValue(this)
                                }
                            }

                            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                                super.onAccuracyChanged(sensor, accuracy)
                                setAccuracy(accuracy)
                                setSensorAccuracyData(sensor)
                            }

                            override fun onSensorAdditionalInfo(info: SensorAdditionalInfo?) {
                                super.onSensorAdditionalInfo(info)
                                //TODO: Implement sensor additional info
                            }
                        }
                        requestSensorListener()
                    } else {
                        requestSensorListener()
                    }
                } else {
                    requestSensorListener()
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?)
    {
        event?.values?.apply {
            updateMeasurementValue(this)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int)
    {
        setAccuracy(accuracy)
        setSensorAccuracyData(sensor)
    }

    private fun updateMeasurementValue(values:FloatArray)
    {
        when (sensor?.type) {
            Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GRAVITY, Sensor.TYPE_LINEAR_ACCELERATION -> {

                if (values.size == 3) {
                    val xAxisForce = values[0]
                    val yAxisForce = values[1]
                    val zAxisForce = values[2]

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

                if (values.size == 6) {
                    val xAxisAcceleration = values[0]
                    val yAxisAcceleration = values[1]
                    val zAxisAcceleration = values[2]

                    val xAxisAccelerationWithEstimate = values[3]
                    val yAxisAccelerationWithEstimate = values[4]
                    val zAxisAccelerationWithEstimate = values[5]

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

                if (values.size == 3) {
                    val xAxisForce = values[0]
                    val yAxisForce = values[1]
                    val zAxisForce = values[2]

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

                if (values.size == 6) {
                    val xAxisForce = values[0]
                    val yAxisForce = values[1]
                    val zAxisForce = values[2]

                    val xAxisForceDrift = values[3]
                    val yAxisForceDrift = values[4]
                    val zAxisForceDrift = values[5]

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

                if (values.size == 3) {
                    val geomagneticXAxis = values[0]
                    val geomagneticYAxis = values[1]
                    val geomagneticZAxis = values[2]

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

                if (values.size == 6) {
                    val geomagneticXAxis = values[0]
                    val geomagneticYAxis = values[1]
                    val geomagneticZAxis = values[2]

                    val geomagneticXAxisWithEstimate = values[3]
                    val geomagneticYAxisWithEstimate = values[4]
                    val geomagneticZAxisWithEstimate = values[5]

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

                if (values.isNotEmpty()) {
                    val pressureReading = decimalFormat.format(values[0])
                    sensor_measurement?.text = StringBuilder().append(
                        getSensorValueUnit(
                            pressureReading
                        )
                    ).toString()
                }
            }

            Sensor.TYPE_AMBIENT_TEMPERATURE, Sensor.TYPE_TEMPERATURE -> {

                if (values.isNotEmpty()) {
                    val pressureReading = decimalFormat.format(values[0])
                    sensor_measurement?.text = StringBuilder().append(
                        getSensorValueUnit(
                            pressureReading
                        )
                    ).toString()
                }
            }

            Sensor.TYPE_STEP_COUNTER -> {

                if (values.isNotEmpty()) {
                    val pressureReading = decimalFormat.format(values[0])
                    sensor_measurement?.text = StringBuilder().append(
                        getSensorValueUnit(
                            pressureReading
                        )
                    ).toString()
                }
            }

            Sensor.TYPE_LIGHT -> {

                if (values.isNotEmpty()) {
                    val illuminanceReading = decimalFormat.format(values[0])
                    sensor_measurement?.text = StringBuilder().append(
                        getSensorValueUnit(
                            illuminanceReading
                        )
                    ).toString()
                }
            }

            Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT -> {

                if (values.isNotEmpty()) {
                    val typeLowLatencyOffBodyDetectReading = decimalFormat.format(values[0])
                    sensor_measurement?.text = StringBuilder().append(
                        getSensorValueUnit(
                            typeLowLatencyOffBodyDetectReading
                        )
                    ).toString()
                }
            }

            Sensor.TYPE_PROXIMITY -> {

                if (values.isNotEmpty()) {
                    val distance = decimalFormat.format(values[0])
                    sensor_measurement?.text = StringBuilder().append(
                        getSensorValueUnit(
                            distance
                        )
                    ).toString()
                }
            }

            Sensor.TYPE_HEART_RATE -> {

                if (values.isNotEmpty())
                {
                    val distance = decimalFormat.format(values[0])
                    sensor_measurement?.text = StringBuilder().append(
                        getSensorValueUnit(
                            distance
                        )
                    ).toString()
                }
            }

            Sensor.TYPE_HEART_BEAT -> {

                if (values.isNotEmpty())
                {
                    val distance = decimalFormat.format(values[0])
                    sensor_measurement?.text = StringBuilder().append(
                        getSensorValueUnit(
                            distance
                        )
                    ).toString()
                }
            }

            Sensor.TYPE_ORIENTATION -> {

                if (values.isNotEmpty())
                {
                    if (values.size == 3) {
                        val xAxis = values[0]
                        val yAxis = values[1]
                        val zAxis = values[2]

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

            Sensor.TYPE_RELATIVE_HUMIDITY -> {

                if (values.isNotEmpty())
                {
                    val distance = decimalFormat.format(values[0])
                    sensor_measurement?.text = StringBuilder().append(
                        getSensorValueUnit(
                            distance
                        )
                    ).toString()
                }
            }

            Sensor.TYPE_POSE_6DOF -> {
                if (values.isNotEmpty())
                {
                    if (values.size == 3) {
                        val xAxis = values[0]
                        val yAxis = values[1]
                        val zAxis = values[2]

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

            Sensor.TYPE_STATIONARY_DETECT -> {
                if (values.isNotEmpty())
                {
                    val stationaryValue = decimalFormat.format(values[0])
                    sensor_measurement?.text = StringBuilder().append(
                        getSensorValueUnit(
                            stationaryValue
                        )
                    ).toString()
                }
            }

            Sensor.TYPE_MOTION_DETECT -> {
                if (values.isNotEmpty())
                {
                    val stationaryValue = decimalFormat.format(values[0])
                    sensor_measurement?.text = StringBuilder().append(
                        getSensorValueUnit(
                            stationaryValue
                        )
                    ).toString()
                }
            }

            Sensor.TYPE_GAME_ROTATION_VECTOR -> {

                if (values.size == 3) {
                    val xAxis = values[0]
                    val yAxis = values[1]
                    val zAxis = values[2]

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

    private fun setSensorAccuracyData(sensor: Sensor?)
    {
        max_range_text?.text = getSensorValueUnit(sensor?.maximumRange)
        resolution_text?.text = getSensorValueUnit(sensor?.resolution)
        min_delay_text?.text = StringBuilder().append(sensor?.minDelay).append(" μs").toString()
        power_text?.text = StringBuilder().append(sensor?.power).append(" mA").toString()
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

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun requestTriggerSensor()
    {
        if (triggerEventListener == null || sensor == null)
            return

        sensorManager?.requestTriggerSensor(triggerEventListener,sensor)
    }

    private fun requestSensorListener()
    {
        if (sensor == null)
            return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            if (sensorEventCallback != null)
            {
                sensorManager?.registerListener(sensorEventCallback, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            } else {
                sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            }
        } else {
            sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        animator?.end()
        animator = null
        sensorManager?.unregisterListener(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
        {
            triggerEventListener?.let { triggerEvent ->
                sensorManager?.cancelTriggerSensor(triggerEvent,sensor)
            }
            triggerEventListener = null
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            sensorEventCallback?.let { sensorEventCallback ->
                sensorManager?.unregisterListener(sensorEventCallback)
            }
            sensorEventCallback = null
        }

        sensor = null
        sensorManager = null
    }
}