package com.example.datasaverexampleapp.shimmer_animation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.paging.PagingItem
import com.example.datasaverexampleapp.type_alias.Layout

class ShimmerListAdapter : RecyclerView.Adapter<ShimmerListAdapter.ShimmerViewModel>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewModel {
        return ShimmerViewModel(LayoutInflater.from(parent.context).inflate(Layout.item_paging_custom_cell,parent,false))
    }

    override fun onBindViewHolder(holder: ShimmerViewModel, position: Int) {
        list[position].let { item ->
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ShimmerViewModel(view: View) : RecyclerView.ViewHolder(view)
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