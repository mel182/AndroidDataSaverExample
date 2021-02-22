package com.example.datasaverexampleapp.implicit_delegation_example

import android.widget.Toast
import com.example.datasaverexampleapp.application.AppContext

object CustomOnClickListener : OnDataClicked
{
    override fun onDataClicked(data: String) {
        Toast.makeText(AppContext.appContext, data, Toast.LENGTH_SHORT).show()
    }
}