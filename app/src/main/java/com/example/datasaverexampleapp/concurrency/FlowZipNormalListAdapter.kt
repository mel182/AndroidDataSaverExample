package com.example.datasaverexampleapp.concurrency

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.R

class FlowZipNormalListAdapter : RecyclerView.Adapter<FlowZipNormalListAdapter.ViewHolder>()
{
    private val dataList:ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.zip_normal_custom_cell,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataFound = dataList[position]
        holder.bind(dataFound)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateList(data: String)
    {
        dataList.add(data)
        val index = dataList.indexOf(data)
        notifyItemInserted(index);
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val title = view.findViewById<TextView>(R.id.title)

        fun bind(data:String)
        {
            title?.text = data
        }
    }
}