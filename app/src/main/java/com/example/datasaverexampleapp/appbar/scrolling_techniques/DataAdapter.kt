package com.example.datasaverexampleapp.appbar.scrolling_techniques

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.item_custom_cell.view.*

class DataAdapter(val data:ArrayList<String>) : RecyclerView.Adapter<DataAdapter.ViewHolder>()
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

    class ViewHolder(view:View) : RecyclerView.ViewHolder(view)
    {
        private val title: TextView? = view.findViewById<TextView>(R.id.cell_title)

        fun bind(data:String)
        {
            title?.text = data
        }
    }
}