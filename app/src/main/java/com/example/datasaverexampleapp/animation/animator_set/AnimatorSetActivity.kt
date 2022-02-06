package com.example.datasaverexampleapp.animation.animator_set

import android.animation.*
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.animation.PathInterpolatorCompat
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.animation.floating_action_menu.FloatingActionMenuAnimationActivity
import kotlinx.android.synthetic.main.activity_animator_set.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AnimatorSetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animator_set)

        animate_button?.setOnClickListener {

            AnimatorSet().apply {

                val alpha = ObjectAnimator.ofFloat(animation_image, "alpha", 0.0f , 1.0f)
                val translation = ObjectAnimator.ofFloat(animation_image, "translationY", 300.0f , 0.0f)
                playTogether(alpha, translation)
                duration = 1000
                interpolator = AccelerateInterpolator()
                start()
            }
        }

        fly_in_down_button?.setOnClickListener {

            val offDistX:Int = -animation_image.right
            val offDistY = -DPtoPX(10)

            val flyDownAnim = AnimatorSet().apply {
                duration = 800
                val translationX1 = ObjectAnimator.ofFloat(animation_image, "translationX",animation_image.translationX, offDistX.toFloat())
                val translationY1 = ObjectAnimator.ofFloat(animation_image, "translationY",animation_image.translationY, offDistY.toFloat())
                translationY1.interpolator = PathInterpolatorCompat.create(0.1f, 1f)
                val rotation1 = ObjectAnimator.ofFloat(animation_image,"rotation",animation_image.rotation,0.0f)
                val rotation2 = ObjectAnimator.ofFloat(animation_image,"rotationX",animation_image.rotationX,30.0f)
                rotation1.interpolator = AccelerateInterpolator()
                playTogether(translationX1,
                    translationY1,
                    rotation1,
                    rotation2,
                    ObjectAnimator.ofFloat(animation_image,"scaleX",animation_image.scaleX, 0.9f),
                    ObjectAnimator.ofFloat(animation_image,"scaleY",animation_image.scaleY, 0.9f))

                addListener(object: Animator.AnimatorListener{

                    override fun onAnimationStart(animation: Animator?) {
                        animation_image.rotation = 180.0f
                    }

                    override fun onAnimationEnd(animation: Animator?) { }

                    override fun onAnimationCancel(animation: Animator?) { }

                    override fun onAnimationRepeat(animation: Animator?) { }
                })
            }

            val flyInAnim = AnimatorSet().apply {
                duration = 800
                interpolator = DecelerateInterpolator()
                val translationX1 = ObjectAnimator.ofFloat(animation_image, "translationX",offDistX.toFloat(), 0.0f)
                val translationY1 = ObjectAnimator.ofFloat(animation_image, "translationY",offDistY.toFloat(), 0.0f)
                val rotation2 = ObjectAnimator.ofFloat(animation_image,"rotationX",30.0f, 0.0f)

                playTogether(translationX1,
                    translationY1,
                    rotation2,
                    ObjectAnimator.ofFloat(animation_image,"scaleX",0.9f, 1.0f),
                    ObjectAnimator.ofFloat(animation_image,"scaleY",0.9f, 1.0f))

                startDelay = 100
                addListener(object: Animator.AnimatorListener{

                    override fun onAnimationStart(animation: Animator?) {
                        animation_image.rotationY = 0.0f
                    }

                    override fun onAnimationEnd(animation: Animator?) { }

                    override fun onAnimationCancel(animation: Animator?) { }

                    override fun onAnimationRepeat(animation: Animator?) { }
                })
            }

            AnimatorSet().apply {
                playSequentially(flyDownAnim, flyInAnim)
                start()
            }
        }

        feed_item_animator_button?.setOnClickListener {

            AnimatorSet().apply {

                val rotationAnim = ObjectAnimator.ofFloat(fav_icon, "rotation", 0.0f, 360.0f).apply {
                    duration = 300
                    interpolator = AccelerateInterpolator()
                }

                val bounceAnimX = ObjectAnimator.ofFloat(fav_icon,"scaleX",0.2f, 1.0f).apply {
                    duration = 300
                    interpolator = OvershootInterpolator()
                }

                val bounceAnimY = ObjectAnimator.ofFloat(fav_icon,"scaleY",0.2f, 1.0f).apply {
                    duration = 300
                    interpolator = OvershootInterpolator()
                    addListener(object: Animator.AnimatorListener{
                        override fun onAnimationStart(animation: Animator?) {
                            fav_icon.setImageResource(R.drawable.ic_favorite_red)
                        }

                        override fun onAnimationEnd(animation: Animator?) {

                            CoroutineScope(Dispatchers.Main).launch {

                                delay(2000)
                                fav_icon.setImageResource(R.drawable.ic_favorite_yellow)
                            }
                        }

                        override fun onAnimationCancel(animation: Animator?) {}

                        override fun onAnimationRepeat(animation: Animator?) {}

                    })
                }

                play(bounceAnimX).with(bounceAnimY).after(rotationAnim)
                start()
            }
        }

        animate_shutter_button?.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {

                animation_image.visibility = View.INVISIBLE

                delay(4000)

                animation_image.visibility = View.VISIBLE
                animation_image.alpha = 0.0f

                val alphaInAnim = ObjectAnimator.ofFloat(animation_image,"alpha", 0.0f, 0.8f).apply {
                    duration = 100
                    startDelay = 100
                    interpolator = AccelerateInterpolator()
                }

                val alphaOutAnim = ObjectAnimator.ofFloat(animation_image,"alpha", 0.8f, 0.0f).apply {
                    duration = 200
                    interpolator = AccelerateInterpolator()
                }

                AnimatorSet().apply {

                    playSequentially(alphaInAnim, alphaOutAnim)
                    start()
                }
            }
        }

        heart_beat_animation_button?.setOnClickListener {

            fav_icon.setImageResource(R.drawable.ic_favorite_red)

            AnimatorSet().apply {

                val scaleX = ObjectAnimator.ofFloat(fav_icon, View.SCALE_X, 0.6f).apply {
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.REVERSE
                    duration = 1000
                }

                val scaleY = ObjectAnimator.ofFloat(fav_icon, View.SCALE_Y, 0.6f).apply {
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

        floating_action_menu_animation_button?.setOnClickListener {

            val intent = Intent(this, FloatingActionMenuAnimationActivity::class.java)
            startActivity(intent)
        }

    }

    private fun DPtoPX(dp:Int) : Int
    {
        val displayMatrics = resources.displayMetrics
        return Math.round(dp * (displayMatrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

}