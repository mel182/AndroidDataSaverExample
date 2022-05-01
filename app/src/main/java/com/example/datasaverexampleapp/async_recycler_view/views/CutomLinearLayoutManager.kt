package com.example.datasaverexampleapp.async_recycler_view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.IndexOutOfBoundsException

class CustomLinearLayoutManager : LinearLayoutManager
{
    constructor(context: Context?) : super(context)

    constructor(context: Context?, orientation:Int, reverseLayout: Boolean) : super(context)

    constructor(context: Context?, attr: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attr, defStyleAttr, defStyleRes)

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {

        try {
            super.onLayoutChildren(recycler, state)
        }catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }
}