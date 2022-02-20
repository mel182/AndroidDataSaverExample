package com.example.datasaverexampleapp.drawing

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityDrawExampleBinding
import com.example.datasaverexampleapp.type_alias.Layout

class DrawExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_example)
        title = "Draw Example"

        DataBindingUtil.setContentView<ActivityDrawExampleBinding>(
            this, Layout.activity_draw_example
        ).apply {

            paintTestButton.setOnClickListener {

                paintTestView.apply {

                    setBackgroundColor(Color.TRANSPARENT)

                    // Make color red and 50% transparent
                    val opacity = 127
                    val color = Color.argb(opacity,255,0,0) //  Color with 'Color.argb'
                    val colorHex = Color.parseColor("#7FFF0000") //  Color with hex color parse

                    AlertDialog.Builder(this@DrawExampleActivity).apply {
                        setTitle("Draw example")
                        setMessage("Which option would you like to execute?\nNote: The color is red and 50% transparent")
                        setPositiveButton("Color") { _, _ ->
                            setBackgroundColor(color)
                        }
                        setNegativeButton("Hex color") { _, _ ->
                            setBackgroundColor(colorHex)
                        }
                        create().show()
                    }
                }
            }

            opacityTestButton.setOnClickListener {

                opacityTestView.apply {

                    // Setting the opacity of an existing Paint object using the 'setAlpha' method.
                    // Make color 50% transparent
                    val paint = Paint()
                    paint.color = Color.RED
                    paint.alpha = 127
                    setBackgroundColor(paint.color)
                }
            }

            compassExampleButton.setOnClickListener {
                val intent = Intent(this@DrawExampleActivity, CompassExampleFinalActivity::class.java)
                startActivity(intent)
            }

            compositeDrawableButton.setOnClickListener {
                val intent = Intent(this@DrawExampleActivity, CompositeDrawableActivity::class.java)
                startActivity(intent)
            }
        }
    }
}