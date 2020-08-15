package com.example.datasaverexampleapp.earth_quake_example

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.earth_quake_example.model.Earthquake
import java.text.SimpleDateFormat
import java.util.*

class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)
{
    private val detailTextView:TextView? = itemView.findViewById(R.id.list_item_earthquake_details)

    @SuppressLint("SetTextI18n")
    fun bind(data:Earthquake)
    {
        val timeFormatter = SimpleDateFormat("HH:mm",Locale.getDefault())
        val timeString = timeFormatter.format(data.date)
        detailTextView?.setText("$timeString: ${data.magnitude} ${data.details}'")
    }

}