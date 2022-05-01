package com.example.datasaverexampleapp.async_recycler_view.custom_cell

import com.example.datasaverexampleapp.async_recycler_view.base.BaseItemWrapper
import com.example.datasaverexampleapp.async_recycler_view.model.RecyclerViewAsyncListItem
import com.example.datasaverexampleapp.databinding.HolderAsyncRecyclerviewLoadingCustomCellBinding
import com.example.datasaverexampleapp.type_alias.Layout

class AsyncRecyclerViewCustomCell(val data: RecyclerViewAsyncListItem) : BaseItemWrapper<HolderAsyncRecyclerviewLoadingCustomCellBinding>(Layout.holder_async_recyclerview_loading_custom_cell)
{
    override fun populate(binding: HolderAsyncRecyclerviewLoadingCustomCellBinding?) {
        super.populate(binding)

    }
}