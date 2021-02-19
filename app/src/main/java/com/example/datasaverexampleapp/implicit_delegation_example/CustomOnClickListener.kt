package com.example.datasaverexampleapp.implicit_delegation_example

import android.view.View
import android.widget.TextView
import android.widget.Toast

object CustomOnClickListener : View.OnClickListener
{
    override fun onClick(view: View?) {

        view?.also { clickedView ->

            if (clickedView is TextView)
            {
                Toast.makeText(clickedView.context, clickedView.text, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(clickedView.context, "View clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }
}