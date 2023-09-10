package com.example.sharedatabetweencomposeview.using_stateful_dependencies.singleton

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object GlobalCounter {

    private val _count = MutableStateFlow(0)
    val count = _count.asStateFlow()

    fun increment() {
        _count.value++
    }
}