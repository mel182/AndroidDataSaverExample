package com.example.datasaverexampleapp.mvvm_coroutine_flow_livedata

import com.google.gson.annotations.SerializedName

data class DemoModel (
    @SerializedName("userId") val userID:Int = 0,
    @SerializedName("id") val id:Int = 0,
    @SerializedName("title") val title:String = "",
    @SerializedName("completed") val completed:Boolean = false): BaseResponse()

