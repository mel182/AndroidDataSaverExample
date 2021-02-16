package com.example.datasaverexampleapp.animation.floating_action_menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_floating_action_menu_animation.*

class FloatingActionMenuAnimationActivity : AppCompatActivity() {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.fab_rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.fab_rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.fab_from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.fab_to_bottom_anim) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_floating_action_menu_animation)
        title = "Floating action menu"

        floatingActionButton_main?.setOnClickListener {
            animateButtons()
        }

        floatingActionButtonEdit?.setOnClickListener {

            if (it.visibility == View.VISIBLE)
            {
                Toast.makeText(this, "Edit button clicked!",Toast.LENGTH_SHORT).show()
                animateButtons()
            }
        }

        floatingActionButtonMessage?.setOnClickListener {

            if (it.visibility == View.VISIBLE)
            {
                Toast.makeText(this, "Message button clicked!",Toast.LENGTH_SHORT).show()
                animateButtons()
            }
        }

    }

    private fun animateButtons()
    {
        if (floatingActionButtonEdit.visibility == View.VISIBLE && floatingActionButtonMessage.visibility == View.VISIBLE)
        {
            floatingActionButtonEdit?.apply {
                visibility = View.INVISIBLE
                startAnimation(toBottom)
            }

            floatingActionButtonMessage?.apply {
                visibility = View.INVISIBLE
                startAnimation(toBottom)
            }

            floatingActionButton_main?.apply {
                startAnimation(rotateClose)
                setImageResource(R.drawable.ic_baseline_settings)
            }

        } else {

            floatingActionButtonEdit?.apply {
                visibility = View.VISIBLE
                startAnimation(fromBottom)
            }

            floatingActionButtonMessage?.apply {
                visibility = View.VISIBLE
                startAnimation(fromBottom)
            }

            floatingActionButton_main?.apply {
                startAnimation(rotateOpen)
                setImageResource(R.drawable.ic_add)
            }
        }
    }
}