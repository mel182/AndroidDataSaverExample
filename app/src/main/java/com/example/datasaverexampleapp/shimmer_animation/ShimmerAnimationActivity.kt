package com.example.datasaverexampleapp.shimmer_animation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.databinding.ActivityShimmerAnimationBinding
import com.example.datasaverexampleapp.paging.PagingItem
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShimmerAnimationActivity : AppCompatActivity(Layout.activity_shimmer_animation) {

    var binding : ActivityShimmerAnimationBinding? = null
    var shimmerListAdapter : ShimmerListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Shimmer animation example"

        shimmerListAdapter = ShimmerListAdapter()
        binding = DataBindingUtil.setContentView<ActivityShimmerAnimationBinding>(this, Layout.activity_shimmer_animation).apply {

            itemRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@ShimmerAnimationActivity)
                adapter = shimmerListAdapter
            }

            shimmerLayout.startShimmer()

            CoroutineScope(Dispatchers.Main).launch {

                delay(3000)
                shimmerLayout.stopShimmer()
                shimmerLayoutContent.visibility = View.GONE
                shimmerListAdapter?.setList(getDemoItems())
            }
        }
    }

    private fun getDemoItems() : List<PagingItem> = listOf(PagingItem(1,"value1",20),
        PagingItem(2,"value2",21),
        PagingItem(3,"value3",22),
        PagingItem(4,"value4",23),
        PagingItem(5,"value5",25),
        PagingItem(6,"value6",26),
        PagingItem(7,"value7",27),
        PagingItem(8,"value8",28),
        PagingItem(9,"value9",29))
}