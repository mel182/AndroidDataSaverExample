@file:Suppress("UNNECESSARY_SAFE_CALL")

package com.example.datasaverexampleapp.animation

import android.animation.Animator
import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.animation.activity_transition.ActivityTransition1
import com.example.datasaverexampleapp.databinding.ActivityAnimationExampleBinding
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.coroutines.*
import kotlin.math.hypot

@Suppress("UNUSED_VARIABLE")
@SuppressLint("SetTextI18n")
class AnimationExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation_example)

        DataBindingUtil.setContentView<ActivityAnimationExampleBinding>(
            this, Layout.activity_animation_example
        ).apply {

            alpha01ExampleTextview.apply {
                val alphaAnimator = AnimatorInflater.loadAnimator(this@AnimationExampleActivity, R.animator.set_alpha_between_0_1)
                alphaAnimator.setTarget(this)
                alphaAnimator.start()
            }

            groupAnimationExample.apply {

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

            circularRevealAnimationButton.setOnClickListener {

                val centerX =  circularRevealTextView.width / 2
                val centerY = circularRevealTextView.height / 2
                val coveringRadius = hypot(centerX.toDouble(), centerY.toDouble()).toFloat()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    if (circularRevealTextView.visibility == View.INVISIBLE)
                    {
                        val anim = ViewAnimationUtils.createCircularReveal(circularRevealTextView, centerX,centerY,0F,coveringRadius)
                        circularRevealTextView.visibility = View.VISIBLE
                        anim.start()
                        anim.addListener(object: Animator.AnimatorListener {
                            override fun onAnimationStart(animation: Animator) {

                            }

                            override fun onAnimationEnd(animation: Animator) {
                                circularRevealAnimationButton.text = "Circular reveal hide"
                            }

                            override fun onAnimationCancel(animation: Animator) {}
                            override fun onAnimationRepeat(animation: Animator) {}
                        })
                    } else {
                        val anim = ViewAnimationUtils.createCircularReveal(circularRevealTextView, centerX,centerY,coveringRadius,0F)
                        anim.start()
                        anim.addListener(object: Animator.AnimatorListener {
                            override fun onAnimationStart(animation: Animator) {}

                            override fun onAnimationEnd(animation: Animator) {
                                circularRevealTextView.visibility = View.INVISIBLE
                                circularRevealAnimationButton.text = "Circular reveal"
                            }

                            override fun onAnimationCancel(animation: Animator) {}
                            override fun onAnimationRepeat(animation: Animator) {}
                        })
                    }
                }
            }

            activityTransitionAnimationButton.setOnClickListener {
                val intent = Intent(this@AnimationExampleActivity,ActivityTransition1::class.java)
                startActivity(intent)
            }
        }
    }
}