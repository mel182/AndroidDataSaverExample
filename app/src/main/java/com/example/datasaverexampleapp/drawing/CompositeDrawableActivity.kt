package com.example.datasaverexampleapp.drawing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_card_view.view.*
import kotlinx.android.synthetic.main.activity_composite_drawable.*

class CompositeDrawableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_composite_drawable)
        title = "Composite drawable Example"

        scale_drawable_button?.setOnClickListener {
            scale_drawable_imageView?.setImageLevel(5000)
        }

        rotate_drawable_button?.setOnClickListener {
            rotate_drawable_imageView?.setImageLevel(5000)
        }

        level_drawable_view?.setImageLevel(0)
        view_level_state?.text = "image level 0"
        level_list_button?.text = "level 1"

        level_list_button?.let { button ->

            button.setOnClickListener {

                when(button.text)
                {
                    "level 0" -> {
                        level_drawable_view?.setImageLevel(0)
                        view_level_state?.text = "image level 0"
                        button.text = "level 1"
                    }

                    "level 1" -> {
                        level_drawable_view?.setImageLevel(1)
                        view_level_state?.text = "image level 1"
                        button.text = "level 2"
                    }

                    "level 2" -> {
                        level_drawable_view?.setImageLevel(2)
                        view_level_state?.text = "image level 2"
                        button.text = "level 4"
                    }

                    "level 4" -> {
                        level_drawable_view?.setImageLevel(4)
                        view_level_state?.text = "image level 4"
                        button.text = "level 6"
                    }

                    "level 6" -> {
                        level_drawable_view?.setImageLevel(6)
                        view_level_state?.text = "image level 6"
                        button.text = "level 8"
                    }

                    "level 8" -> {
                        level_drawable_view?.setImageLevel(8)
                        view_level_state?.text = "image level 8"
                        button.text = "level 10"
                    }

                    "level 10" -> {
                        level_drawable_view?.setImageLevel(10)
                        view_level_state?.text = "image level 10"
                        button.text = "level 0"
                    }
                }
            }
        }
    }
}