package com.jetpackcompose.localsearch

data class TodoListState(
    val todos: List<Todo> = emptyList(),
    val searchQuery: String = ""
)
