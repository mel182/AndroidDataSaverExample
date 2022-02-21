package com.example.datasaverexampleapp.dialog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityDialogExampleBinding
import com.example.datasaverexampleapp.type_alias.Layout

class DialogExampleActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_example)

        DataBindingUtil.setContentView<ActivityDialogExampleBinding>(
            this, Layout.activity_dialog_example
        ).apply {

            appcompatDialogButton.setOnClickListener {

                val dialogFragment = AppCompatDialogFragmentExample()
                dialogFragment.show(supportFragmentManager,null)
            }

            appcompatDialogWithCustomViewButton.setOnClickListener {

                DialogFragmentCustomView()
                    .setTitle("Custom view")
                    .setButtonTitle("Button title")
                    .show(supportFragmentManager,null)
            }
        }
    }
}