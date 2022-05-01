package com.example.datasaverexampleapp.async_recycler_view.base

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.async_recycler_view.interfaces.OnRecyclerClickListener

class BaseViewHolder<Binding : ViewDataBinding>(val binding: Binding) : RecyclerView.ViewHolder(binding.root)
{
    init {
        binding.setVariable(com.example.datasaverexampleapp.BR.holder,this)
    }

    var onRecyclerViewClickListener: OnRecyclerClickListener? = null
    private set

    fun setOnRecyclerViewClickListener(listener:OnRecyclerClickListener?)
    {
        onRecyclerViewClickListener = listener
    }

    open fun onClick(view: View, wrapper: BaseItemWrapper<*>) {
        onClick(view = view, wrapper = wrapper, item = null)
    }

    open fun onClick(view: View, wrapper: BaseItemWrapper<*>, item:Any?) {
        onRecyclerViewClickListener?.onItemClicked(position = absoluteAdapterPosition, viewholder = this, view = view, item = wrapper, extras = item)
    }

}