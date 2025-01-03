@file:Suppress("DEPRECATION")

package com.example.datasaverexampleapp.hardware_sensor.general

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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.application.AppContext
import com.example.datasaverexampleapp.base_classes.BaseFragment
import com.example.datasaverexampleapp.databinding.FragmentSensorReadingBinding
import com.example.datasaverexampleapp.type_alias.Layout
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
    private var minDelayValue by SensorDelayUnitDelegate()
    private var sensorPowerValue by SensorPowerUnitDelegate()
    private var binding: FragmentSensorReadingBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.apply {
            binding = DataBindingUtil.setContentView<FragmentSensorReadingBinding>(
                this, Layout.fragment_sensor_reading
            ).apply {
                //----
                sensor?.apply {
                    sensorValueUnit = type.toString()
                    minDelayValue = minDelay.toString()
                    sensorPowerValue = power.toString()
                }

                animator = AnimatorInflater.loadAnimator(
                    AppContext.appContext,
                    R.animator.object_animator_xml_example
                ).apply {
                    setTarget(readingText)
                    interpolator = DecelerateInterpolator()
                }
                animator?.start()

                setSensorAccuracyData(sensor)

                if (sensor?.type == Sensor.TYPE_STEP_DETECTOR)
                {
                    sensorMeasurement.text = "Step detector sensor"
                } else if (sensor?.type == Sensor.TYPE_SIGNIFICANT_MOTION)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

                        // The 'TriggerEvent' parameter received by the 'onTrigger' handler includes the following properties
                        // to describe each Trigger Event:
                        // - Sensor: The sensor object that triggered the event
                        // - Values: A float array that contains the new value(s) observed.
                        // - Timestamp: The time (in nanoseconds) at which the Sensor Event occurred
                        triggerEventListener = object : TriggerEventListener() {
                            override fun onTrigger(event: TriggerEvent?) {

                                event?.values.apply {
                                    sensorMeasurement.text = "Significant motion detected!"
                                }
                            }
                        }
                        sensorMeasurement.text = "Trigger in special events...."
                        requestTriggerSensor()
                    } else {
                        sensorMeasurement.text = "Unsupported"
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

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        {
                            if (measureSensor.isAdditionalInfoSupported) {

                                // If a Sensor if capable of returning Sensor Additional Info, you can use
                                // the new 'SensorEventCallback' an extension of the Sensor Event Listener
                                // that includes additional callback handlers.
                                sensorEventCallback = object : SensorEventCallback() {

                                    override fun onSensorChanged(event: SensorEvent?) {
                                        super.onSensorChanged(event)
                                        // ------ Event --------
                                        // - Sensor: The sensor object that triggered the event
                                        // - Accuracy: The accuracy of the Sensor when the event occured
                                        // - Values: A float array that contains the new value(s) observed
                                        // - timestamp: The time (in nanoseconds) at which the Sensor Event occurred
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

                                        info?.let { data ->

                                            additionalInfoSection.visibility = View.VISIBLE
                                            additionalPayloadType.apply {

                                                // Integer and float arrays that may contain payload
                                                // value(s) for the Sensor, as described by the
                                                // information type.
                                                text = if (data.floatValues.isNotEmpty())
                                                {
                                                    "float list size: ${data.floatValues.size}"
                                                } else {
                                                    "int list size: ${data.floatValues.size}"
                                                }
                                            }

                                            additionalFrameTypeText.text = when(data.type)
                                                // Sensors can return multiple types of additional sensor information
                                            {
                                                // Mark the beginning and end of this frame of additional information
                                                SensorAdditionalInfo.TYPE_FRAME_BEGIN -> "Frame begin"
                                                // The internal Sensor temperature, returned in degrees Celsius as the first
                                                // value in the 'floatValues' array.
                                                SensorAdditionalInfo.TYPE_FRAME_END -> "Frame end"
                                                // The raw period sampling period, in seconds, returned as the first value in
                                                // the float array; and the estimated sample time-jitter returned as the standard
                                                // deviation, available in the second value in the float array.
                                                SensorAdditionalInfo.TYPE_SAMPLING -> "Sampling"
                                                // The physical location and angle of the Sensor relative to the device's
                                                // geometric Sensor. The values are returned as a homogeneous matrix in
                                                // the first twelve values in the float array.
                                                SensorAdditionalInfo.TYPE_SENSOR_PLACEMENT -> "Sensor placement"
                                                // The delay to the sensor results introduced by data processing (such as filtering or smoothing)
                                                // which have not been taken into account in the Sensor Event timestamps. The first float array value
                                                // is the estimated delay, the second value is the estimated standard deviation in estimated delays.
                                                SensorAdditionalInfo.TYPE_UNTRACKED_DELAY -> "Untracked delay"
                                                // The vector calibration parameter, representing the calibration applied to a Sensor with
                                                // three-element vector output. Returns a homogeneous matrix in the first 12 values in the
                                                // float array describing ant linear transformation, including rotation, scaling, shear and shift.
                                                SensorAdditionalInfo.TYPE_VEC3_CALIBRATION -> "VEC 3 Calibration"
                                                else -> ""
                                            }

                                            // Each information type returned within a frame is numbered sequentially, with the serial value
                                            // identifying the sequence number within the frame.
                                            additionalSerialNrText.text = data.serial.toString()
                                        }
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
                // ----
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
        binding?.apply {

            when (sensor?.type) {
                Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GRAVITY, Sensor.TYPE_LINEAR_ACCELERATION -> {

                    if (values.size == 3) {
                        val xAxisForce = values[0]
                        val yAxisForce = values[1]
                        val zAxisForce = values[2]

                        sensorMeasurement.text = StringBuilder()
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

                        sensorMeasurement.text = StringBuilder()
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

                        sensorMeasurement.text = StringBuilder()
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

                        sensorMeasurement.text = StringBuilder()
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

                        sensorMeasurement.text = StringBuilder()
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

                        sensorMeasurement.text = StringBuilder()
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
                        sensorMeasurement.text = StringBuilder().append(
                            getSensorValueUnit(
                                pressureReading
                            )
                        ).toString()
                    }
                }

                Sensor.TYPE_AMBIENT_TEMPERATURE, Sensor.TYPE_TEMPERATURE -> {

                    if (values.isNotEmpty()) {
                        val pressureReading = decimalFormat.format(values[0])
                        sensorMeasurement.text = StringBuilder().append(
                            getSensorValueUnit(
                                pressureReading
                            )
                        ).toString()
                    }
                }

                Sensor.TYPE_STEP_COUNTER -> {

                    if (values.isNotEmpty()) {
                        val pressureReading = decimalFormat.format(values[0])
                        sensorMeasurement.text = StringBuilder().append(
                            getSensorValueUnit(
                                pressureReading
                            )
                        ).toString()
                    }
                }

                Sensor.TYPE_LIGHT -> {

                    if (values.isNotEmpty()) {
                        val illuminanceReading = decimalFormat.format(values[0])
                        sensorMeasurement.text = StringBuilder().append(
                            getSensorValueUnit(
                                illuminanceReading
                            )
                        ).toString()
                    }
                }

                Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT -> {

                    if (values.isNotEmpty()) {
                        val typeLowLatencyOffBodyDetectReading = decimalFormat.format(values[0])
                        sensorMeasurement.text = StringBuilder().append(
                            getSensorValueUnit(
                                typeLowLatencyOffBodyDetectReading
                            )
                        ).toString()
                    }
                }

                Sensor.TYPE_PROXIMITY -> {

                    if (values.isNotEmpty()) {
                        val distance = decimalFormat.format(values[0])
                        sensorMeasurement.text = StringBuilder().append(
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
                        sensorMeasurement.text = StringBuilder().append(
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
                        sensorMeasurement.text = StringBuilder().append(
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

                            sensorMeasurement.text = StringBuilder()
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
                        sensorMeasurement.text = StringBuilder().append(
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

                            sensorMeasurement.text = StringBuilder()
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
                        sensorMeasurement.text = StringBuilder().append(
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
                        sensorMeasurement.text = StringBuilder().append(
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

                        sensorMeasurement.text = StringBuilder()
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

    private fun setSensorAccuracyData(sensor: Sensor?)
    {
        binding?.apply {
            maxRangeText.text = getSensorValueUnit(sensor?.maximumRange)
            resolutionText.text = getSensorValueUnit(sensor?.resolution)
            minDelayText.text = minDelayValue
            powerText.text = sensorPowerValue
        }
    }

    private fun getSensorValueUnit(value: Any?): String = StringBuilder().append(value).append(" ").append(
        sensorValueUnit
    ).toString()

    private fun setAccuracy(accuracy: Int) {

        binding?.apply {
            when (accuracy) {
                SensorManager.SENSOR_STATUS_NO_CONTACT -> {
                    accuracyText.apply {
                        // Indicates that the Sensor data is unreliable because the Sensor
                        // has lost contact with what it measures.
                        text = "No contact"
                        setTextColor(ContextCompat.getColor(AppContext.appContext, R.color.alert_red))
                    }
                }

                SensorManager.SENSOR_STATUS_UNRELIABLE -> {
                    accuracyText.apply {
                        // Indicates that the Sensor data is unreliable, meaning that either calibration is
                        // required or readings are not currently possible.
                        text = "Unreliable"
                        setTextColor(ContextCompat.getColor(AppContext.appContext, R.color.alert_red))
                    }
                }

                SensorManager.SENSOR_STATUS_ACCURACY_LOW -> {
                    accuracyText.apply {
                        // Indicates that the Sensor is reporting with low accuracy and needs to be calibrated
                        text = "Low accuracy"
                        setTextColor(ContextCompat.getColor(AppContext.appContext, R.color.alert_red))
                    }
                }

                SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> {
                    accuracyText.apply {
                        // Indicates that the Sensor data is of average accuracy and that calibration
                        // might improve the accuracy of the reported results.
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
                    accuracyText.apply {
                        // Indicates that the Sensor is reporting with the highest possible accuracy
                        text = "High accuracy"
                        setTextColor(ContextCompat.getColor(AppContext.appContext, R.color.green))
                    }
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
                // Register your sensor event listener with the Sensor Manager. Specify the Sensor to observe
                // and the minimum frequency at which you want to receive updates, either in microseconds or
                // using one of the microseconds.
                // Sampling periods options:
                // - SENSOR_DELAY_FASTEST: get sensor data as fast as possible
                // - SENSOR_DELAY_GAME: rate suitable for games
                // - SENSOR_DELAY_UI: rate suitable for the user interface
                // - SENSOR_DELAY_NORMAL: rate (default) suitable for screen orientation changes
                sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            }
        } else {
            // For most Sensors- those that report results continuously, on change, or caused by a special
            // trigger - you receive Sensor Events by implementing a 'SensorEventListener'.
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
                // If you have not received a Trigger Event and your application no longer
                // needs to respond to it, you should cancel your Trigger Event Listeners manually.
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