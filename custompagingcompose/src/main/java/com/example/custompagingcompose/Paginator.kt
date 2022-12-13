package com.example.custompagingcompose

interface Paginator<Key,Item> {
    suspend fun loadNextItems()
    suspend fun loadHistoryItems()
    fun reset()
}