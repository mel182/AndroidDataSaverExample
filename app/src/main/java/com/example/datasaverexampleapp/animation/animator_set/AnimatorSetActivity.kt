package com.example.datasaverexampleapp.animation.animator_set

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.animation.floating_action_menu.FloatingActionMenuAnimationActivity
import com.example.datasaverexampleapp.databinding.ActivityAnimatorSetBinding
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AnimatorSetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animator_set)

        DataBindingUtil.setContentView<ActivityAnimatorSetBinding>(this, Layout.activity_animator_set).apply {

            animateButton.setOnClickListener {

                AnimatorSet().apply {

                    val alpha = ObjectAnimator.ofFloat(animationImage, "alpha", 0.0f , 1.0f)
                    val translation = ObjectAnimator.ofFloat(animationImage, "translationY", 300.0f , 0.0f)
                    playTogether(alpha, translation)
                    duration = 1000
                    interpolator = AccelerateInterpolator()
                    start()
                }
            }

            flyInDownButton.setOnClickListener {

                val offDistX:Int = -animationImage.right
                val offDistY = -DPtoPX(10)

                val flyDownAnim = AnimatorSet().apply {
                    duration = 800
                    val translationX1 = ObjectAnimator.ofFloat(animationImage, "translationX",animationImage.translationX, offDistX.toFloat())
                    val translationY1 = ObjectAnimator.ofFloat(animationImage, "translationY",animationImage.translationY, offDistY.toFloat())
                    translationY1.interpolator = PathInterpolatorCompat.create(0.1f, 1f)
                    val rotation1 = ObjectAnimator.ofFloat(animationImage,"rotation",animationImage.rotation,0.0f)
                    val rotation2 = ObjectAnimator.ofFloat(animationImage,"rotationX",animationImage.rotationX,30.0f)
                    rotation1.interpolator = AccelerateInterpolator()
                    playTogether(translationX1,
                        translationY1,
                        rotation1,
                        rotation2,
                        ObjectAnimator.ofFloat(animationImage,"scaleX",animationImage.scaleX, 0.9f),
                        ObjectAnimator.ofFloat(animationImage,"scaleY",animationImage.scaleY, 0.9f))

                    addListener(object: Animator.AnimatorListener{

                        override fun onAnimationStart(animation: Animator) {
                            animationImage.rotation = 180.0f
                        }

                        override fun onAnimationEnd(animation: Animator) { }

                        override fun onAnimationCancel(animation: Animator) { }

                        override fun onAnimationRepeat(animation: Animator) { }
                    })
                }

                val flyInAnim = AnimatorSet().apply {
                    duration = 800
                    interpolator = DecelerateInterpolator()
                    val translationX1 = ObjectAnimator.ofFloat(animationImage, "translationX",offDistX.toFloat(), 0.0f)
                    val translationY1 = ObjectAnimator.ofFloat(animationImage, "translationY",offDistY.toFloat(), 0.0f)
                    val rotation2 = ObjectAnimator.ofFloat(animationImage,"rotationX",30.0f, 0.0f)

                    playTogether(translationX1,
                        translationY1,
                        rotation2,
                        ObjectAnimator.ofFloat(animationImage,"scaleX",0.9f, 1.0f),
                        ObjectAnimator.ofFloat(animationImage,"scaleY",0.9f, 1.0f))

                    startDelay = 100
                    addListener(object: Animator.AnimatorListener{

                        override fun onAnimationStart(animation: Animator) {
                            animationImage.rotationY = 0.0f
                        }

                        override fun onAnimationEnd(animation: Animator) { }

                        override fun onAnimationCancel(animation: Animator) { }

                        override fun onAnimationRepeat(animation: Animator) { }
                    })
                }

                AnimatorSet().apply {
                    playSequentially(flyDownAnim, flyInAnim)
                    start()
                }
            }

            feedItemAnimatorButton.setOnClickListener {

                AnimatorSet().apply {

                    val rotationAnim = ObjectAnimator.ofFloat(favIcon, "rotation", 0.0f, 360.0f).apply {
                        duration = 300
                        interpolator = AccelerateInterpolator()
                    }

                    val bounceAnimX = ObjectAnimator.ofFloat(favIcon,"scaleX",0.2f, 1.0f).apply {
                        duration = 300
                        interpolator = OvershootInterpolator()
                    }

                    val bounceAnimY = ObjectAnimator.ofFloat(favIcon,"scaleY",0.2f, 1.0f).apply {
                        duration = 300
                        interpolator = OvershootInterpolator()
                        addListener(object: Animator.AnimatorListener{
                            override fun onAnimationStart(animation: Animator) {
                                favIcon.setImageResource(R.drawable.ic_favorite_red)
                            }

                            override fun onAnimationEnd(animation: Animator) {

                                CoroutineScope(Dispatchers.Main).launch {

                                    delay(2000)
                                    favIcon.setImageResource(R.drawable.ic_favorite_yellow)
                                }
                            }

                            override fun onAnimationCancel(animation: Animator) {}

                            override fun onAnimationRepeat(animation: Animator) {}

                        })
                    }

                    play(bounceAnimX).with(bounceAnimY).after(rotationAnim)
                    start()
                }
            }

            animateShutterButton.setOnClickListener {

                CoroutineScope(Dispatchers.Main).launch {

                    animationImage.visibility = View.INVISIBLE

                    delay(4000)

                    animationImage.visibility = View.VISIBLE
                    animationImage.alpha = 0.0f

                    val alphaInAnim = ObjectAnimator.ofFloat(animationImage,"alpha", 0.0f, 0.8f).apply {
                        duration = 100
                        startDelay = 100
                        interpolator = AccelerateInterpolator()
                    }

                    val alphaOutAnim = ObjectAnimator.ofFloat(animationImage,"alpha", 0.8f, 0.0f).apply {
                        duration = 200
                        interpolator = AccelerateInterpolator()
                    }

                    AnimatorSet().apply {

                        playSequentially(alphaInAnim, alphaOutAnim)
                        start()
                    }
                }
            }

            heartBeatAnimationButton.setOnClickListener {

                favIcon.setImageResource(R.drawable.ic_favorite_red)

                AnimatorSet().apply {

                    val scaleX = ObjectAnimator.ofFloat(favIcon, View.SCALE_X, 0.6f).apply {
                        repeatCount = ValueAnimator.INFINITE
                        repeatMode = ValueAnimator.REVERSE
                        duration = 1000
                    }

                    val scaleY = ObjectAnimator.ofFloat(favIcon, View.SCALE_Y, 0.6f).apply {
                        repeatCount = ValueAnimator.INFINITE
                        repeatMode = ValueAnimator.REVERSE
                        duration = 1000
                    }

                    playTogether(scaleX, scaleY)
                    start()

//                CoroutineScope(Dispatchers.Main).launch {
//                    delay(2000)
//                    cancel()
//                }

                }
            }

            floatingActionMenuAnimationButton.setOnClickListener {

                val intent = Intent(this@AnimatorSetActivity, FloatingActionMenuAnimationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun DPtoPX(dp:Int) : Int
    {
        val displayMatrics = resources.displayMetrics
        return Math.round(dp * (displayMatrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

}