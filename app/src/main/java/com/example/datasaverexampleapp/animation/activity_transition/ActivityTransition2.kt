package com.example.datasaverexampleapp.animation.activity_transition

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityTransition2Binding
import com.example.datasaverexampleapp.type_alias.Layout

class ActivityTransition2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition2)

        DataBindingUtil.setContentView<ActivityTransition2Binding>(
            this, Layout.activity_transition2
        ).apply {
            activity1Button?.setOnClickListener {
                onBackPressed()
            }
        }
    }
}