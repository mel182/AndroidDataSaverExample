package com.example.datasaverexampleapp.hardware_sensor

import android.hardware.Sensor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.type_alias.Layout
import com.example.datasaverexampleapp.type_alias.ViewByID

class SensorListAdapter (sensorList:List<Sensor>, private val sensorClickCallback:OnSensorClickedCallback) : RecyclerView.Adapter<SensorListAdapter.ViewHolder>() {

    private val sensorList = ArrayList<Sensor>()

    init {
        this.sensorList.addAll(sensorList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(Layout.item_hardware_sensor_list_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sensorFound = sensorList[position]
        holder.apply {
            mainView.setOnClickListener {
                sensorClickCallback.onSensorSelected(sensorFound)
            }
            bind(sensorFound)
        }
    }

    override fun getItemCount(): Int = sensorList.size

    fun updateList(updateSensorList:List<Sensor>)
    {
        sensorList.apply {
            clear()
            addAll(updateSensorList)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(view:View) : RecyclerView.ViewHolder(view)
    {
        val mainView: LinearLayout = view.findViewById(ViewByID.main_view)
        private val sensorName = view.findViewById<TextView>(ViewByID.sensor_name)
        private val sensorWakeUp = view.findViewById<TextView>(ViewByID.sensor_wake_up)
        private val vendorName = view.findViewById<TextView>(ViewByID.vendor_name)
        private val version = view.findViewById<TextView>(ViewByID.version)
        private val type = view.findViewById<TextView>(ViewByID.type)
        private val maxRange = view.findViewById<TextView>(ViewByID.max_range)
        private val resolution = view.findViewById<TextView>(ViewByID.resolution)
        private val power = view.findViewById<TextView>(ViewByID.power)
        private val minDelay = view.findViewById<TextView>(ViewByID.min_delay)
        private val maxDelay = view.findViewById<TextView>(ViewByID.max_delay)
        private val reportingModeView = view.findViewById<LinearLayout>(ViewByID.reporting_mode_section)
        private val reportingMode = view.findViewById<TextView>(ViewByID.reporting_mode)

        private var sensorType by SensorTypeDelegate()
        private var sensorReportingType by SensorReportingDelegate()

        fun bind(sensor:Sensor)
        {
            sensorType = sensor.type.toString()

            sensorName.text = sensor.name
            vendorName.text = sensor.vendor
            version.text = sensor.version.toString()
            type.text = sensorType
            maxRange.text = sensor.maximumRange.toString()
            resolution.text = sensor.resolution.toString()
            power.text = "${sensor.power}mA"
            minDelay.text = "${sensor.minDelay}μs"

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                maxDelay.apply {
                    visibility = View.VISIBLE
                    text = "${sensor.maxDelay}μs"
                }
                sensorWakeUp.apply {
                    visibility = View.VISIBLE
                    text = if (sensor.isWakeUpSensor) "Wakeup sensor" else "Non-Wakeup sensor"
                }

                reportingModeView.visibility = View.VISIBLE
                sensorReportingType = sensor.reportingMode.toString()
                reportingMode.text = sensorReportingType
            } else {
                maxDelay.visibility = View.GONE
                sensorWakeUp.visibility = View.GONE
                reportingModeView.visibility = View.GONE
            }
        }
    }
}