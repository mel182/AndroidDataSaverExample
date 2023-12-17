package com.jetpackcompose.paginglist.repository

import com.jetpackcompose.paginglist.models.PagingItem

object PagingDemoRepository {

    suspend fun getData(page:Long?) : List<PagingItem> {

        return when(page)
        {
            0L -> demoList1

            1646760146L -> demoList2

            else -> ArrayList()
        }
    }

    private val demoList1 = arrayListOf(
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
        PagingItem(id = 23,name = "Item 23", age = 23, data = 1646760146),
    )

    private val demoList2 = arrayListOf(
        PagingItem(id = 24,name = "Item 24", age = 24),
        PagingItem(id = 25,name = "Item 25", age = 25),
        PagingItem(id = 26,name = "Item 26", age = 26),
        PagingItem(id = 27,name = "Item 27", age = 27),
        PagingItem(id = 28,name = "Item 28", age = 28),
        PagingItem(id = 29,name = "Item 29", age = 29),
        PagingItem(id = 30,name = "Item 30", age = 30),
        PagingItem(id = 31,name = "Item 31", age = 31),
        PagingItem(id = 32,name = "Item 32", age = 32),
        PagingItem(id = 33,name = "Item 33", age = 33),
        PagingItem(id = 34,name = "Item 34", age = 34),
        PagingItem(id = 35,name = "Item 35", age = 35),
        PagingItem(id = 36,name = "Item 36", age = 36),
        PagingItem(id = 37,name = "Item 37", age = 37),
        PagingItem(id = 38,name = "Item 38", age = 38),
        PagingItem(id = 39,name = "Item 39", age = 39),
        PagingItem(id = 40,name = "Item 40", age = 40),
        PagingItem(id = 41,name = "Item 41", age = 41),
        PagingItem(id = 42,name = "Item 42", age = 42),
        PagingItem(id = 43,name = "Item 43", age = 43),
        PagingItem(id = 44,name = "Item 44", age = 44),
        PagingItem(id = 45,name = "Item 45", age = 45),
        PagingItem(id = 46,name = "Item 46", age = 46, data = 1646842946),
    )
}