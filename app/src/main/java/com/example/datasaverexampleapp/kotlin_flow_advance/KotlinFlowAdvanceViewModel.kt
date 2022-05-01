package com.example.datasaverexampleapp.kotlin_flow_advance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class KotlinFlowAdvanceViewModel : ViewModel() {

    val countdownFlow = flow<Int> {
        val startingValue = 10
        var currentValue = startingValue
        emit(startingValue)
        while (currentValue > 0) {
            delay(1000)
            currentValue--
            emit(currentValue)
        }
    }

    fun collectFlow() : Flow<Int> {

        return flow {
            viewModelScope.launch {
                countdownFlow.collect {
                    emit(it)
                }
            }
        }
    }

}