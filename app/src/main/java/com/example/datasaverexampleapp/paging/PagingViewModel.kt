package com.example.datasaverexampleapp.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow

class PagingViewModel : ViewModel()
{
    val pagingItemsList : Flow<PagingData<PagingItem>> = Pager(PagingConfig(pageSize = 20), initialKey = 0) {
        PagingItemSource()
    }.flow.cachedIn(viewModelScope)
}