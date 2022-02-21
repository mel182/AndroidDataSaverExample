@file:Suppress("DEPRECATION", "UNNECESSARY_SAFE_CALL", "SimpleRedundantLet")

package com.example.datasaverexampleapp.hardware_sensor.general

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.iterator
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.base_classes.BaseActivity
import com.example.datasaverexampleapp.databinding.ActivityHardwareSensorBinding
import com.example.datasaverexampleapp.type_alias.Layout
import com.example.datasaverexampleapp.type_alias.ViewByID
import com.google.android.material.bottomsheet.BottomSheetBehavior

// NOTE: Where a sensor is required for your application to function, you can specify it as a required feature in the application's manifest.
//
// BEST PRACTICES FOR WORKING WITH SENSOR
// Using Sensors in your applications can be incredibly powerful; like al good things, their use comes at a price, primarily the cost of increased battery drain.
// You should follow several best practices to ensure you make the most of the device Sensors, without having a negative overall impact on the user experience:
// * ALWAYS VERIFY SENSORS EXIST BEFORE ATTEMPTING TO USE THEM
// * PROVIDE ALTERNATIVES TO SENSOR INPUT
// * DON'T USE DEPRECATED SENSOR TYPES
// * BE CONSERVATIVE WHEN SELECTING SENSOR REPORTING FREQUENCIES
// * DON'T BLOCK THE 'onSensorChanged' HANDLER
// * UNREGISTER YOUR SENSOR EVENT LISTENERS
//
class HardwareSensorActivity : BaseActivity(Layout.activity_hardware_sensor),
    OnSensorClickedCallback {

    private val TAG = "HARDWARE_SENSOR"
    private var sensorListAdapter: SensorListAdapter? = null
    private val sensorManager by lazy {
        // The sensor manager is used to manage the sensor hardware available on Android devices. Use the 'getSystemService' to return a reference to the Sensor Manager Service.
        getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Rather than interact with the sensor hardware directly, you work with a series of 'Sensor' objects that represent that hardware. These 'sensor' object describe the properties
        // of the hardware sensor they represent, including the 'type','name','manufacturer' and details on accuracy and range.
        // The 'Sensor' class includes a set of constants that describe which type of hardware sensor is being represented by a particular 'Sensor' object. These constants take the form of
        // 'Sensor.TYPE_ ' followed by the name of a supported Sensor.
        //
        // Sensor can generally be divided into two categories: physical hardware sensors and virtual sensors.
        // Physical sensor e.g.: Light, Barometric etc.. (These typically work independently of each other, each reporting the results obtained from a particular piece of hardware and
        //                                                generally don't apply any filtering or smoothing)
        // Virtual Sensors e.g.: filtered combination of accelerometers , magnetic-field Sensors and gyroscope and orientation sensor. (represent a simplified, corrected, or composite
        //                                                                                                                              sensor data in a way that makes them easier to use
        //                                                                                                                              within some applications)
    }

    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private var menu: Menu? = null
    private var itemsEnabled = true
    private var selectedMenuItemID = 0

    val normalHeight = 0.5f
    val adjustedHeight = 0.7f

    private var binding: ActivityHardwareSensorBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityHardwareSensorBinding>(
            this, Layout.activity_hardware_sensor
        ).apply {
            showAllSensor()

            resources.displayMetrics?.also { displayMetrics ->

                bottomSheetItem.bottomSheet.let { bottomSheetView ->

                    val layoutParams = bottomSheetView.layoutParams
                    layoutParams.height = (displayMetrics.heightPixels * normalHeight).toInt()
                    bottomSheetView.layoutParams = layoutParams

                    BottomSheetBehavior.from(bottomSheetItem.bottomSheet).apply {
                        bottomSheetBehavior = this
                        peekHeight = 0
                        addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                            @SuppressLint("SwitchIntDef")
                            override fun onStateChanged(bottomSheet: View, newState: Int) {

                                when (newState) {
                                    BottomSheetBehavior.STATE_EXPANDED -> {
                                        listOverlay.setOnClickListener {
                                            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                                        }
                                    }

                                    BottomSheetBehavior.STATE_COLLAPSED -> {
                                        listOverlay.apply {
                                            visibility = View.GONE
                                            alpha = 0f
                                        }
                                        removeReadingFragment()
                                    }
                                }
                            }

                            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                                listOverlay.alpha = slideOffset
                            }
                        })
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.sensor_option_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        this.menu = menu
        disableUnsupportedMenuItems()
        return true
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun disableUnsupportedMenuItems() {
        this.menu?.also { menuItem ->

            // ---- JELLY BEAN MR2 ---- \\
            val significantMotion = menuItem.findItem(ViewByID.significant_motion)
            val magneticFieldUncalibrated = menuItem.findItem(ViewByID.magnetic_field_uncalibrated)
            val gameRotationVector = menuItem.findItem(ViewByID.game_rotation_vector)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                setMenuItemState(significantMotion)
                setMenuItemState(magneticFieldUncalibrated)
                setMenuItemState(gameRotationVector)
            } else {
                menuItem.removeItem(ViewByID.significant_motion)
                menuItem.removeItem(ViewByID.magnetic_field_uncalibrated)
                menuItem.removeItem(ViewByID.game_rotation_vector)
            }
            // ---- JELLY BEAN MR2 ---- \\

            // ---- KITKAT ---- \\
            val geomagneticRotationVector = menuItem.findItem(ViewByID.geomagnetic_rotation_vector)
            val stepCounterSensor = menuItem.findItem(ViewByID.step_counter)
            val stepDetectorSensor = menuItem.findItem(ViewByID.step_detector)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setMenuItemState(geomagneticRotationVector)
                setMenuItemState(stepCounterSensor)
                setMenuItemState(stepDetectorSensor)
            } else {
                menuItem.removeItem(ViewByID.geomagnetic_rotation_vector)
                menuItem.removeItem(ViewByID.step_counter)
                menuItem.removeItem(ViewByID.step_detector)
            }
            // ---- KITKAT ---- \\

            // ---- KITKAT WATCH ---- \\
            val heartRateSensor = menuItem.findItem(ViewByID.heart_rate)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                setMenuItemState(heartRateSensor)
            } else {
                menuItem.removeItem(ViewByID.heart_rate)
            }
            // ---- KITKAT WATCH ---- \\

            // ---- NOUGAT ---- \\
            val dynamicSensor = menuItem.findItem(ViewByID.dynamic_sensor)
            val pose6D0F = menuItem.findItem(ViewByID.pose_6d0F)
            val motionDetectionSensor = menuItem.findItem(ViewByID.motion_detection_sensor)
            val stationaryDetection = menuItem.findItem(ViewByID.stationary_detection)
            val heartBeatenSensor = menuItem.findItem(ViewByID.heart_beat)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setMenuItemState(dynamicSensor)
                setMenuItemState(pose6D0F)
                setMenuItemState(motionDetectionSensor)
                setMenuItemState(stationaryDetection)
                setMenuItemState(heartBeatenSensor)
            } else {
                menuItem.removeItem(ViewByID.dynamic_sensor)
                menuItem.removeItem(ViewByID.pose_6d0F)
                menuItem.removeItem(ViewByID.motion_detection_sensor)
                menuItem.removeItem(ViewByID.stationary_detection)
                menuItem.removeItem(ViewByID.heart_beat)
            }
            // ---- NOUGAT ---- \\

            // ---- OREO ---- \\
            val lowLatencyOffbodyDetectSensor =
                menuItem.findItem(ViewByID.low_latency_offbody_detect)
            val gyroscopeUncalibrated = menuItem.findItem(ViewByID.gyroscope_uncalibrated)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setMenuItemState(lowLatencyOffbodyDetectSensor)
                setMenuItemState(gyroscopeUncalibrated)
            } else {
                menuItem.removeItem(ViewByID.low_latency_offbody_detect)
                menuItem.removeItem(ViewByID.gyroscope_uncalibrated)
            }
            // ---- OREO ---- \\
        }
    }

    @SuppressLint("InlinedApi")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        enableMenuItems()
        selectedMenuItemID = item.itemId

        return when (item.itemId) {
            ViewByID.dynamic_sensor -> {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setCheckState(item) { succeed ->
                        if (succeed)
                            showDynamicSensors()
                    }
                }
                true
            }

            ViewByID.ambient_temperature_sensor -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
                    // This is a thermometer that returns the ambient room temperature in degrees celsius (introduced in Android 4.0 API Level 14)
                }
                true
            }

            ViewByID.relative_humidity_sensor -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
                    // A relative humidity sensor that returns the relative humidity as a percentage. (API level 14)
                }
                true
            }

            ViewByID.accelerometer_sensor -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_ACCELEROMETER)
                    // A three-axis accelerometer that returns the current acceleration along three in m/s2.
                    // The accelerometer is explored is greater detail later in this chapter.
                    //
                    // Note: Accelerometer are also known as gravity sensors because they measure acceleration caused both by movement and by gravity. As a result
                    //       , an accelerometer detecting acceleration on an axis perpendicular to the earth's surface will read -9.8m/s2 when it's at rest.
                    //
                    // Note: It's important to note that that accelerometers do not measure velocity, so you can't measure speed directly based on single accelerometer
                    //       reading.
                    //       Instead, you need to integrate the acceleration over time to find the velocity. You can then integrate the velocity over time to determine
                    //       the distance traveled.
                }
                true
            }

            ViewByID.gyroscope_sensor -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_GYROSCOPE)
                    // A three-axis gyroscope that returns the rate of device rotation along three axes in radians/second. You can integrate the rate of rotation over time
                    // to determine the current orientation of the device; however, it generally is better practice to use this in combination with others sensors (typically
                    // the accelerometers) to provide a smoothed and corrected orientation. You can more the gyroscope later in this chapter.
                }
                true
            }

            ViewByID.gyroscope_uncalibrated -> {
                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED)
                }
                true
            }

            ViewByID.linear_acceleration -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_LINEAR_ACCELERATION)
                    // A three-axis linear acceleration 'Sensor' that returns the acceleration, minus gravity, along three axes in m/s2.
                    // Like the gravity sensor, the linear acceleration is typically implemented as virtual sensor using the accelerometer
                    // output. In this case, to obtain the linear acceleration, a high-pass filter is applied to the accelerometer output.
                }
                true
            }

            ViewByID.rotation_vector -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_ROTATION_VECTOR)
                    // Returns the orientation of the device as a combination of an angle around an axis. It typically is used as an input to the 'getRotationMatrixFromVector'
                    // method from the Sensor Manager to convert the returned rotation vector into a rotation matrix. The rotation vector is typically implemented as virtual sensor
                    // that can combine and correct the results obtained from multiple sensors, such as the accelerometers and gyroscope, to provide a smoother rotation matrix.
                }
                true
            }

            ViewByID.geomagnetic_rotation_vector -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR)
                    // An alternative to the rotation vector, implemented as virtual Sensor using the magnetometer rather than gyroscope. As a result,
                    // it uses lower power but is noisier and best used outdoors (Api Level 19).
                }
                true
            }

            ViewByID.pose_6d0F -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_POSE_6DOF)
                    // A pose sensor with 6 degrees of freedom; similar to the rotation vector, but with an additional delta translation from an arbitrary
                    // reference point. This is a high-power sensor that is expected to be more accurate than the rotation vector. (Api Level 24)
                }
                true
            }

            ViewByID.motion_detection_sensor -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_MOTION_DETECT)
                    // A virtual Sensor that returns a value of 1.0 if it determines that the device has been in motion for at least 5 seconds, with a maximum
                    // latency of another 5 seconds. (Api level 24)
                }
                true
            }

            ViewByID.stationary_detection -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_STATIONARY_DETECT)

                    // A virtual Sensor that returns a value of 1.0 if it determines that the device has been stationary for at least 5 seconds. (Api Level 24)
                }
                true
            }

            ViewByID.significant_motion -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_SIGNIFICANT_MOTION)
                    // A one-shot Sensor that is triggered when a significant device movement is detected, and then automatically disables itself to prevent
                    // further results. This is wakeup sensor, meaning that it will continue to monitor for changes while the device is asleep, and will wake
                    // the device when motion is detected. (Api level 18)
                }
                true
            }

            ViewByID.heart_beat -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_HEART_BEAT)
                    // A sensor that monitors heart-beats, returning a single value whenever a heart-beat is detected, corresponding to the positive peak in the QRS complex
                    // of an ECG signal (APi level 24).
                }
                true
            }

            ViewByID.heart_rate -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_HEART_RATE)
                    // A heart-rate monitor that returns a single value describing the user's heart rate in beats-per-minute (bpm) (API Level 26)
                }
                true
            }

            ViewByID.low_latency_offbody_detect -> {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    setCheckState(item) { succeed ->
                        if (succeed)
                            filterSensor(Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT)
                        // Returns a single value whenever a wearable device transitions from being in contact/not in contact with a person's body. (Api Level 26)
                    }
                }
                true
            }

            ViewByID.step_counter -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_STEP_COUNTER)
                    // Returns the cumulative numbers of steps detected while active since the last device reboot. This sensor is implemented as a low-power hardware Sensor
                    // that can be used to continuously track steps over a long period of time. Unlike most of the Sensors described, you should not unregister this Sensor when
                    // your Activity is stopped if you want to continue steps while your app is in the background. (Api level 19)
                }
                true
            }

            ViewByID.step_detector -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_STEP_DETECTOR)
                    // Returns a single value of 1.0 whenever a step is taken, corresponding with the foot touching the ground. If you want to track the number of steps, the steps
                    // counter sensor is more appropriate. (Api Level 19).
                }
                true
            }

            ViewByID.magnetic_field_uncalibrated -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED)
                }
                true
            }

            ViewByID.magnetic_field -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_MAGNETIC_FIELD)
                    // A magnetometer that find the current magnetic field in
                    // microteslas (μT) along three axes.
                }
                true
            }

            ViewByID.pressure_sensor -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_PRESSURE)
                    // An atmospheric pressure sensor (barometer) that returns the current atmospheric pressure
                    // in millibars (mbars) as a single value. The pressure sensor can be used to determine altitude using the 'getAltitude' method on the
                    // Sensor Manager to compare the atmospheric pressure in two locations. Barometers can also be used in weather forecasting by measuring changes in atmospheric
                    // pressure in the same location over time.
                }
                true
            }

            ViewByID.game_rotation_vector -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)
                }
                true
            }

            ViewByID.light_sensor -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_LIGHT)
                    // An ambient light sensor that returns a single value
                    // describing the ambient illumination in lux. A light
                    // sensor is typically used by the system to alter the screen
                    // brightness dynamically.
                }
                true
            }

            ViewByID.proximity_sensor -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_PROXIMITY)
                    // A proximity sensor indicates the distance between the device and the target object in centimeters. How a target object is selected and the distance supported
                    // will depend
                }
                true
            }

            ViewByID.gravity_sensor -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_GRAVITY)
                    // A three-axis gravity sensor that returns the current direction and
                    // magnitude of gravity along three axes in m/s2. The gravity sensor
                    // typically is implemented as virtual sensor by applying a low-pass
                    // filter to the accelerometer results.
                }
                true
            }

            ViewByID.orientation_sensor -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_ORIENTATION)
                }
                true
            }

            ViewByID.custom_sensor -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(null)
                }
                true
            }

            else -> true
        }
    }

    private fun adjustBottomSheetHeight(sensor:Sensor)
    {
        binding?.apply {
            resources.displayMetrics?.also { displayMetrics ->
                bottomSheetItem.bottomSheet.let { bottomSheetView ->
                    val layoutParams = bottomSheetView.layoutParams
                    layoutParams.height = (displayMetrics.heightPixels * getHeight(sensor)).toInt()
                    bottomSheetView.layoutParams = layoutParams
                }
            }
        }
    }

    private fun getHeight(sensor:Sensor) : Float
    {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            if (sensor.isAdditionalInfoSupported)
                adjustedHeight
            else
                normalHeight
        } else {
            normalHeight
        }
    }

    private fun setCheckState(menuItem: MenuItem, callback: (Boolean) -> Unit) {
        if (!menuItem.isChecked) {
            enableMenuItems(enable = false, menuItem)
            menuItem.isChecked = true
        } else {
            menuItem.isChecked = false
            showAllSensor()
        }

        callback(menuItem.isChecked)
    }

    private fun setMenuItemState(menuItem: MenuItem?) {
        menuItem?.isEnabled = if (selectedMenuItemID == menuItem?.itemId) true else itemsEnabled
    }

    private fun enableMenuItems(enable: Boolean = true, selectedMenuItem: MenuItem? = null) {

        this.menu?.also { menu ->

            itemsEnabled = enable

            for (menuItem in menu.iterator()) {
                menuItem.isEnabled = enable
            }

            selectedMenuItem?.isEnabled = true
        }
    }

    @SuppressLint("InlinedApi")
    private fun filterSensor(type: Int?) {

        val filterList = getAllSensors().asSequence().filter {

            if (type == null) {

                it.type.isCustomSensor()
            } else {
                it.type == type
            }
        }.map { it }.toList()
        sensorListAdapter?.updateList(filterList)

        binding?.apply {
            if (filterList.isEmpty()) {
                this@HardwareSensorActivity.title =
                    " No sensor found"
                noSensorDetectedView.visibility = View.VISIBLE
                sensorList.visibility = View.GONE
            } else {
                val sensorPlural = if (filterList.size == 1) "sensor" else "sensors"
                this@HardwareSensorActivity.title =
                    " ${filterList.size} $sensorPlural found"
                noSensorDetectedView.visibility = View.GONE
                sensorList.visibility = View.VISIBLE
            }
        }
    }

    override fun onSensorSelected(selectedSensor: Sensor)
    {
        if (!selectedSensor.type.isCustomSensor()) {
            adjustBottomSheetHeight(selectedSensor)
            setReadingFragment(selectedSensor)
            binding?.listOverlay?.visibility = View.VISIBLE
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            Toast.makeText(this, "Unsupported sensor", Toast.LENGTH_SHORT).show()
        }
    }

    private var sensorReadingFragment: SensorReadingFragment? = null

    private fun setReadingFragment(sensor: Sensor)
    {
        if (sensorReadingFragment != null)
            return

        binding?.apply {
            bottomSheetItem.bottomSheetTitle.text = sensor.name
            supportFragmentManager.beginTransaction().let { fragmentTransaction ->
                sensorReadingFragment = SensorReadingFragment(sensor,sensorManager).apply {
                    fragmentTransaction.add(bottomSheetItem.bottomSheetContent.id,this)
                    fragmentTransaction.commit()
                }
            }
        }
    }

    private fun removeReadingFragment()
    {
        sensorReadingFragment?.apply {
            this@HardwareSensorActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            sensorReadingFragment = null
        }
    }

    private fun showAllSensor() {
        setNoSensorTitle()

        // You can determine if a particular type of Sensor is available on the host device using Sensor Manager's
        // 'getDefaultSensor' method, passing in the relevant Sensor.TYPE_ constant. If no Sensor of that type
        // is available, 'null' will be returned; if one or more Sensors are available, the default implementation will be returned.
        //
        // The Sensor.TYPE_ALL to return a list of every Sensor.
        //
        // sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE) : To find a list of all available Sensors of a particular type, use the Sensor constants
        //                                                      to indicate the type of Sensor you require, this example returns all the available gyroscopes.
        //
        // sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY, TRUE): This overload implementation of 'getDefaultSensor' that takes both a Sensor type
        //                                                              and a Boolean indicating if you specifically require a wakeup sensor.
        binding?.apply {
            sensorManager.getSensorList(Sensor.TYPE_ALL)?.also { sensor_list ->

                if (sensor_list.isEmpty()) {
                    title = "Hardware sensor (No sensor detected)"
                    noSensorDetectedView.visibility = View.VISIBLE
                    sensorList.visibility = View.GONE
                } else {
                    noSensorDetectedView.visibility = View.GONE
                    sensorList.visibility = View.VISIBLE
                    val sensorCount = if (sensor_list.size > 1) "sensor" else "sensors"
                    title = "Hardware sensor (${sensor_list.size} $sensorCount detected)"

                    sensor_list?.apply {
                        sensorListAdapter?.let { adapter ->
                            adapter.updateList(sensor_list)
                        } ?: kotlin.run {
                            sensorList.apply {
                                val targetLayoutManager = LinearLayoutManager(this@HardwareSensorActivity)
                                layoutManager = targetLayoutManager
                                val dividerItemDecoration =
                                    DividerItemDecoration(context, targetLayoutManager.orientation)
                                addItemDecoration(dividerItemDecoration)
                                sensorListAdapter = SensorListAdapter(sensor_list, this@HardwareSensorActivity)
                                adapter = sensorListAdapter
                            }
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showDynamicSensors() {
        // Introduced the concept of 'dynamic' Sensors, primarily to support the Android Things platform.
        // Dynamic Sensors behave like traditional Sensors but can be connected or disconnected at run time.
        setNoSensorTitle()
        this@HardwareSensorActivity.title = "Dynamic sensors"

        binding?.apply {

            if (sensorManager.isDynamicSensorDiscoverySupported) {
                // You can determine if dynamic Sensors are available on the current host platform using the Sensor
                // Manager's 'isDynamicSensorDiscoverySupported' method. To determine if a specific Sensor is dynamic,
                // call its 'isDynamicSensor' method.
                sensorManager.getDynamicSensorList(Sensor.TYPE_ALL)?.also { dynamicSensorList ->

                    if (dynamicSensorList.isEmpty()) {
                        this@HardwareSensorActivity.title = "No dynamic sensor found"
                        noSensorDetectedView.visibility = View.VISIBLE
                        sensorList.visibility = View.GONE
                    } else {
                        val sensorPlural = if (dynamicSensorList.size > 0) "sensor" else "sensors"
                        this@HardwareSensorActivity.title =
                            " ${dynamicSensorList.size} Dynamic $sensorPlural found"
                        noSensorDetectedView.visibility = View.GONE
                        sensorList.visibility = View.VISIBLE
                        sensorListAdapter?.updateList(dynamicSensorList)
                    }
                }
            } else {
                setNoSensorTitle(false)
                noSensorDetectedView.visibility = View.VISIBLE
                sensorList.visibility = View.GONE
            }
        }
    }

    private fun setNoSensorTitle(noSensorFound: Boolean = true) {
        binding?.noSensorTitle?.text = if (noSensorFound)
            "No sensor detected!"
        else
            "Dynamic sensor discovery feature not supported"
    }

    private fun getAllSensors(): List<Sensor> {
        return sensorManager.getSensorList(Sensor.TYPE_ALL)
    }
}