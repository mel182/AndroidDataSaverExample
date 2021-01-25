package com.example.datasaverexampleapp.animation

import android.animation.AnimatorInflater
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat.*
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.type_alias.Drawable
import kotlinx.android.synthetic.main.activity_animation_example.*
import kotlinx.coroutines.*

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


        GlobalScope.launch {

           val job: Job = launch(Dispatchers.IO){

                launch(Dispatchers.Main){

                    Toast.makeText(this@AnimationExampleActivity, "dispatcher main", Toast.LENGTH_SHORT).show()
                    delay(100)
                }
           }
        }

        start_vector_animate_button?.setOnClickListener {
            val animationDrawable = androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat.create(this,Drawable.vector_animation)
            animate_imageView.setImageDrawable(animationDrawable)
            animationDrawable?.start()
        }
    }
}