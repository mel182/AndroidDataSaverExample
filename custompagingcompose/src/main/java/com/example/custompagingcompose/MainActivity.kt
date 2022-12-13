package com.example.custompagingcompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity()
{
    val viewModel = viewModels<ComposeListPagingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            ReversePagingView(
                viewModel = viewModel,
                loadingView = {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                },
                generalLoadingView = {

                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                           verticalArrangement = Arrangement.Center,
                           modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.LightGray)) {
                        CircularProgressIndicator()
                    }
                }
            )
        }
    }
}

@Composable
fun ReversePagingView(viewModel: Lazy<ReversePagingViewModel<*>>, loadingView: @Composable () -> Unit, generalLoadingView: @Composable () -> Unit) {

    Box(modifier = Modifier.fillMaxSize()) {

        // ---- lazy column
        val scope = rememberCoroutineScope()
        val listState = rememberLazyListState()

        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {

            item {
                AnimatedVisibility(
                    visible = viewModel.value.state.isLoading,
                    enter = fadeIn(),
                    exit = fadeOut(
                        animationSpec = tween(durationMillis = 500)
                    )
                ) {
                    loadingView()
                }
            }

            items(viewModel.value.state.items.size) { index ->

                Log.i("TAG55", "Item index: ${index}")

                val item = viewModel.value.state.items[index]

                // Detect when to load next items
                if (index >= viewModel.value.state.items.size - 1 && !viewModel.value.state.futureEndReached && !viewModel.value.state.isLoading) {
                    viewModel.value.loadNextItems()
                    Log.i("TAG55", "Bottom of list reached!")
                } else if (index == 0 && !viewModel.value.state.isInitialLoad && !viewModel.value.state.historyEndReached) {
                    Log.i("TAG55", "Top of list reached!")
                    Log.i("TAG55", "make history request")
                    viewModel.value.apply {
                        setLastVisibleItem(viewModel.value.state.items[index])
                        loadHistoryItems()
                    }
                }

                    Column(
                        modifier = Modifier
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

                scope.launch {
                    if (viewModel.value.state.isInitialLoad) {
                        scope.launch {
                            viewModel.value.state.items.asSequence().filter { it.title == "Item 8" }
                                .firstOrNull()?.let { index_of_test ->
                                val index = viewModel.value.state.items.indexOf(index_of_test)
                                //delay(2000)
                                listState.animateScrollToItem(index)
                                viewModel.value.setInitialLoadState(initialLoad = false)
                                Log.i(
                                    "TAG45",
                                    "initial load after: ${viewModel.value.state.isInitialLoad}"
                                )
                            }
                        }
                    } else if (viewModel.value.state.isHistoryData) {
                        Log.i("TAG45", "is history data!")
                        viewModel.value.state.lastVisibleItem?.let { lastVisibleItem ->
                            val itemIndex = viewModel.value.state.items.indexOf(lastVisibleItem)
                            Log.i("TAG45", "last visible item index: ${itemIndex}")
                            if (itemIndex != -1) {
                                listState.scrollToItem(itemIndex)
                                viewModel.value.setLastVisibleItem()
                            }
                        }
                    }
                }
        }

        // ---- lazy column
        AnimatedVisibility(
                visible = viewModel.value.state.showInitialLoadingView,
                enter = fadeIn(),
                exit = fadeOut(
                    animationSpec = tween(durationMillis = 500)
                )
            ) {
                generalLoadingView()
            }


        }
}