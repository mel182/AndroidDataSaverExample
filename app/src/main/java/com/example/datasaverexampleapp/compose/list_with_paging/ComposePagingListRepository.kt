package com.example.datasaverexampleapp.compose.list_with_paging

import kotlinx.coroutines.delay

object ComposePagingListRepository {

    private val dummyRemoteDataSource = (1..100).map {
        ComposePagingListItem(
            title = "Item $it",
            description = "Description $it"
        )
    }

    suspend fun getItems(page:Int, pageSize:Int): Result<List<ComposePagingListItem>> {
        delay(2000L)
        val startingIndex = page * pageSize
        return if (startingIndex + pageSize <= dummyRemoteDataSource.size) {
            Result.success(dummyRemoteDataSource.slice(startingIndex until startingIndex + pageSize))
        } else Result.success(emptyList())
    }
}