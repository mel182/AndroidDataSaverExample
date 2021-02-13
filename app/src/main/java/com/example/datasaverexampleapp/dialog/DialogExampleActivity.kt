package com.example.datasaverexampleapp.dialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_dialog_example.*

class DialogExampleActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_example)

        appcompat_dialog_button?.setOnClickListener {

            val dialogFragment = AppCompatDialogFragmentExample()
            dialogFragment.show(supportFragmentManager,null)
        }
    }
}