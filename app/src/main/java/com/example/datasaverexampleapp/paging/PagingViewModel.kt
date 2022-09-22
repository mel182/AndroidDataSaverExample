package com.example.datasaverexampleapp.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PagingViewModel : ViewModel()
{
    // -------------  Flow tips Simple example  ---------------- \\
    private val _pagingItems = MutableStateFlow<List<PagingItem>>(emptyList())
    val pagingItems = _pagingItems.asStateFlow()

    // Function always update de paging item list flow state every time the list get updated!
    val localPagingItem = pagingItems.map { PagingItem ->
        PagingItem.find { it.name == "local" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(),null)


    // Dummy add paging item to list function
    fun testAddPagingItem(pagingItem:PagingItem) {
        _pagingItems.update { it + pagingItem }
    }

    // Dummy update paging item in list
    fun testUpdatePagingItem(pagingItem:PagingItem) {
        _pagingItems.update { it + pagingItem }

        _pagingItems.update {
            it.map { currentPagingItem ->
                if (currentPagingItem.id == pagingItem.id) pagingItem else currentPagingItem
            }
        }
    }
    // -------------  Flow tips Simple example  ---------------- \\

    // -------------  Flow tips Complex example  ---------------- \\
    private val isRefreshing = MutableStateFlow(true)
    private val pagingItemsList2 = MutableStateFlow<List<PagingItem>>(emptyList())

    // The combine lambda get called every time one of the state changes
    val pagingListItem2State = combine(isRefreshing,pagingItemsList2) { isRefreshing, pagingList ->
       // This section get called when one the states changes

        if (isRefreshing)
        {
            // Do something with refreshing
        }

        pagingList
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(),null)
    // -------------  Flow tips Complex example  ---------------- \\

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshingPagingList: StateFlow<Boolean>
    get() = _isRefreshing.asStateFlow()

    fun refreshList() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
        }
    }

    fun stopRefreshList() {
        if (_isRefreshing.value) {
            viewModelScope.launch {
                delay(1000L)
                _isRefreshing.emit(false)
            }
        }
    }

    val pagingItemsList : Flow<PagingData<PagingItem>> = Pager(PagingConfig(pageSize = 20), initialKey = 0) {
        PagingItemSource()
    }.flow.cachedIn(viewModelScope)

    val pagingItemsListSource2 : Flow<PagingData<PagingItem>> = Pager(PagingConfig(pageSize = 20), initialKey = 0) {
        PagingItemSource2 {
            stopRefreshList()
        }
    }.flow.cachedIn(viewModelScope)
}