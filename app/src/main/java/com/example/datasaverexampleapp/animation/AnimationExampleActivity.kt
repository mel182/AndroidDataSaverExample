package com.example.datasaverexampleapp.animation

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.animation.activity_transition.ActivityTransition1
import kotlinx.android.synthetic.main.activity_animation_example.*
import kotlinx.coroutines.*
import kotlin.math.hypot

@Suppress("UNUSED_VARIABLE")
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

        CoroutineScope(Dispatchers.IO).launch {
            val job: Job = launch(Dispatchers.IO){

                launch(Dispatchers.Main){

                    Toast.makeText(this@AnimationExampleActivity, "dispatcher main", Toast.LENGTH_SHORT).show()
                    delay(100)
                }
            }
        }

        circular_reveal_animation_button?.setOnClickListener {

            val centerX = circular_reveal_textView.width / 2
            val centerY = circular_reveal_textView.height / 2
            val coveringRadius = hypot(centerX.toDouble(), centerY.toDouble()).toFloat()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                if (circular_reveal_textView.visibility == View.INVISIBLE)
                {
                    val anim = ViewAnimationUtils.createCircularReveal(circular_reveal_textView, centerX,centerY,0F,coveringRadius)
                    circular_reveal_textView.visibility = View.VISIBLE
                    anim.start()
                    anim.addListener(object: Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator?) {

                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            circular_reveal_animation_button?.text = "Circular reveal hide"
                        }

                        override fun onAnimationCancel(animation: Animator?) {}
                        override fun onAnimationRepeat(animation: Animator?) {}
                    })
                } else {
                    val anim = ViewAnimationUtils.createCircularReveal(circular_reveal_textView, centerX,centerY,coveringRadius,0F)
                    anim.start()
                    anim.addListener(object: Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator?) {}

                        override fun onAnimationEnd(animation: Animator?) {
                            circular_reveal_textView.visibility = View.INVISIBLE
                            circular_reveal_animation_button?.text = "Circular reveal"
                        }

                        override fun onAnimationCancel(animation: Animator?) {}
                        override fun onAnimationRepeat(animation: Animator?) {}
                    })
                }
            }
        }

        activity_transition_animation_button?.setOnClickListener {
            val intent = Intent(this,ActivityTransition1::class.java)
            startActivity(intent)
        }
    }
}