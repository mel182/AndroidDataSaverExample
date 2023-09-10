package com.example.sharedatabetweencomposeview.using_stateful_dependencies.viewmodels

import androidx.lifecycle.ViewModel
import com.example.sharedatabetweencomposeview.using_stateful_dependencies.singleton.GlobalCounter

class Screen2ViewModel: ViewModel() {
    val count = GlobalCounter.count

    fun increment() {
        GlobalCounter.increment()
    }
}