package com.example.customhttpclienttestapp.retrofit_3

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class DemoViewModel: ViewModel() {

    private val _data_list = MutableStateFlow(DemoModel())
    val dataList: StateFlow<DemoModel>
        get() = _data_list.asStateFlow()

    fun load_data() {
        CoroutineScope(Dispatchers.IO).launch {
            async { loadData() }.await()
        }
    }

    suspend fun loadData() {
        Log.i("TAG55","load data suspended")
        val rawResponse = flow {
            coroutineScope {
                val rawResponse = async { DemoRepository.getDemoData(1) }
                emit(rawResponse.await())
            }
        }.first()

        Log.i("TAG55","raw response: ${rawResponse}")
        _data_list.value = rawResponse
    }

}