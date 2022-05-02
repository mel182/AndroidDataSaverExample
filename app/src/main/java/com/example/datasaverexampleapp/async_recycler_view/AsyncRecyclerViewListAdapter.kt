package com.example.datasaverexampleapp.async_recycler_view

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.async_recycler_view.base.BaseRecyclerAdapter
import com.example.datasaverexampleapp.async_recycler_view.interfaces.OnRecyclerClickListener

class AsyncRecyclerViewListAdapter(context: Context,recyclerView: RecyclerView, listener: OnRecyclerClickListener? = null) : BaseRecyclerAdapter(context,recyclerView, listener)