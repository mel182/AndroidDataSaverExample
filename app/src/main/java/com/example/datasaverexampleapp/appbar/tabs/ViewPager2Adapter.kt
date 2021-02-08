package com.example.datasaverexampleapp.appbar.tabs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.appbar.scrolling_techniques.DataAdapter

class ViewPager2Adapter(val itemList:ArrayList<String>) : RecyclerView.Adapter<ViewPager2Adapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_data_list,parent,false)
        return ViewHolder(parent.context,view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(val context:Context,view:View) : RecyclerView.ViewHolder(view)
    {
        val dataList = view.findViewById<RecyclerView>(R.id.item_list)

        fun bind()
        {
            dataList?.apply {

                val data = arrayListOf(
                    "value 1", "value 2", "value 3",
                    "value 4", "value 5", "value 6",
                    "value 7", "value 8", "value 9",
                    "value 10", "value 11", "value 12",
                    "value 13", "value 14", "value 15",
                    "value 16", "value 17", "value 18",
                    "value 19", "value 20", "value 21",
                    "value 22", "value 23", "value 24",
                    "value 25", "value 26", "value 27",
                    "value 28", "value 29", "value 30",
                    "value 31", "value 32", "value 33")

                layoutManager = LinearLayoutManager(context)
                adapter = DataAdapter(data)
            }
        }
    }
}