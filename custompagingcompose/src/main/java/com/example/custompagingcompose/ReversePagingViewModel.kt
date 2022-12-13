package com.example.custompagingcompose

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class ReversePagingViewModel<Item> : ViewModel() {

    private var paginator:DefaultPaginator<Long,Item>? = null

    var state by mutableStateOf(ScreenState())

    open fun setPaginator(paginator:DefaultPaginator<Long,Item>) {
        this.paginator = initializePaginator()
    }

    abstract fun initializePaginator():DefaultPaginator<Long,Item>

    //abstract val paginator:DefaultPaginator<Long,Item>

//    private val paginator = DefaultPaginator<Long,Item>(
//        initialKey = state.futureKey,
//        onLoadUpdated = {
//            state = state.copy(isLoading = it)
//        },
//        onFutureRequest = { nextPage ->
//            //ComposePagingListRepository.getFutureItems(nextPage.toLong())
//            repository.getFutureItems(nextPage.toLong())
//        },
//        onHistoryRequest = { nextPage ->
////            ComposePagingListRepository.getHistoryItems(nextPage.toLong())
//            repository.getHistoryItems(nextPage.toLong())
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

    open fun setInitialLoadState(initialLoad:Boolean) {
        Log.i("TAG45","set initial load: ${initialLoad}")
        state = state.copy(isInitialLoad = initialLoad)
        Log.i("TAG45","new state: ${state}")
    }

    open fun setLastVisibleItem(composePagingItem:ComposePagingListItem? = null) {
        Log.i("TAG45","set last visible item: ${composePagingItem}")
        state = state.copy(lastVisibleItem = composePagingItem)
        Log.i("TAG45","new state: ${state}")
    }

    open fun loadNextItems() {
        viewModelScope.launch {
            Log.i("TAG55","paginator: ${paginator}")
            paginator?.loadNextItems()
        }
    }

    open fun loadInitialItems() {
        viewModelScope.launch {
            Log.i("TAG55","paginator: ${paginator}")
            paginator?.loadNextItems()
        }
    }

    open fun loadHistoryItems() {
        viewModelScope.launch {
            paginator?.loadHistoryItems()
        }
    }
}