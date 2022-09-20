package com.example.datasaverexampleapp.compose.list_with_paging.native_paging

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.datasaverexampleapp.compose.extensions.toPx
import com.example.datasaverexampleapp.paging.PagingViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

class ComposeNativePagingExampleActivity : AppCompatActivity() {

    private val pagingItemViewModel by viewModels<PagingViewModel>()

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Compose native paging library"

        setContent {

            Column(modifier = Modifier.fillMaxWidth()) {

                var isRefreshing by remember { mutableStateOf(false) }
                val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
                val pagingItemList = pagingItemViewModel.pagingItemsList.collectAsLazyPagingItems()

                SwipeRefresh(state = swipeRefreshState, onRefresh = {
                    isRefreshing = true
                    pagingItemList.refresh()
                }, indicator = { state, trigger ->
                    CustomIndicator(state,trigger)
                }) {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {

                        val itemCount = pagingItemList.itemCount
                        var lastAgeGroup: Int? = null

                        if (isRefreshing) {
                            isRefreshing = false
                        }

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

@Composable
fun CustomIndicator(swipeRefreshState: SwipeRefreshState, refreshTriggerDistance: Dp, color: Color = Color.LightGray) {

    if (swipeRefreshState.isRefreshing) {
        Text(text = "Test", modifier = Modifier.fillMaxWidth())
    }



//    Box(modifier = Modifier
//        .drawWithCache {
//            onDrawBehind {
//                val distance = refreshTriggerDistance.toPx()
//                val progress = (swipeRefreshState.indicatorOffset / distance).coerceIn(0f, 1f)
//                val brush = Brush.verticalGradient(
//                    0f to color.copy(alpha = 0.45f),
//                    1f to color.copy(alpha = 0f)
//                )
//
//                drawRect(brush = brush, alpha = FastOutSlowInEasing.transform(progress))
//            }
//        }
//        .fillMaxWidth()
//        .height(80.dp)) {
//
//        if (swipeRefreshState.isRefreshing) {
//            LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Color.Blue)
//        } else {
//            val trigger = refreshTriggerDistance.toPx()
//            val progress = (swipeRefreshState.indicatorOffset / trigger).coerceIn(0f,1f)
//            LinearProgressIndicator(
//                progress = progress,
//                modifier = Modifier.fillMaxWidth(),
//                color = Color.Green
//            )
//        }
//    }





}