package com.example.datasaverexampleapp.mvvm_coroutine_flow_livedata

interface RepositoryCallback<T> {
    fun onResponse(response:T)
    fun onFailed(error:T)
}