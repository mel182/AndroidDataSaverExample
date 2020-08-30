package com.example.datasaverexampleapp.earth_quake_example

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.earth_quake_example.model.Earthquake

class EarthQuakeRecyclerViewAdapter(private val earthquakes: ArrayList<Earthquake> = ArrayList()) : RecyclerView.Adapter<ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_earthquake,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.earthquakes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val earthquake = earthquakes[position]
        holder.bind(earthquake)
    }

    fun loadData(data:Earthquake)
    {
        this.earthquakes.add(data)
    }

    fun clearData()
    {
        this.earthquakes.clear()
        notifyDataSetChanged()
    }
}