package com.example.custompagingcompose

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class ReversePagingViewModel<Item> : ViewModel() {

    private var paginator:DefaultPaginator<Long,Item>? = null

    var currentDisplayedTitle: String = ""

    var state by mutableStateOf(ScreenState())

    private val _show_sticky_header = MutableStateFlow(true)
    val showStickyHeader: StateFlow<Boolean>
        get() = _show_sticky_header.asStateFlow()

    private val _sticky_header_title = MutableStateFlow("")
    val stickyHeaderTitle: StateFlow<String>
        get() = _sticky_header_title.asStateFlow()

    fun setStickyHeaderTitle(title:String) {

        if (title != currentDisplayedTitle) {
            currentDisplayedTitle = title
            _sticky_header_title.value = title
        }
    }

    open fun setPaginator(paginator:DefaultPaginator<Long,Item>) {
        this.paginator = initializePaginator()
    }

    abstract fun initializePaginator():DefaultPaginator<Long,Item>

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