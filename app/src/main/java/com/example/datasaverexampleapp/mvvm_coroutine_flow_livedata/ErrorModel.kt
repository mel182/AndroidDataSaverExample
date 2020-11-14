package com.example.datasaverexampleapp.mvvm_coroutine_flow_livedata

import com.google.gson.annotations.SerializedName

class ErrorModel(@SerializedName("message") val message:String = "", )
{
    var statusCode:Int = 0
    var error:String = ""
    var errorMessage:ErrorModel? = null

    fun requestFailed():Boolean
    {
        return error != "" || errorMessage != null
    }
}