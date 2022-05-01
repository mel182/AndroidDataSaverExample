package com.example.datasaverexampleapp.motion_layout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.paging.PagingItem
import com.example.datasaverexampleapp.type_alias.Layout
import com.example.datasaverexampleapp.type_alias.ViewByID

class MotionLayoutListAdapter : RecyclerView.Adapter<MotionLayoutListAdapter.DemoViewHolder>()
{
    private val listItems = ArrayList<PagingItem>()

    fun setList(list:ArrayList<PagingItem>)
    {
        listItems.apply {
            clear()

            for (pagingItemFound in list) {
                add(pagingItemFound)
                val index = indexOf(pagingItemFound)
                if (index != -1)
                    notifyItemInserted(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemoViewHolder {
        return DemoViewHolder(LayoutInflater.from(parent.context).inflate(Layout.item_paging_custom_cell,parent,false))
    }

    override fun onBindViewHolder(holder: DemoViewHolder, position: Int) {
        val item = listItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = listItems.size

    inner class DemoViewHolder(view:View) : RecyclerView.ViewHolder(view) {

        private val nameTextView = view.findViewById<TextView>(ViewByID.name_paging_textView)
        private val ageTextView = view.findViewById<TextView>(ViewByID.age_paging_textView)

        fun bind(item: PagingItem)
        {
            nameTextView.text = item.name
            ageTextView.text = item.age.toString()
        }
    }
}