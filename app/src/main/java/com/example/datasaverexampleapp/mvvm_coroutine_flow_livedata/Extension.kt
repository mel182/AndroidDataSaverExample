package com.example.datasaverexampleapp.mvvm_coroutine_flow_livedata

import com.google.gson.Gson
import retrofit2.HttpException

fun Exception?.parseError():ErrorModel
{
    return this?.let { exception ->

        var errorResponse = ErrorModel()

        when(exception)
        {
            is HttpException -> {
                exception.response()?.errorBody()?.let {

                    try{
                        errorResponse = Gson().fromJson(it.charStream(),ErrorModel::class.java)
                        errorResponse.statusCode = exception.code()
                    }catch (e:Exception)
                    {
                        errorResponse.error = e.message?:""
                    }
                }?: kotlin.run {
                    errorResponse.error = exception.message()
                }
            }

            else -> {
                errorResponse.error = exception.message?:""
            }
        }

        errorResponse
    }?:ErrorModel()
}