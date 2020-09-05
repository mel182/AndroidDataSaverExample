package com.example.datasaverexampleapp.animation

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
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

        group_animation_example?.apply {

            setOnClickListener {
                val rotationShrinkAnimation = AnimationUtils.loadAnimation(this@AnimationExampleActivity, R.anim.rotation_shrink)
                startAnimation(rotationShrinkAnimation)
            }
        }

        imageViewAnimation?.apply {
            setBackgroundResource(R.drawable.image_animation)
            val animationDrawable = background as AnimationDrawable
            animationDrawable.start()
        }
    }
}