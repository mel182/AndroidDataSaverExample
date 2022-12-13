package com.example.custompagingcompose

data class ScreenState(
    val showInitialLoadingView: Boolean = true,
    val isLoading: Boolean = false,
    val isHistoryData: Boolean = false,
    val isInitialLoad: Boolean = true,
    val items:List<ComposePagingListItem> = emptyList(),
    val lastVisibleItem:ComposePagingListItem? = null,
    val error: String? = null,
    val historyEndReached: Boolean = false,
    val futureEndReached: Boolean = false,
    val futureKey: Long = 0,
    val historyKey: Long = 0
)
