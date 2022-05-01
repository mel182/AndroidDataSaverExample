package com.example.datasaverexampleapp.kotlin_flow_advance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityKotlinFlowAdvanceExampleBinding
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class KotlinFlowAdvanceExampleActivity : AppCompatActivity(R.layout.activity_kotlin_flow_advance_example) {

    private var binding: ActivityKotlinFlowAdvanceExampleBinding? = null
    private var kotlinFlowAdvanceViewModel: KotlinFlowAdvanceViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kotlinFlowAdvanceViewModel =  ViewModelProvider(this)[KotlinFlowAdvanceViewModel::class.java]
        binding = DataBindingUtil.setContentView<ActivityKotlinFlowAdvanceExampleBinding?>(
            this, Layout.activity_kotlin_flow_advance_example
        ).apply {

            startCountExampleButton.setOnClickListener {

                // To get collect as state implement the following view model compose dependency:
                // - implementation "androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version"
                CoroutineScope(Dispatchers.Main).launch {
//                    kotlinFlowAdvanceViewModel?.collectFlow()?.collect {
//                        countEmitResultText.text = it.toString()
//                    }
                }
            }
        }
    }
}