package com.example.datasaverexampleapp.intent_example.starSignPicker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_for_result.*

class ActivityForResultActivity : AppCompatActivity(), OnItemClicked {

    companion object {
        const val EXTRA_ACTIVITY_FOR_RESULT_EXAMPLE = "SIGN NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_for_result)
        title = "Activity for result Example"

        demo_list?.apply {
            layoutManager = LinearLayoutManager(this@ActivityForResultActivity)
            adapter = StarSignAdapter(this@ActivityForResultActivity)
        }
    }

    override fun onItemClick(item: String) {

        val outData = Intent()
        outData.putExtra(EXTRA_ACTIVITY_FOR_RESULT_EXAMPLE, item)
        setResult(Activity.RESULT_OK, outData)
        finish()
    }
}