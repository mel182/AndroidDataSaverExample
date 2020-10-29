package com.example.datasaverexampleapp.intent_example.starSignPicker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_star_sign_picker.*

class StarSignPickerActivity : AppCompatActivity(), OnItemClicked {

    companion object {
        const val EXTRA_SIGN_NAME = "SIGN NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_star_sign_picker)
        title = "Star sign picker Example"

        star_list?.apply {
            layoutManager = LinearLayoutManager(this@StarSignPickerActivity)
            adapter = StarSignAdapter(this@StarSignPickerActivity)
        }
    }

    override fun onItemClick(item: String) {

        val outData = Intent()
        outData.putExtra(EXTRA_SIGN_NAME, item)
        setResult(Activity.RESULT_OK, outData)
        finish()
    }
}