package com.example.datasaverexampleapp.hardware_sensor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.datasaverexampleapp.databinding.ItemSensorOptionsListBinding
import com.example.datasaverexampleapp.hardware_sensor.book_example.CompassFinalWithSensorActivity
import com.example.datasaverexampleapp.hardware_sensor.book_example.DeviceOrientationActivity
import com.example.datasaverexampleapp.hardware_sensor.book_example.ForceMeterActivity
import com.example.datasaverexampleapp.hardware_sensor.book_example.user_activity_recognition.UserRecognitionActivity
import com.example.datasaverexampleapp.hardware_sensor.general.HardwareSensorActivity
import com.example.datasaverexampleapp.type_alias.Layout

class OptionDialogFragment(private val activity:AppCompatActivity) : DialogFragment(), View.OnClickListener
{
    private var binding: ItemSensorOptionsListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(Layout.item_sensor_options_list,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity.apply {
            binding= DataBindingUtil.setContentView<ItemSensorOptionsListBinding>(
                this, Layout.item_sensor_options_list
            ).apply {
                examplesListButton.setOnClickListener(this@OptionDialogFragment)
                forceMeterExampleButton.setOnClickListener(this@OptionDialogFragment)
                compassArtificialHorizonButton.setOnClickListener(this@OptionDialogFragment)
                userRecognitionButton.setOnClickListener(this@OptionDialogFragment)
                deviceOrientationButton.setOnClickListener(this@OptionDialogFragment)
            }
        }
    }

    override fun onClick(v: View?) {

        binding?.apply {

            v?.let { viewClicked ->

                when(viewClicked.id)
                {
                    examplesListButton.id -> {
                        val intent = Intent(activity, HardwareSensorActivity::class.java)
                        startActivity(intent)
                        dismiss()
                    }

                    forceMeterExampleButton.id -> {
                        val intent = Intent(activity, ForceMeterActivity::class.java)
                        startActivity(intent)
                        dismiss()
                    }

                    deviceOrientationButton.id -> {
                        val intent = Intent(activity, DeviceOrientationActivity::class.java)
                        startActivity(intent)
                        dismiss()
                    }

                    compassArtificialHorizonButton.id -> {
                        val intent = Intent(activity, CompassFinalWithSensorActivity::class.java)
                        startActivity(intent)
                        dismiss()
                    }

                    userRecognitionButton.id -> {
                        val intent = Intent(activity, UserRecognitionActivity::class.java)
                        startActivity(intent)
                        dismiss()
                    }
                }
            }
        }
    }
}