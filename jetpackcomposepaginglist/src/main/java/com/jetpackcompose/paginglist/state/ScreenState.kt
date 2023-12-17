package com.jetpackcompose.paginglist.state

import com.jetpackcompose.paginglist.models.ComposePagingListItem

data class ScreenState(
    val isLoading: Boolean = false,
    val items:List<ComposePagingListItem> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 0
)
