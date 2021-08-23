package com.example.datasaverexampleapp.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.R

class PagingExampleListAdapter : PagingDataAdapter<PagingItem, PagingExampleListAdapter.ViewModel>(PagingItemComparator) {

    override fun onBindViewHolder(holder: ViewModel, position: Int)
    {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        return ViewModel(LayoutInflater.from(parent.context).inflate(R.layout.item_paging_custom_cell,parent,false))
    }

    inner class ViewModel(view: View) : RecyclerView.ViewHolder(view)
    {
        private val nameTextView = view.findViewById<TextView>(R.id.name_paging_textView)
        private val ageTextView = view.findViewById<TextView>(R.id.age_paging_textView)

        fun bind(item:PagingItem)
        {
            nameTextView.text = item.name
            ageTextView.text = item.age.toString()
        }
    }

    object PagingItemComparator : DiffUtil.ItemCallback<PagingItem>() {
        override fun areItemsTheSame(oldItem: PagingItem, newItem: PagingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PagingItem, newItem: PagingItem): Boolean {
            return oldItem == newItem
        }
    }
}