package com.example.datasaverexampleapp.drawing

import android.app.AlertDialog
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_draw_example.*

class DrawExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_example)
        title = "Draw Example"

        paint_test_button?.setOnClickListener {

            paint_test_view?.apply {

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

        opacity_test_button?.setOnClickListener {

            opacity_test_view?.apply {

                // Setting the opacity of an existing Paint object using the 'setAlpha' method.
                // Make color 50% transparent
                val paint = Paint()
                paint.color = Color.RED
                paint.alpha = 127
                setBackgroundColor(paint.color)
            }
        }

        compass_example_button?.setOnClickListener {
            val intent = Intent(this, CompassExampleFinalActivity::class.java)
            startActivity(intent)
        }

        composite_drawable_button?.setOnClickListener {
            val intent = Intent(this, CompositeDrawableActivity::class.java)
            startActivity(intent)
        }
    }
}