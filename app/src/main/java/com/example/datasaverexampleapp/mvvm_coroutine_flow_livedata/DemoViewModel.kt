package com.example.datasaverexampleapp.mvvm_coroutine_flow_livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DemoViewModel:ViewModel() {

    private var demoResponse:MutableLiveData<DemoModel> = MutableLiveData()
    private var demoAsyncResponse:MutableLiveData<DemoModelSummary> = MutableLiveData()

    fun getDemo(amount:Int): LiveData<DemoModel>
    {
        DemoRepository.getDemo(amount = amount,object : RepositoryCallback<DemoModel>{

            override fun onResponse(response: DemoModel) {
                demoResponse.value = response
            }

            override fun onFailed(error: DemoModel) {
                demoResponse.value = error
            }
        })

        return demoResponse
    }

    fun getAsyncDemo(amount:Int): LiveData<DemoModelSummary>
    {
        CoroutineScope(Dispatchers.Main).launch{
            DemoRepository.getDemoAsync().let {
                demoAsyncResponse.value = it.first()
            }
        }

        return demoAsyncResponse
    }

    suspend fun getAsyncDemo(): LiveData<DemoModelSummary> = DemoRepository.getDemoAsync().asLiveData()
}