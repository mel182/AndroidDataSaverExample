package com.example.datasaverexampleapp.recycler_view_item_animation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.paging.PagingItem
import com.example.datasaverexampleapp.type_alias.Layout

class RecyclerViewListAdapter : RecyclerView.Adapter<RecyclerViewListAdapter.AnimationItemViewModel>() {

//    ListAdapter<PagingItem, RecyclerViewListAdapter.AnimationItemViewModel>(Companion) {

//    companion object : DiffUtil.ItemCallback<PagingItem>(){
//        override fun areItemsTheSame(oldItem: PagingItem, newItem: PagingItem): Boolean = oldItem == newItem
//        override fun areContentsTheSame(oldItem: PagingItem, newItem: PagingItem): Boolean = oldItem == newItem
//    }

    private val list: ArrayList<PagingItem> = ArrayList()

    fun setList(list:List<PagingItem>) {
        this.list.apply {
            clear()
            for (pagingItemFound in list) {
                add(pagingItemFound)
                val index = list.indexOf(pagingItemFound)

                if (index != -1)
                    notifyItemInserted(index)
            }
        }
    }

    fun clearList() {
        this.list.apply {
            clear()
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimationItemViewModel {
        return AnimationItemViewModel(
            LayoutInflater.from(parent.context)
                .inflate(Layout.item_paging_custom_cell2, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AnimationItemViewModel, position: Int) {
        list[position].let { item ->
            holder.bind(item)
        }

//        currentList[position].let { item ->
//            holder.bind(item)
//        }
    }

    override fun getItemCount(): Int = list.size

    inner class AnimationItemViewModel(view: View) : RecyclerView.ViewHolder(view)
    {
        private val nameTextView = view.findViewById<TextView>(R.id.name_paging_textView)
        private val ageTextView = view.findViewById<TextView>(R.id.age_paging_textView)

        fun bind(item: PagingItem)
        {
            nameTextView.text = item.name
            ageTextView.text = item.age.toString()
        }
    }
}