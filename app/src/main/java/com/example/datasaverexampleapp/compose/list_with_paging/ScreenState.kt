package com.example.datasaverexampleapp.compose.list_with_paging

data class ScreenState(
    val isLoading: Boolean = false,
    val items:List<ComposePagingListItem> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 0
)
