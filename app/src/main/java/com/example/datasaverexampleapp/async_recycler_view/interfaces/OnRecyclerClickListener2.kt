package com.example.datasaverexampleapp.async_recycler_view.interfaces

import android.view.View
import com.example.datasaverexampleapp.async_recycler_view.base.BaseViewHolder
import com.example.datasaverexampleapp.async_recycler_view.base.BaseViewHolder2

interface OnRecyclerClickListener2 {
    fun onItemClicked(position:Int, viewholder: BaseViewHolder2<*>, view: View?, item:Any?, extras: Any?)
}