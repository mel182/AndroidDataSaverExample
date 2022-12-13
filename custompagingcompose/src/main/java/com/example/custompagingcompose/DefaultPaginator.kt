package com.example.custompagingcompose

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onFutureRequest: suspend (nextKey: Key) -> Result<List<Item>>,
    private inline val onHistoryRequest: suspend (nextKey: Key) -> Result<List<Item>>,
    private inline val getNextKey: suspend (List<Item>) -> Key,
    private inline val getNextHistoryKey: suspend (List<Item>) -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onHistorySuccess: suspend (items: List<Item>, newHistoryKey: Key) -> Unit,
    private inline val onFutureSuccess: suspend (items: List<Item>, newKey: Key) -> Unit) : Paginator<Key,Item>
{
    private var currentFutureKey = initialKey
    private var currentHistoryKey = initialKey
    private var isMakingRequest = false

    override suspend fun loadNextItems() {
        if (isMakingRequest)
            return

        isMakingRequest = true
        onLoadUpdated(true)
        val result = onFutureRequest(currentFutureKey)
        isMakingRequest = false
        val items = result.getOrElse {
            onError(it)
            onLoadUpdated(false)
            return
        }
        currentFutureKey = getNextKey(items)
        onFutureSuccess(items, currentFutureKey)
        onLoadUpdated(false)
    }

    override suspend fun loadHistoryItems() {
        if (isMakingRequest)
            return

        isMakingRequest = true
        onLoadUpdated(true)
        val result = onHistoryRequest(currentHistoryKey)
        isMakingRequest = false
        val items = result.getOrElse {
            onError(it)
            onLoadUpdated(false)
            return
        }
        currentHistoryKey = getNextHistoryKey(items)
        onHistorySuccess(items, currentHistoryKey)
        onLoadUpdated(false)
    }

    override fun reset() {
        currentFutureKey = initialKey
        currentHistoryKey = initialKey
    }
}