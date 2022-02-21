@file:Suppress("DEPRECATION")

package com.example.datasaverexampleapp.vibration

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityVibrationExampleBinding
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VibrationExampleActivity : AppCompatActivity()
{
    private var vibrator:Vibrator? = null
    private val pattern = longArrayOf(1500, 800, 800, 800)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vibration_example)
        title = "Vibration example"
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        DataBindingUtil.setContentView<ActivityVibrationExampleBinding>(
            this, Layout.activity_vibration_example
        )?.let { binding ->

            vibrator?.apply {

                binding.vibratePatternButton.setOnClickListener {

                    if (hasVibrator())
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        {
                            val vibrate = VibrationEffect.createWaveform(pattern, 0) // 0 for repeating
                            vibrate(vibrate) // Execute vibration pattern
                        } else {
                            vibrate(pattern,0) // Execute vibration pattern '0' for repeating
                        }

                        CoroutineScope(Dispatchers.Main).launch {

                            delay(20000)
                            cancel()
                            Toast.makeText(this@VibrationExampleActivity,"Vibrator cancelled!",Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                binding.vibrateOnceButton.setOnClickListener {

                    if (hasVibrator())
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        {
                            val vibrate = VibrationEffect.createWaveform(pattern, -1) // -1 disable repeating
                            vibrate(vibrate) // Execute vibration pattern
                        } else {
                            vibrate(pattern,-1) // Execute vibration pattern '-1' disable repeating
                        }
                    }
                }

                binding.vibrateForSecondButton.setOnClickListener {

                    if (hasVibrator())
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        {
                            val vibrate = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE) // -1 disable repeating
                            vibrate(vibrate) // Execute vibration pattern
                        } else {
                            vibrate(1000) // Execute vibration pattern '-1' disable repeating
                        }
                    }
                }
            }
        }
    }
}