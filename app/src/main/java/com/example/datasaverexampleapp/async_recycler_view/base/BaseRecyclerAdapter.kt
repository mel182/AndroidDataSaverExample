package com.example.datasaverexampleapp.async_recycler_view.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.async_recycler_view.CustomLinearLayoutManager
import com.example.datasaverexampleapp.async_recycler_view.custom_cell.AsyncRecyclerViewCustomCell
import com.example.datasaverexampleapp.async_recycler_view.interfaces.OnRecyclerClickListener
import com.example.datasaverexampleapp.async_recycler_view.model.RecyclerViewAsyncListItem
import com.example.datasaverexampleapp.databinding.HolderAsyncRecyclerviewLoadingCustomCellBinding

open class BaseRecyclerAdapter(context:Context?, val recyclerView:RecyclerView?, val onRecyclerViewClickListener: OnRecyclerClickListener? = null) : RecyclerView.Adapter<BaseViewHolder<*>>()
{
    private val data: ArrayList<BaseItemWrapper<*>> = ArrayList()
    val layoutManager: RecyclerView.LayoutManager = CustomLinearLayoutManager(context = context).apply {
        recyclerView?.layoutManager = this
    }

    open fun notifyDataSetChangeOnUIThread() {

        recyclerView?.apply {

            if (!isComputingLayout && scrollState == RecyclerView.SCROLL_STATE_IDLE)
                notifyDataSetChanged()
        }
    }

    fun setData(data: List<BaseItemWrapper<*>>) {

        if (recyclerView?.scrollState != RecyclerView.SCROLL_STATE_IDLE)
        {
            postSetData(data)
            return
        }

        loadData(data)
        notifyDataSetChangeOnUIThread()
    }

    private fun loadData(list: List<BaseItemWrapper<*>>) {
        data.apply {
            clear()
            addAll(list)
        }
    }

    open fun clearData()
    {
        data.clear()
        notifyDataSetChangeOnUIThread()
    }

    private fun postSetData(data: List<BaseItemWrapper<*>>) {

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    setData(data)
                    recyclerView.removeOnScrollListener(this)
                }
            }
        })
    }

    private fun getItem(position:Int) : BaseItemWrapper<*>? = if (position < data.size) data.get(position) else null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return BaseViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), viewType, parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        getItem(position)?.let { item ->

            if (item is AsyncRecyclerViewCustomCell)
                item.populate(holder.binding as HolderAsyncRecyclerviewLoadingCustomCellBinding)

            onRecyclerViewClickListener?.apply {
                holder.setOnRecyclerViewClickListener(this)
            }
        }
    }

    override fun getItemCount(): Int = data.size
}