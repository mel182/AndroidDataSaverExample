package com.example.datasaverexampleapp.implicit_delegation_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_implicit_delegation.*

class ImplicitDelegationActivity : AppCompatActivity(), View.OnClickListener by CustomOnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_implicit_delegation)
        title = "OnCLick Implicit delegation example"

        list?.apply {
            layoutManager = LinearLayoutManager(this@ImplicitDelegationActivity)
            adapter = ImplicitItemAdapter(arrayListOf("Value 1","Value 2",
                "Value 3","Value 4",
                "Value 5","Value 6",
                "Value 7","Value 8",
                "Value 9","Value 10",
                "Value 11","Value 12",
                "Value 13","Value 14"),this@ImplicitDelegationActivity)
        }
    }
}