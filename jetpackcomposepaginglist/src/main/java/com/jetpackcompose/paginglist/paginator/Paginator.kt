package com.jetpackcompose.paginglist.paginator

interface Paginator<Key,Item> {
    suspend fun loadNextItems()
    fun reset()
}