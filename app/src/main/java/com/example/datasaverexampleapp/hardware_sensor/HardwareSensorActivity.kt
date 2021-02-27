package com.example.datasaverexampleapp.hardware_sensor

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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.base_classes.BaseActivity
import com.example.datasaverexampleapp.type_alias.Layout
import com.example.datasaverexampleapp.type_alias.ViewByID
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_hardware_sensor.*
import kotlinx.android.synthetic.main.item_bottom_sheet_layout.*


class HardwareSensorActivity : BaseActivity(Layout.activity_hardware_sensor),
    OnSensorClickedCallback {

    private val TAG = "HARDWARE_SENSOR"
    private var sensorListAdapter: SensorListAdapter? = null
    private val sensorManager by lazy { getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private var menu: Menu? = null
    private var itemsEnabled = true
    private var selectedMenuItemID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showAllSensor()

        resources.displayMetrics?.also { displayMetrics ->

            bottom_sheet?.let { bottomSheetView ->

                val layoutParams = bottomSheetView.layoutParams
                layoutParams.height = (displayMetrics.heightPixels * 0.5).toInt()
                bottomSheetView.layoutParams = layoutParams

                BottomSheetBehavior.from(bottom_sheet).apply {
                    bottomSheetBehavior = this
                    peekHeight = 0
                    addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(bottomSheet: View, newState: Int) {

                            when (newState) {
                                BottomSheetBehavior.STATE_EXPANDED -> {
                                    list_overlay?.setOnClickListener {
                                        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                                    }
                                }

                                BottomSheetBehavior.STATE_COLLAPSED -> {
                                    list_overlay?.apply {
                                        visibility = View.GONE
                                        alpha = 0f
                                    }
                                    removeReadingFragment()
                                }
                            }
                        }

                        override fun onSlide(bottomSheet: View, slideOffset: Float) {
                            list_overlay?.alpha = slideOffset
                        }
                    })
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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

            ViewByID.accelerometer_sensor -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_ACCELEROMETER)
                }
                true
            }

            ViewByID.gyroscope_sensor -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_GYROSCOPE)
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
                }
                true
            }

            ViewByID.rotation_vector -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_ROTATION_VECTOR)
                }
                true
            }

            ViewByID.geomagnetic_rotation_vector -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR)
                }
                true
            }

            ViewByID.pose_6d0F -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_POSE_6DOF)
                }
                true
            }

            ViewByID.motion_detection_sensor -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_MOTION_DETECT)
                }
                true
            }

            ViewByID.stationary_detection -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_STATIONARY_DETECT)
                }
                true
            }

            ViewByID.significant_motion -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_SIGNIFICANT_MOTION)
                }
                true
            }

            ViewByID.heart_beat -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_HEART_BEAT)
                }
                true
            }

            ViewByID.heart_rate -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_HEART_RATE)
                }
                true
            }

            ViewByID.low_latency_offbody_detect -> {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    setCheckState(item) { succeed ->
                        if (succeed)
                            filterSensor(Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT)
                    }
                }
                true
            }

            ViewByID.step_counter -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_STEP_COUNTER)
                }
                true
            }

            ViewByID.step_detector -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_STEP_DETECTOR)
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
                }
                true
            }

            ViewByID.pressure_sensor -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_PRESSURE)
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
                }
                true
            }

            ViewByID.proximity_sensor -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_PROXIMITY)
                }
                true
            }

            ViewByID.gravity_sensor -> {

                setCheckState(item) { succeed ->
                    if (succeed)
                        filterSensor(Sensor.TYPE_GRAVITY)
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

//                it.type != Sensor.TYPE_STEP_DETECTOR && it.type != Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT
//                        && it.type != Sensor.TYPE_HEART_RATE && it.type != Sensor.TYPE_HEART_BEAT
//                        && it.type != Sensor.TYPE_SIGNIFICANT_MOTION && it.type != Sensor.TYPE_STATIONARY_DETECT
//                        && it.type != Sensor.TYPE_MOTION_DETECT && it.type != Sensor.TYPE_POSE_6DOF
//                        && it.type != Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR && it.type != Sensor.TYPE_ROTATION_VECTOR
//                        && it.type != Sensor.TYPE_LINEAR_ACCELERATION && it.type != Sensor.TYPE_GYROSCOPE
//                        && it.type != Sensor.TYPE_ACCELEROMETER && it.type != Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED
//                        && it.type != Sensor.TYPE_MAGNETIC_FIELD && it.type != Sensor.TYPE_PRESSURE
//                        && it.type != Sensor.TYPE_GAME_ROTATION_VECTOR && it.type != Sensor.TYPE_STEP_COUNTER
//                        && it.type != Sensor.TYPE_LIGHT && it.type != Sensor.TYPE_PROXIMITY && it.type != Sensor.TYPE_GRAVITY
//                        && it.type != Sensor.TYPE_ORIENTATION && it.type != Sensor.TYPE_GYROSCOPE_UNCALIBRATED
            } else {
                it.type == type
            }
        }.map { it }.toList()
        sensorListAdapter?.updateList(filterList)

        if (filterList.isEmpty()) {
            this@HardwareSensorActivity.title =
                " No sensor found"
            no_sensor_detected_view?.visibility = View.VISIBLE
            sensor_list?.visibility = View.GONE
        } else {
            val sensorPlural = if (filterList.size == 1) "sensor" else "sensors"
            this@HardwareSensorActivity.title =
                " ${filterList.size} $sensorPlural found"
            no_sensor_detected_view?.visibility = View.GONE
            sensor_list?.visibility = View.VISIBLE
        }
    }

    override fun onSensorSelected(selectedSensor: Sensor)
    {
        if (!selectedSensor.type.isCustomSensor()) {
            setReadingFragment(selectedSensor)
            list_overlay?.visibility = View.VISIBLE
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

        bottom_sheet_title?.text = sensor.name
        supportFragmentManager.beginTransaction().let { fragmentTransaction ->
            sensorReadingFragment = SensorReadingFragment(sensor,sensorManager).apply {
                fragmentTransaction.add(this@HardwareSensorActivity.bottom_sheet_content.id,this)
                fragmentTransaction.commit()
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
        sensorManager.getSensorList(Sensor.TYPE_ALL)?.also { sensorList ->

            if (sensorList.isEmpty()) {
                title = "Hardware sensor (No sensor detected)"
                no_sensor_detected_view?.visibility = View.VISIBLE
                sensor_list?.visibility = View.GONE
            } else {
                no_sensor_detected_view?.visibility = View.GONE
                sensor_list?.visibility = View.VISIBLE
                val sensorCount = if (sensorList.size > 1) "sensor" else "sensors"
                title = "Hardware sensor (${sensorList.size} $sensorCount detected)"

                sensor_list?.apply {
                    sensorListAdapter?.let { adapter ->
                        adapter.updateList(sensorList)
                    } ?: kotlin.run {
                        val targetLayoutManager = LinearLayoutManager(this@HardwareSensorActivity)
                        layoutManager = targetLayoutManager
                        val dividerItemDecoration =
                            DividerItemDecoration(context, targetLayoutManager.orientation)
                        addItemDecoration(dividerItemDecoration)
                        sensorListAdapter =
                            SensorListAdapter(sensorList, this@HardwareSensorActivity)
                        adapter = sensorListAdapter
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showDynamicSensors() {
        setNoSensorTitle()

        this@HardwareSensorActivity.title = "Dynamic sensors"

        if (sensorManager.isDynamicSensorDiscoverySupported) {
            sensorManager.getDynamicSensorList(Sensor.TYPE_ALL)?.also { dynamicSensorList ->

                if (dynamicSensorList.isEmpty()) {
                    this@HardwareSensorActivity.title = "No dynamic sensor found"
                    no_sensor_detected_view?.visibility = View.VISIBLE
                    sensor_list?.visibility = View.GONE
                } else {
                    val sensorPlural = if (dynamicSensorList.size > 0) "sensor" else "sensors"
                    this@HardwareSensorActivity.title =
                        " ${dynamicSensorList.size} Dynamic $sensorPlural found"
                    no_sensor_detected_view?.visibility = View.GONE
                    sensor_list?.visibility = View.VISIBLE
                    sensorListAdapter?.updateList(dynamicSensorList)
                }
            }
        } else {
            setNoSensorTitle(false)
            no_sensor_detected_view?.visibility = View.VISIBLE
            sensor_list?.visibility = View.GONE
        }
    }

    private fun setNoSensorTitle(noSensorFound: Boolean = true) {
        no_sensor_title?.text = if (noSensorFound)
            "No sensor detected!"
        else
            "Dynamic sensor discovery feature not supported"
    }

    private fun getAllSensors(): List<Sensor> {
        return sensorManager.getSensorList(Sensor.TYPE_ALL)
    }
}