package com.example.datasaverexampleapp.implicit_delegation_example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.R

class ImplicitItemAdapter(val data:ArrayList<String>,private val onClickListener: View.OnClickListener) : RecyclerView.Adapter<ImplicitItemAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_custom_cell,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataFound = data[position]
        holder.bind(dataFound)
        holder.title?.setOnClickListener(onClickListener)
    }

    override fun getItemCount() = data.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val title: TextView? = view.findViewById<TextView>(R.id.cell_title)

        fun bind(data:String)
        {
            title?.text = data
        }
    }
}