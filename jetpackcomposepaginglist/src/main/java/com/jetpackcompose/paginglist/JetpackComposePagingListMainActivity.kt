@file:OptIn(ExperimentalFoundationApi::class)

package com.jetpackcompose.paginglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.jetpackcompose.paginglist.viewmodels.PagingViewModel

class JetpackComposePagingListMainActivity : ComponentActivity() {

    private val pagingItemViewModel by viewModels<PagingViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Compose native paging library"

        setContent {

            /*
            indicator = { state, trigger ->
                    CustomIndicator(swipeRefreshState = state, refreshTriggerDistance = trigger)
                }
            */

            Column(modifier = Modifier.fillMaxWidth()) {
                val isRefreshingPagingList by pagingItemViewModel.isRefreshingPagingList.collectAsState()
                val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshingPagingList)
                val pagingItemList = pagingItemViewModel.pagingItemsListSource2.collectAsLazyPagingItems()

                SwipeRefresh(state = swipeRefreshState, onRefresh = {
                    pagingItemViewModel.refreshList()
                    pagingItemList.refresh()
                }, indicator = { state, trigger ->
                    SwipeRefreshIndicator(state = state, refreshTriggerDistance = trigger, scale = true, backgroundColor = Color.Green, shape = MaterialTheme.shapes.small)
                }) {

                    LazyColumn(modifier = Modifier.fillMaxWidth()) {

                        val itemCount = pagingItemList.itemCount
                        var lastAgeGroup: Int? = null

                        for (index in 0 until itemCount) {

                            pagingItemList.peek(index)?.let { nextItem ->
                                val age = nextItem.age
                                // create a header for every 10y of age
                                if (lastAgeGroup == null || age > lastAgeGroup!!) {
                                    lastAgeGroup = (lastAgeGroup ?: 0) + 10
                                    val ageGroup = lastAgeGroup
                                    stickyHeader {
                                        Text(text = "<= $ageGroup", modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.LightGray)
                                            .padding(16.dp))
                                    }
                                }

                                pagingItemList[index]?.let { listItem ->
                                    item(key = listItem.name) {

                                        Column(modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                        ) {
                                            Text(
                                                text = listItem.name,
                                                fontSize = 20.sp,
                                                color = Color.Black
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(text = listItem.age.toString())
                                        }
                                    }
                                }
                            }
                        }


                        // the following code code is without sticky header only native paging
//                items(pagingItemList) { item ->
//                    item?.let {
//                        Column(modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                        ) {
//                            Text(
//                                text = it.name,
//                                fontSize = 20.sp,
//                                color = Color.Black
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                            Text(text = it.age.toString())
//                        }
//                    }
//
//                    when(pagingItemList.loadState.append) {
//
//                        is LoadState.NotLoading -> Unit
//                        LoadState.Loading -> {
//                           // Set loading layout
//                        }
//
//                        is LoadState.Error -> {
//                            // Set error layout
//                        }
//                    }
//
//                    // When the user open the view for the first time
//                    when(pagingItemList.loadState.refresh) {
//                        is LoadState.NotLoading -> Unit
//                        LoadState.Loading -> {
//                            // Set loading layout
//                        }
//
//                        is LoadState.Error -> {
//                            // Set error layout
//                        }
//                    }
//                }
                    }
                }
            }
        }
    }
}