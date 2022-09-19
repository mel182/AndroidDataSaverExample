package com.example.datasaverexampleapp.compose.list_with_paging.native_paging

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.datasaverexampleapp.paging.PagingViewModel

class ComposeNativePagingExampleActivity : AppCompatActivity() {

    private val pagingItemViewModel by viewModels<PagingViewModel>()

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Compose native paging library"

        setContent {

            val pagingItemList = pagingItemViewModel.pagingItemsList.collectAsLazyPagingItems()
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