package com.example.datasaverexampleapp.async_recycler_view

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.async_recycler_view.base.BaseDataBindingActivity
import com.example.datasaverexampleapp.async_recycler_view.base.BaseViewHolder
import com.example.datasaverexampleapp.async_recycler_view.custom_cell.AsyncRecyclerViewCustomCell
import com.example.datasaverexampleapp.async_recycler_view.interfaces.OnRecyclerClickListener
import com.example.datasaverexampleapp.async_recycler_view.model.RecyclerViewAsyncListItem
import com.example.datasaverexampleapp.base_classes.BaseActivity
import com.example.datasaverexampleapp.databinding.ActivityAsyncRecyclerViewExampleBinding
import com.example.datasaverexampleapp.type_alias.Layout

/**
 * In this example we are going your going to show how to optimized complex items in RecyclerView (and some animation in some time).
 * When you do layout inflation in runtime your app should render frames in about 16ms to achieve 60 frames per second.
 * It is very difficult to achieve when you load RecyclerView.
 * This example shows how to improve the loading performance and make the rendering faster.
 * Furthermore, we are going to implement a best practice to show how to use data binding efficiently.
 */
class AsyncRecyclerViewExampleActivity : BaseDataBindingActivity<ActivityAsyncRecyclerViewExampleBinding>(Layout.activity_async_recycler_view_example), OnRecyclerClickListener
{
    private var listAdapter: AsyncRecyclerViewListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Async loading Recycler view example"
        val dummyData = listOf(
            AsyncRecyclerViewCustomCell(RecyclerViewAsyncListItem(name = "John1", age = 21)),
            AsyncRecyclerViewCustomCell(RecyclerViewAsyncListItem(name = "John2", age = 22)),
            AsyncRecyclerViewCustomCell(RecyclerViewAsyncListItem(name = "John3", age = 23)),
            AsyncRecyclerViewCustomCell(RecyclerViewAsyncListItem(name = "John4", age = 24)),
            AsyncRecyclerViewCustomCell(RecyclerViewAsyncListItem(name = "John5", age = 25)),
            AsyncRecyclerViewCustomCell(RecyclerViewAsyncListItem(name = "John6", age = 26))
        )

        this.view?.apply {
            listAdapter = AsyncRecyclerViewListAdapter(itemList, this@AsyncRecyclerViewExampleActivity).apply {
                itemList.adapter = this
                itemList.layoutManager = CustomLinearLayoutManager(this@AsyncRecyclerViewExampleActivity)
                setData(dummyData)
            }
        }
    }

    override fun onItemClicked(position: Int, viewholder: BaseViewHolder<*>, view: View?, item: Any?, extras: Any?) {
        Log.i("TAG12","on item clicked!")
    }
}