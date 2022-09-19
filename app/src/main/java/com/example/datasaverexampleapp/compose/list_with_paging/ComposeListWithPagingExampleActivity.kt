package com.example.datasaverexampleapp.compose.list_with_paging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

class ComposeListWithPagingExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Compose list with paging example"

        setContent {
            val viewModel = viewModel<ComposeListPagingViewModel>()
            val state = viewModel.state
            LazyColumn(modifier = Modifier.fillMaxSize()) {

                items(state.items.size) { index ->

                    val item = state.items[index]

                    // Detect when to load next items
                    if (index >= state.items.size - 1 && !state.endReached && !state.isLoading) {
                        viewModel.loadNextItems()
                    }

                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                    ) {
                        Text(
                            text = item.title,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = item.description)
                    }
                }

                item {
                    if (state.isLoading) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}