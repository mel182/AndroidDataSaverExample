package com.example.datasaverexampleapp.animation

import android.animation.AnimatorInflater
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_animation_example.*

class AnimationExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation_example)

        alpha_0_1_example_textview?.apply {
            val alphaAnimator = AnimatorInflater.loadAnimator(this@AnimationExampleActivity, R.animator.set_alpha_between_0_1)
            alphaAnimator.setTarget(this)
            alphaAnimator.start()
        }
    }
}