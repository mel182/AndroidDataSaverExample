package com.example.datasaverexampleapp.recycler_view_item_animation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.databinding.ActivityRecyclerViewItemAnimationBinding
import com.example.datasaverexampleapp.paging.PagingItem
import com.example.datasaverexampleapp.type_alias.Layout

// This is an example of recycler view item animation.
// The recycler view in this example uses the '@anim/layout_animation' which contains the 'animation'
// element that hold the type of animation. In this case:
// - fall_down
// - rotate_in
// - scale_up
// - slide_in
class RecyclerViewItemAnimationActivity : AppCompatActivity(Layout.activity_recycler_view_item_animation) {

    private var binding : ActivityRecyclerViewItemAnimationBinding? = null
    private var recyclerViewListAdapter: RecyclerViewListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Recycler view item animation activity"
        recyclerViewListAdapter = RecyclerViewListAdapter()

        binding = DataBindingUtil.setContentView<ActivityRecyclerViewItemAnimationBinding>(this,Layout.activity_recycler_view_item_animation).apply {

            recyclerViewList.apply {
                layoutManager = LinearLayoutManager(this@RecyclerViewItemAnimationActivity)
                adapter = recyclerViewListAdapter
            }

            recyclerViewListAdapter?.setList(getDemoItems())
        }
    }

    private fun getDemoItems() : List<PagingItem> = listOf(
        PagingItem(1,"value1",20),
        PagingItem(2,"value2",21),
        PagingItem(3,"value3",22),
        PagingItem(4,"value4",23),
        PagingItem(5,"value5",25),
        PagingItem(6,"value6",26),
        PagingItem(7,"value7",27),
        PagingItem(8,"value8",28),
        PagingItem(9,"value9",29)
    )
}