package com.example.datasaverexampleapp.paging

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

class PagingViewModel : ViewModel()
{
    val pagingItemsList : Flow<PagingData<PagingItem>> = Pager(PagingConfig(pageSize = 20)) {
        PagingItemSource()
    }.flow
}