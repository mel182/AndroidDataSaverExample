package com.example.datasaverexampleapp.intent_example.starSignPicker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityForResultBinding
import com.example.datasaverexampleapp.type_alias.Layout

class ActivityForResultActivity : AppCompatActivity(), OnItemClicked {

    companion object {
        const val EXTRA_ACTIVITY_FOR_RESULT_EXAMPLE = "SIGN NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_for_result)
        title = "Activity for result Example"

        DataBindingUtil.setContentView<ActivityForResultBinding>(
            this, Layout.activity_for_result
        ).apply {
            demoList.apply {
                layoutManager = LinearLayoutManager(this@ActivityForResultActivity)
                adapter = StarSignAdapter(this@ActivityForResultActivity)
            }
        }
    }

    override fun onItemClick(item: String) {
        val outData = Intent()
        outData.putExtra(EXTRA_ACTIVITY_FOR_RESULT_EXAMPLE, item)
        setResult(Activity.RESULT_OK, outData)
        finish()
    }
}