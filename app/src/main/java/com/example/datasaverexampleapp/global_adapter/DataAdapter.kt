package com.example.datasaverexampleapp.global_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.R

class DataAdapter(val data:ArrayList<String> = ArrayList()) : RecyclerView.Adapter<DataAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_custom_cell,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataFound = data[position]
        holder.bind(dataFound)
    }

    override fun getItemCount() = data.size

    fun update(newData:ArrayList<String>)
    {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    class ViewHolder(view:View) : RecyclerView.ViewHolder(view)
    {
        private val title: TextView? = view.findViewById<TextView>(R.id.cell_title)

        fun bind(data:String)
        {
            title?.text = data
        }
    }
}