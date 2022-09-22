package com.example.datasaverexampleapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay

class PagingItemSource2(val onCompleted: () -> Unit) : PagingSource<Long, PagingItem>()
{
    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, PagingItem> {

        return try {

            val prevPage:Long = params.key ?: 0L

            delay(4000L)

            val result = PagingDemoRepository.getData(prevPage)

            val nextPage = if (result.isEmpty()) null else result.last().data

            onCompleted()
            // With this setting it is garantee that the page source will stop loading when data are in
            LoadResult.Page(data = result,
                prevKey = if (prevPage == 0L) null else nextPage ,
                nextKey = nextPage
            )

        } catch (e: Exception) {
            onCompleted()
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Long, PagingItem>): Long {
        // Called when refreshing list
        return 0L
    }

    override val keyReuseSupported: Boolean = true
}