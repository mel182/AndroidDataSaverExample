package com.example.datasaverexampleapp.implicit_delegation_example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityImplicitDelegationBinding
import com.example.datasaverexampleapp.type_alias.Layout

class ImplicitDelegationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_implicit_delegation)
        title = "OnCLick Implicit delegation example"

        DataBindingUtil.setContentView<ActivityImplicitDelegationBinding>(
            this, Layout.activity_implicit_delegation
        ).apply {
            list.apply {
                layoutManager = LinearLayoutManager(this@ImplicitDelegationActivity)
                adapter = ImplicitItemAdapter(arrayListOf(
                    "Value 1","Value 2",
                    "Value 3","Value 4",
                    "Value 5","Value 6",
                    "Value 7","Value 8",
                    "Value 9","Value 10",
                    "Value 11","Value 12",
                    "Value 13","Value 14"))
            }
        }
    }
}