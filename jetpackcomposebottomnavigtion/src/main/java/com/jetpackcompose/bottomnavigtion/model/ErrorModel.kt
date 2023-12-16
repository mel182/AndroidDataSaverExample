package com.jetpackcompose.bottomnavigtion.model

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