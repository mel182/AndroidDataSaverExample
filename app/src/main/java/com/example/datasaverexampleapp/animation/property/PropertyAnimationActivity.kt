package com.example.datasaverexampleapp.animation.property

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.RectEvaluator
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Rect
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
    }
}