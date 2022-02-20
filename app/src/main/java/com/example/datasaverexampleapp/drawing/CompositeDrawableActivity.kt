package com.example.datasaverexampleapp.drawing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityCompositeDrawableBinding
import com.example.datasaverexampleapp.type_alias.Layout

class CompositeDrawableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_composite_drawable)
        title = "Composite drawable Example"

        DataBindingUtil.setContentView<ActivityCompositeDrawableBinding>(
            this, Layout.activity_composite_drawable
        ).apply {

            scaleDrawableButton.setOnClickListener {
                scaleDrawableImageView.setImageLevel(5000)
            }

            rotateDrawableButton.setOnClickListener {
                rotateDrawableImageView.setImageLevel(5000)
            }

            levelDrawableView.setImageLevel(0)
            viewLevelState.text = "image level 0"
            levelListButton.text = "level 1"

            levelListButton.let { button ->

                button.setOnClickListener {

                    when(button.text)
                    {
                        "level 0" -> {
                            levelDrawableView.setImageLevel(0)
                            viewLevelState.text = "image level 0"
                            button.text = "level 1"
                        }

                        "level 1" -> {
                            levelDrawableView.setImageLevel(1)
                            viewLevelState.text = "image level 1"
                            button.text = "level 2"
                        }

                        "level 2" -> {
                            levelDrawableView.setImageLevel(2)
                            viewLevelState.text = "image level 2"
                            button.text = "level 4"
                        }

                        "level 4" -> {
                            levelDrawableView.setImageLevel(4)
                            viewLevelState.text = "image level 4"
                            button.text = "level 6"
                        }

                        "level 6" -> {
                            levelDrawableView.setImageLevel(6)
                            viewLevelState.text = "image level 6"
                            button.text = "level 8"
                        }

                        "level 8" -> {
                            levelDrawableView.setImageLevel(8)
                            viewLevelState.text = "image level 8"
                            button.text = "level 10"
                        }

                        "level 10" -> {
                            levelDrawableView.setImageLevel(10)
                            viewLevelState.text = "image level 10"
                            button.text = "level 0"
                        }
                    }
                }
            }
        }
    }
}