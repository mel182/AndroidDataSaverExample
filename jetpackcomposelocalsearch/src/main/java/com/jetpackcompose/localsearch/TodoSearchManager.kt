package com.jetpackcompose.localsearch

import android.content.Context
import androidx.appsearch.app.AppSearchSession
import androidx.appsearch.app.PutDocumentsRequest
import androidx.appsearch.app.SearchSpec
import androidx.appsearch.app.SetSchemaRequest
import androidx.appsearch.localstorage.LocalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TodoSearchManager(private val appContext: Context) {

    private var session: AppSearchSession? = null

    suspend fun init() {
        withContext(Dispatchers.IO) {
            val sessionFuture = LocalStorage.createSearchSessionAsync(
                LocalStorage.SearchContext.Builder(
                    appContext,
                    "todo"
                ).build()
            )
            val setSchemaRequest = SetSchemaRequest.Builder()
                .addDocumentClasses(Todo::class.java)
                .build()

            sessionFuture.get()?.apply {
                session = this
                setSchemaAsync(setSchemaRequest)
            }
        }
    }

    suspend fun putTodos(todos: List<Todo>): Boolean {

        return withContext(Dispatchers.IO) {

            val rawList = ArrayList(todos)
            val refineList = ArrayList<Todo>()

            rawList.forEach { itemFound ->

                val result = searchTodos(query = itemFound.title)
                if (result.isEmpty())
                    refineList.add(itemFound)
            }

            if (refineList.isEmpty())
            {
                true
            } else {
                session?.putAsync(
                    PutDocumentsRequest.Builder()
                        .addDocuments(refineList)
                        .build()
                )?.get()?.isSuccess == true
            }
        }
    }

    suspend fun updateTodos(todos: List<Todo>): Boolean {

        return withContext(Dispatchers.IO) {

            session?.putAsync(
                PutDocumentsRequest.Builder()
                    .addDocuments(todos)
                    .build()
            )?.get()?.isSuccess == true
        }
    }

    suspend fun searchTodos(query:String): List<Todo> {
        return withContext(Dispatchers.IO) {
            val searchSpec = SearchSpec.Builder()
                .setSnippetCount(10)
                .addFilterNamespaces("my_todos")
                .setRankingStrategy(SearchSpec.RANKING_STRATEGY_USAGE_COUNT)
                .build()
            val result = session?.search(query.ifBlank { "Todo" },searchSpec) ?: return@withContext emptyList()

            val page = result.nextPageAsync.get()
            page.mapNotNull {
                if (it.genericDocument.schemaType == Todo::class.java.simpleName) {
                    it.getDocument(Todo::class.java)
                } else null
            }
        }
    }

    fun closeSession() {
        session?.close()
        session = null
    }
}