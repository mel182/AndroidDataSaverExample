package com.example.customhttpclienttestapp.retrofit_3

abstract class BaseResponse {

    var errorMessage:ErrorResponse? = null

    fun requestFailed():Boolean = errorMessage != null

}