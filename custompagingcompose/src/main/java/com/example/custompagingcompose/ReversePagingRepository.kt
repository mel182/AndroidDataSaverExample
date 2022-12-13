package com.example.custompagingcompose

abstract class ReversePagingRepository<Key,Item>(val pageSize:Int) {
    abstract suspend fun getFutureItems(key:Key): Result<List<Item>>
    abstract suspend fun getHistoryItems(key:Key): Result<List<Item>>
    abstract fun refreshData()
}