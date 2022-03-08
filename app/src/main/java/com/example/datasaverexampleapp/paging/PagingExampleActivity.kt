package com.example.datasaverexampleapp.paging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.datasaverexampleapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PagingExampleActivity : AppCompatActivity() {

    private val listAdapter = PagingExampleListAdapter()
    private lateinit var pagingViewModel: PagingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paging_example)
        title = "Paging Example"
        pagingViewModel = ViewModelProvider(this).get(PagingViewModel::class.java)

        findViewById<SwipeRefreshLayout>(R.id.refresh_layout)?.apply {

            setOnRefreshListener {
                listAdapter.refresh()
                isRefreshing = false
            }
        }

        findViewById<RecyclerView>(R.id.paging_recycler_list)?.apply {
            layoutManager = LinearLayoutManager(this@PagingExampleActivity)
            setHasFixedSize(true)
            adapter = listAdapter

            addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager
                    if (layoutManager is LinearLayoutManager)
                    {
                        if (layoutManager.findFirstVisibleItemPosition() == 0)
                        {
                            Log.i("TAG64","List reach the top")
                        }


                        if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1)
                        {
                            Log.i("TAG64","List reach the bottom")
                        }
                    }
                }
            })
        }

        CoroutineScope(Dispatchers.Main).launch {
            Log.i("TAG65","loading paging data to list....")
            pagingViewModel.pagingItemsList.collectLatest {
                Log.i("TAG65","Paging data received!")
                listAdapter.submitData(it)
            }
        }
    }
}