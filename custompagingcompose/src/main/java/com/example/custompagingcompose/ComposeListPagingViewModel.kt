package com.example.custompagingcompose

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ComposeListPagingViewModel : ReversePagingViewModel<ComposePagingListItem>() {

    init {
        Log.i("TAG55","init view model")
        setPaginator(initializePaginator())
        loadNextItems()
    }

    override fun setPaginator(paginator: DefaultPaginator<Long, ComposePagingListItem>) {
        super.setPaginator(initializePaginator())
    }

    override fun initializePaginator(): DefaultPaginator<Long, ComposePagingListItem> {

        return DefaultPaginator(
            initialKey = state.futureKey,
            onLoadUpdated = {
                state = state.copy(isLoading = it)
            },
            onFutureRequest = { nextKey ->
                ComposePagingListRepository.getFutureItems(nextKey)
            },
            onHistoryRequest = { nextKey ->
                ComposePagingListRepository.getHistoryItems(nextKey)
            },
            getNextKey = {
                state.futureKey + 1
            },
            getNextHistoryKey = {
                state.historyKey + 1
            },
            onError = {
                state = state.copy(error = it?.localizedMessage)
            },
            onHistorySuccess = { items, newKey ->
                state = state.copy(
                    items = items + state.items,
                    historyKey = newKey,
                    isHistoryData = true,
                    showInitialLoadingView = false,
                    isInitialLoad = state.isInitialLoad,
                    historyEndReached = true
                )
                Log.i("TAG55","on history success: ${state}")
            },
            onFutureSuccess = { items, newKey ->
                state = state.copy(
                    items = state.items + items,
                    futureKey = newKey,
                    isHistoryData = false,
                    showInitialLoadingView = false,
                    isInitialLoad = state.isInitialLoad,
                    futureEndReached = items.isEmpty()
                )
                Log.i("TAG55","on success: ${state}")
            }
        )
    }


    //    var state by mutableStateOf(ScreenState())
//
//    private val paginator = DefaultPaginator(
//        initialKey = state.futureKey,
//        onLoadUpdated = {
//            state = state.copy(isLoading = it)
//        },
//        onFutureRequest = { nextPage ->
//            ComposePagingListRepository.getFutureItems(nextPage, 20)
//        },
//        onHistoryRequest = { nextPage ->
//            ComposePagingListRepository.getHistoryItems(nextPage, 20)
//        },
//        getNextKey = {
//            state.futureKey + 1
//        },
//        getNextHistoryKey = {
//            state.futureKey + 1
//        },
//        onError = {
//            state = state.copy(error = it?.localizedMessage)
//        },
//        onHistorySuccess = { items, newKey ->
//            state = state.copy(
//                items = items + state.items,
//                historyKey = newKey,
//                isHistoryData = true,
//                showInitialLoadingView = false,
//                isInitialLoad = state.isInitialLoad,
//                historyEndReached = true
//            )
//            Log.i("TAG55","on history success: ${state}")
//        },
//        onFutureSuccess = { items, newKey ->
//            state = state.copy(
//                items = state.items + items,
//                futureKey = newKey,
//                isHistoryData = false,
//                showInitialLoadingView = false,
//                isInitialLoad = state.isInitialLoad,
//                futureEndReached = items.isEmpty()
//            )
//            Log.i("TAG55","on success: ${state}")
//        }
//    )
//
//    init {
//        Log.i("TAG45","init view model")
//        loadNextItems()
//    }
//
//    fun setInitialLoadState(initialLoad:Boolean) {
//        Log.i("TAG45","set initial load: ${initialLoad}")
//        state = state.copy(isInitialLoad = initialLoad)
//        Log.i("TAG45","new state: ${state}")
//    }
//
//    fun setLastVisibleItem(composePagingItem:ComposePagingListItem? = null) {
//        Log.i("TAG45","set last visible item: ${composePagingItem}")
//        state = state.copy(lastVisibleItem = composePagingItem)
//        Log.i("TAG45","new state: ${state}")
//    }
//
//    fun loadNextItems() {
//        viewModelScope.launch {
//            paginator.loadNextItems()
//        }
//    }
//
//    fun loadHistoryItems() {
//        viewModelScope.launch {
//            paginator.loadHistoryItems()
//        }
//    }

}