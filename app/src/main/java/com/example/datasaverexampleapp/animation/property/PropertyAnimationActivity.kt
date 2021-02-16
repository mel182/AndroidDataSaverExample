package com.example.datasaverexampleapp.animation.property

import android.animation.*
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Rect
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.*
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.example.datasaverexampleapp.R
import com.google.firebase.database.collection.LLRBNode
import kotlinx.android.synthetic.main.activity_property_animation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PropertyAnimationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_animation)
        title = "Property animation example"

        property_animations_button?.setOnClickListener {

            ObjectAnimator.ofFloat(animation_textView, View.ALPHA, 1.0f,0.0f)?.apply {
                duration = 550
                start()

                this.addListener(object: Animator.AnimatorListener{
                    override fun onAnimationStart(animation: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) {

                        CoroutineScope(Dispatchers.Main).launch {

                            delay(2000)

                            ObjectAnimator.ofFloat(animation_textView, View.ALPHA, 0.0f,1.0f)?.apply {
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

        rect_evaluator_button?.setOnClickListener {

            animation_image?.apply {

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
                                animation_image?.setImageResource(R.drawable.ic_baseline_wifi_24)
                            }

                            override fun onAnimationCancel(animation: Animator?) {}

                            override fun onAnimationRepeat(animation: Animator?) {}
                        })
                    }
                }
            }
        }

        type_evaluator_animations_button?.setOnClickListener {

            // animation_textView
            animation_image?.apply {

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

        background_color_animation_button?.setOnClickListener {

            ObjectAnimator.ofObject(background_animation_view,"backgroundColor", ArgbEvaluator(), Color.RED, Color.BLUE).apply {
                duration = 2000
                start()
            }
        }

        hsv_evaluator_button?.setOnClickListener {

            ObjectAnimator.ofObject(background_animation_view,"backgroundColor", HsvEvaluator(), Color.RED, Color.BLUE).apply {
                duration = 2000
                start()
            }
        }

        object_animator_xml_overshoot_interpolator_example_button?.setOnClickListener {

            AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                setTarget(xml_animation_view)
                interpolator = OvershootInterpolator()
                start()
            }
        }

        object_animator_xml_accelerate_decelerate_interpolator_example_button?.setOnClickListener {

            AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                setTarget(xml_animation_view)
                interpolator = AccelerateDecelerateInterpolator() // The rate of change starts and ends slowly but accelerates through the middle
                start()
            }
        }

        object_animator_xml_anticipate_interpolator_example_button?.setOnClickListener {

            AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                setTarget(xml_animation_view)
                interpolator = AnticipateInterpolator() // The rate of change starts slowly but accelerates through the middle
                start()
            }
        }

        object_animator_xml_anticipate_overshoot_interpolator_example_button?.setOnClickListener {

            AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                setTarget(xml_animation_view)
                interpolator = AnticipateOvershootInterpolator() // The change start backward, flings forward, overshoots the target value, and finally goes back to the final value
                start()
            }
        }

        object_animator_xml_bounce_interpolator_example_button?.setOnClickListener {

            AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                setTarget(xml_animation_view)
                interpolator = BounceInterpolator() // The change bounces at the end
                start()
            }
        }

        object_animator_xml_cycle_interpolator_example_button?.setOnClickListener {

            AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                setTarget(xml_animation_view)
                interpolator = CycleInterpolator(1.0f) // The change is repeated following a sinusoidal pattern
                start()
            }
        }

        object_animator_xml_decelerate_interpolator_example_button?.setOnClickListener {

            AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                setTarget(xml_animation_view)
                interpolator = DecelerateInterpolator() // The rate of change starts out quickly and then decelerates
                start()
            }
        }

        object_animator_xml_linear_interpolator_example_button?.setOnClickListener {

            AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                setTarget(xml_animation_view)
                interpolator = LinearInterpolator() // The rate of change is constant
                start()
            }
        }

        object_animator_xml_overshoot_interpolator_example_button2?.setOnClickListener {

            AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                setTarget(xml_animation_view)
                interpolator = OvershootInterpolator() // The change flings forward, overshoots the last value, and then comes back
                start()
            }
        }

        object_animator_xml_path_interpolator_example_button?.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                AnimatorInflater.loadAnimator(applicationContext, R.animator.object_animator_xml_example).apply {
                    setTarget(xml_animation_view)
                    interpolator = PathInterpolator(0.0f,1.0f) // The change follows a path object that extends from Point (0,0)
                                                                                // to (1,0).
                    start()
                }
            }
        }

    }
}