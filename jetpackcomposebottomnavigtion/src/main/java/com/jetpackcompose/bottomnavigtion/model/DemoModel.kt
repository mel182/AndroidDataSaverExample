package com.jetpackcompose.bottomnavigtion.model

import com.google.gson.annotations.SerializedName

data class DemoModel (
    @SerializedName("userId") val userID:Int = 0,
    @SerializedName("id") val id:Int = 0,
    @SerializedName("title") val title:String = "",
    @SerializedName("completed") val completed:Boolean = false): BaseResponse()

