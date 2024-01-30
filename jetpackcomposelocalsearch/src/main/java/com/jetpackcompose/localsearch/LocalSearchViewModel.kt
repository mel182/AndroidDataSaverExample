package com.jetpackcompose.localsearch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class LocalSearchViewModel(private val todoSearchManager: TodoSearchManager) : ViewModel() {

    var state by mutableStateOf(TodoListState())

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            todoSearchManager.apply {
                init()
                val todos = (1..100).map {
                    Todo(
                        namespaces = "my_todos",
                        id = UUID.randomUUID().toString(),
                        score = 1,
                        title = "Todo $it",
                        text = "Description $it",
                        isDone = Random.nextBoolean()
                    )
                }
                putTodos(todos)
                state = state.copy(todos = todos)
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        state = state.copy(searchQuery = query)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500.milliseconds)
            val todos = todoSearchManager.searchTodos(query)
            state = state.copy(todos = todos)
        }
    }

    fun onDoneChanged(todo: Todo, isDone: Boolean) {
        viewModelScope.launch {
            val doneResult = todoSearchManager.updateTodos(
                listOf(todo.copy(isDone = isDone))
            )
            state = state.copy(
                todos = state.todos.map {
                    if (it.id == todo.id) {
                        it.copy(isDone = isDone)
                    } else it
                }
            )
        }
    }

    override fun onCleared() {
        todoSearchManager.closeSession()
        super.onCleared()
    }
}