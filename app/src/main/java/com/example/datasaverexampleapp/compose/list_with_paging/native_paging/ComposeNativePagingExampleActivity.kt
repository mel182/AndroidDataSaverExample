package com.example.datasaverexampleapp.compose.list_with_paging.native_paging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.datasaverexampleapp.paging.PagingViewModel

class ComposeNativePagingExampleActivity : AppCompatActivity() {

    private val pagingItemViewModel by viewModels<PagingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Compose native paging library"

        setContent {

            val pagingItemList = pagingItemViewModel.pagingItemsList.collectAsLazyPagingItems()

            LazyColumn{

                items(pagingItemList) { item ->
                    item?.let {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                        ) {
                            Text(
                                text = it.name,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = it.age.toString())
                        }
                    }

                    when(pagingItemList.loadState.append) {

                        is LoadState.NotLoading -> Unit
                        LoadState.Loading -> {
                           // Set loading layout
                        }

                        is LoadState.Error -> {
                            // Set error layout
                        }
                    }

                    // When the user open the view for the first time
                    when(pagingItemList.loadState.refresh) {
                        is LoadState.NotLoading -> Unit
                        LoadState.Loading -> {
                            // Set loading layout
                        }

                        is LoadState.Error -> {
                            // Set error layout
                        }
                    }
                }
            }
        }
    }
}