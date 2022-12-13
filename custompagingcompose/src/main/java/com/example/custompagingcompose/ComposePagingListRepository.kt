package com.example.custompagingcompose

import android.util.Log
import kotlinx.coroutines.delay

object ComposePagingListRepository : ReversePagingRepository<Long,ComposePagingListItem>(pageSize = 20) {

    private val dummyRemoteDataSource = (1..100).map {
        ComposePagingListItem(
            id = it.toString(),
            title = "Item $it",
            category = "Future",
            description = "Description $it"
        )
    }

    override suspend fun getFutureItems(key: Long): Result<List<ComposePagingListItem>> {
        Log.i("TAG55","get items, key: $key - page size: $pageSize")
        delay(2000L)
        val startingIndex = key.toInt() * pageSize
        return if (startingIndex + pageSize <= dummyRemoteDataSource.size) {
            Result.success(dummyRemoteDataSource.slice(startingIndex until startingIndex + pageSize))
        } else Result.success(emptyList())
    }

    override suspend fun getHistoryItems(key: Long): Result<List<ComposePagingListItem>> {
        Log.i("TAG55","get items, key: $key - page size: $pageSize")
        delay(2000L)
        val testData = listOf(
            ComposePagingListItem(
                id = "h1",
                title = "History Item 1",
                category = "History",
                description = "History description 1"
            ),
            ComposePagingListItem(
                id = "h2",
                title = "History Item 2",
                category = "History",
                description = "History description 2"
            ),
            ComposePagingListItem(
                id = "h3",
                title = "History Item 3",
                category = "History",
                description = "History description 3"
            ),
            ComposePagingListItem(
                id = "h4",
                title = "History Item 4",
                category = "History",
                description = "History description 4"
            ),
            ComposePagingListItem(
                id = "h5",
                title = "History Item 5",
                category = "History",
                description = "History description 5"
            ),
            ComposePagingListItem(
                id = "h6",
                title = "History Item 6",
                category = "History",
                description = "History description 6"
            ),
            ComposePagingListItem(
                id = "h7",
                title = "History Item 7",
                category = "History",
                description = "History description 7"
            ),
            ComposePagingListItem(
                id = "h8",
                title = "History Item 8",
                category = "History",
                description = "History description 8"
            ),
            ComposePagingListItem(
                id = "h9",
                title = "History Item 9",
                category = "History",
                description = "History description 9"
            ),
            ComposePagingListItem(
                id = "h10",
                title = "History Item 10",
                category = "History",
                description = "History description 10"
            ),
            ComposePagingListItem(
                id = "h11",
                title = "History Item 11",
                category = "History",
                description = "History description 11"
            ),
            ComposePagingListItem(
                id = "h12",
                title = "History Item 12",
                category = "History",
                description = "History description 12"
            ),
        )

        return Result.success(testData)
    }

    override fun refreshData() {
        TODO("Not yet implemented")
    }


    /*
    suspend fun getFutureItems(page:Int, pageSize:Int): Result<List<ComposePagingListItem>> {
        Log.i("TAG55","get items, page: $page - page size: $pageSize")
        delay(2000L)
        val startingIndex = page * pageSize
        return if (startingIndex + pageSize <= dummyRemoteDataSource.size) {
            Result.success(dummyRemoteDataSource.slice(startingIndex until startingIndex + pageSize))
        } else Result.success(emptyList())
    }

    suspend fun getHistoryItems(page:Int, pageSize:Int): Result<List<ComposePagingListItem>> {
        Log.i("TAG55","get items, page: $page - page size: $pageSize")
        delay(2000L)
        val testData = listOf(
            ComposePagingListItem(
                title = "History Item 1",
                description = "History description 1"
            ),
            ComposePagingListItem(
                title = "History Item 2",
                description = "History description 2"
            ),
            ComposePagingListItem(
                title = "History Item 3",
                description = "History description 3"
            ),
            ComposePagingListItem(
                title = "History Item 4",
                description = "History description 4"
            ),
            ComposePagingListItem(
                title = "History Item 5",
                description = "History description 5"
            ),
            ComposePagingListItem(
                title = "History Item 6",
                description = "History description 6"
            ),
            ComposePagingListItem(
                title = "History Item 7",
                description = "History description 7"
            ),
            ComposePagingListItem(
                title = "History Item 8",
                description = "History description 8"
            ),
            ComposePagingListItem(
                title = "History Item 9",
                description = "History description 9"
            ),
            ComposePagingListItem(
                title = "History Item 10",
                description = "History description 10"
            ),
            ComposePagingListItem(
                title = "History Item 11",
                description = "History description 11"
            ),
            ComposePagingListItem(
                title = "History Item 12",
                description = "History description 12"
            ),
        )

        return Result.success(testData)

//        val startingIndex = page * pageSize
//        return if (startingIndex + pageSize <= dummyRemoteDataSource.size) {
//            Result.success(dummyRemoteDataSource.slice(startingIndex until startingIndex + pageSize))
//        } else Result.success(emptyList())
    }
    */
}