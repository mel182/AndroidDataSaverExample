package com.example.datasaverexampleapp.compose.list_with_paging

interface Paginator<Key,Item> {
    suspend fun loadNextItems()
    fun reset()
}