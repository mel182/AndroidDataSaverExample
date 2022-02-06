package com.example.datasaverexampleapp.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import java.lang.Exception

class PagingItemSource : PagingSource<Int, PagingItem>()
{
    private var pagingInt = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PagingItem> {

        Log.i("TAG64","load data params: ${params.key}")
        Log.i("TAG64","paging int: ${pagingInt}")

        return try {

            val nextPage = params.key ?: 1
            pagingInt++
            LoadResult.Page(data = demoList,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = pagingInt
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PagingItem>): Int {
        return pagingInt + 1
    }

    private val demoList = arrayListOf(
        PagingItem(id = 1,name = "Item 1", age = 1),
        PagingItem(id = 2,name = "Item 2", age = 2),
        PagingItem(id = 3,name = "Item 3", age = 3),
        PagingItem(id = 4,name = "Item 4", age = 4),
        PagingItem(id = 5,name = "Item 5", age = 5),
        PagingItem(id = 6,name = "Item 6", age = 6),
        PagingItem(id = 7,name = "Item 7", age = 7),
        PagingItem(id = 8,name = "Item 8", age = 8),
        PagingItem(id = 9,name = "Item 9", age = 9),
        PagingItem(id = 10,name = "Item 10", age = 10),
        PagingItem(id = 11,name = "Item 11", age = 11),
        PagingItem(id = 12,name = "Item 12", age = 12),
        PagingItem(id = 13,name = "Item 13", age = 13),
        PagingItem(id = 14,name = "Item 14", age = 14),
        PagingItem(id = 15,name = "Item 15", age = 15),
        PagingItem(id = 16,name = "Item 16", age = 16),
        PagingItem(id = 17,name = "Item 17", age = 17),
        PagingItem(id = 18,name = "Item 18", age = 18),
        PagingItem(id = 19,name = "Item 19", age = 19),
        PagingItem(id = 20,name = "Item 20", age = 20),
        PagingItem(id = 21,name = "Item 21", age = 21),
        PagingItem(id = 22,name = "Item 22", age = 22),
        PagingItem(id = 23,name = "Item 23", age = 23),
    )
}