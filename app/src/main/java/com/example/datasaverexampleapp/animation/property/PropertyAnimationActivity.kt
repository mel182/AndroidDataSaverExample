package com.example.datasaverexampleapp.animation.property

import android.animation.*
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityPropertyAnimationBinding
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PropertyAnimationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_animation)
        title = "Property animation example"

        DataBindingUtil.setContentView<ActivityPropertyAnimationBinding>(
            this, Layout.activity_property_animation
        ).apply {

            propertyAnimationsButton.setOnClickListener {

                ObjectAnimator.ofFloat(animationTextView, View.ALPHA, 1.0f,0.0f)?.apply {
                    duration = 550
                    start()

                    this.addListener(object: Animator.AnimatorListener{
                        override fun onAnimationStart(animation: Animator?) {}

                        override fun onAnimationEnd(animation: Animator?) {

                            CoroutineScope(Dispatchers.Main).launch {

                                delay(2000)

                                ObjectAnimator.ofFloat(animationTextView, View.ALPHA, 0.0f,1.0f)?.apply {
                                    duration = 550
                                    start()
                                }
                            }
                        }

                        override fun onAnimationCancel(animation: Animator?) {}

                        override fun onAnimationRepeat(animation: Animator?) {}

                    })
                }
            }

            rectEvaluatorButton.setOnClickListener {

                animationImage.apply {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

                        val local = Rect()
                        getLocalVisibleRect(local)
                        val from = Rect(local)
                        val to = Rect(local)

                        from.right = from.left + local.width()/4
                        from.bottom = from.top + local.height()/2

                        to.left = to.right - local.width()/2
                        to.top = to.bottom - local.height()/4

                        ObjectAnimator.ofObject(this,"clipBounds", RectEvaluator(), from, to).apply {
                            duration = 2000
                            start()

                            this.addListener(object: Animator.AnimatorListener{
                                override fun onAnimationStart(animation: Animator?) {}

                                override fun onAnimationEnd(animation: Animator?) {
                                    animationImage.setImageResource(R.drawable.ic_baseline_wifi_24)
                                }

                                override fun onAnimationCancel(animation: Animator?) {}

                                override fun onAnimationRepeat(animation: Animator?) {}
                            })
                        }
                    }
                }
            }

            typeEvaluatorAnimationsButton.setOnClickListener {

                // animation_textView
                animationImage.apply {

                    scaleType = ImageView.ScaleType.MATRIX
                    val scale = height.toFloat() / drawable.intrinsicHeight.toFloat()
                    val from = Matrix().apply {
                        setScale(scale,scale)
                        postSkew(-0.5f, 0.0f)
                    }

                    val to = Matrix().apply {
                        setScale(scale, scale)
                        postSkew(0.5f,0.0f)
                    }

                    ObjectAnimator.ofObject(this,"imageMatrix",MatrixEvaluator(), from,to).apply {
                        duration = 500
                        repeatCount = 5
                        repeatMode = ObjectAnimator.REVERSE
                        start()
                    }
                }
            }

            backgroundColorAnimationButton.setOnClickListener {

                ObjectAnimator.ofObject(backgroundAnimationView,"backgroundColor", ArgbEvaluator(), Color.RED, Color.BLUE).apply {
                    duration = 2000
                    start()
                }
            }

            hsvEvaluatorButton.setOnClickListener {

                ObjectAnimator.ofObject(backgroundAnimationView,"backgroundColor", HsvEvaluator(), Color.RED, Color.BLUE).apply {
                    duration = 2000
                    start()
                }
            }

            objectAnimatorXmlOvershootInterpolatorExampleButton.setOnClickListener {

                AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                    setTarget(xmlAnimationView)
                    interpolator = OvershootInterpolator()
                    start()
                }
            }

            objectAnimatorXmlAccelerateDecelerateInterpolatorExampleButton.setOnClickListener {

                AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                    setTarget(xmlAnimationView)
                    interpolator = AccelerateDecelerateInterpolator() // The rate of change starts and ends slowly but accelerates through the middle
                    start()
                }
            }

            objectAnimatorXmlAnticipateInterpolatorExampleButton.setOnClickListener {

                AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                    setTarget(xmlAnimationView)
                    interpolator = AnticipateInterpolator() // The rate of change starts slowly but accelerates through the middle
                    start()
                }
            }

            objectAnimatorXmlAnticipateOvershootInterpolatorExampleButton.setOnClickListener {

                AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                    setTarget(xmlAnimationView)
                    interpolator = AnticipateOvershootInterpolator() // The change start backward, flings forward, overshoots the target value, and finally goes back to the final value
                    start()
                }
            }

            objectAnimatorXmlBounceInterpolatorExampleButton.setOnClickListener {

                AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                    setTarget(xmlAnimationView)
                    interpolator = BounceInterpolator() // The change bounces at the end
                    start()
                }
            }

            objectAnimatorXmlCycleInterpolatorExampleButton.setOnClickListener {

                AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                    setTarget(xmlAnimationView)
                    interpolator = CycleInterpolator(1.0f) // The change is repeated following a sinusoidal pattern
                    start()
                }
            }

            objectAnimatorXmlDecelerateInterpolatorExampleButton.setOnClickListener {

                AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                    setTarget(xmlAnimationView)
                    interpolator = DecelerateInterpolator() // The rate of change starts out quickly and then decelerates
                    start()
                }
            }

            objectAnimatorXmlLinearInterpolatorExampleButton.setOnClickListener {

                AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                    setTarget(xmlAnimationView)
                    interpolator = LinearInterpolator() // The rate of change is constant
                    start()
                }
            }

            objectAnimatorXmlOvershootInterpolatorExampleButton2.setOnClickListener {

                AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                    setTarget(xmlAnimationView)
                    interpolator = OvershootInterpolator() // The change flings forward, overshoots the last value, and then comes back
                    start()
                }
            }

            objectAnimatorXmlPathInterpolatorExampleButton.setOnClickListener {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                        setTarget(xmlAnimationView)
                        interpolator = PathInterpolator(0.0f,1.0f) // The change follows a path object that extends from Point (0,0)
                        // to (1,0).
                        start()
                    }
                }
            }
        }
    }
}