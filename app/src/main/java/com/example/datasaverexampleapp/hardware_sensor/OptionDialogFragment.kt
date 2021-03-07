package com.example.datasaverexampleapp.hardware_sensor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.datasaverexampleapp.hardware_sensor.book_example.CompassFinalWithSensorActivity
import com.example.datasaverexampleapp.hardware_sensor.book_example.DeviceOrientationActivity
import com.example.datasaverexampleapp.hardware_sensor.book_example.ForceMeterActivity
import com.example.datasaverexampleapp.hardware_sensor.book_example.user_activity_recognition.UserRecognitionActivity
import com.example.datasaverexampleapp.hardware_sensor.general.HardwareSensorActivity
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.android.synthetic.main.item_sensor_options_list.*
import kotlinx.android.synthetic.main.item_sensor_options_list.view.*

class OptionDialogFragment(private val activity:AppCompatActivity) : DialogFragment(), View.OnClickListener
{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(Layout.item_sensor_options_list,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view.examples_list_button?.setOnClickListener(this)
        view.force_meter_example_button?.setOnClickListener(this)
        view.compass_artificial_horizon_button?.setOnClickListener(this)
        view.user_recognition_button?.setOnClickListener(this)
        view.device_orientation_button?.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        v?.let { viewClicked ->

            when(viewClicked.id)
            {
                examples_list_button.id -> {
                    val intent = Intent(activity, HardwareSensorActivity::class.java)
                    startActivity(intent)
                    dismiss()
                }

                force_meter_example_button.id -> {
                    val intent = Intent(activity, ForceMeterActivity::class.java)
                    startActivity(intent)
                    dismiss()
                }

                device_orientation_button.id -> {
                    val intent = Intent(activity, DeviceOrientationActivity::class.java)
                    startActivity(intent)
                    dismiss()
                }

                compass_artificial_horizon_button.id -> {
                    val intent = Intent(activity, CompassFinalWithSensorActivity::class.java)
                    startActivity(intent)
                    dismiss()
                }

                user_recognition_button.id -> {
                    val intent = Intent(activity, UserRecognitionActivity::class.java)
                    startActivity(intent)
                    dismiss()
                }
            }
        }
    }


}