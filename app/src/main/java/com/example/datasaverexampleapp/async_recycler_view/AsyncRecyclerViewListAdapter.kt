package com.example.datasaverexampleapp.async_recycler_view

import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.async_recycler_view.base.BaseRecyclerAdapter2
import com.example.datasaverexampleapp.async_recycler_view.interfaces.OnRecyclerClickListener

class AsyncRecyclerViewListAdapter(recyclerView: RecyclerView, listener: OnRecyclerClickListener? = null) : BaseRecyclerAdapter2(recyclerView, listener)