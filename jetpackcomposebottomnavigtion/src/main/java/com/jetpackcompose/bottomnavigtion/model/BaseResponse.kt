package com.jetpackcompose.bottomnavigtion.model

abstract class BaseResponse {

    var errorMessage:ErrorModel? = null

    fun requestFailed():Boolean
    {
        return errorMessage != null
    }
}