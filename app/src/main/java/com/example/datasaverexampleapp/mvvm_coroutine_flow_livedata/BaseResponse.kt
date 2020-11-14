package com.example.datasaverexampleapp.mvvm_coroutine_flow_livedata

abstract class BaseResponse {

    var errorMessage:ErrorModel? = null

    fun requestFailed():Boolean
    {
        return errorMessage != null
    }
}