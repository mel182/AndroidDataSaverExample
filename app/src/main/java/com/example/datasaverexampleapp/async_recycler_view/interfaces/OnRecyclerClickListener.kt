package com.example.datasaverexampleapp.async_recycler_view.interfaces

import android.view.View
import com.example.datasaverexampleapp.async_recycler_view.base.BaseViewHolder

interface OnRecyclerClickListener {
    fun onItemClicked(position:Int, viewholder: BaseViewHolder<*>, view: View?, item:Any?, extras: Any?)
}