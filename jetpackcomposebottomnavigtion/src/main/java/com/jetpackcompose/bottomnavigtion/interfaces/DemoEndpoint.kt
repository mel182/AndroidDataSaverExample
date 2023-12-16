package com.jetpackcompose.bottomnavigtion.interfaces

import com.jetpackcompose.bottomnavigtion.model.DemoModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DemoEndpoint {

    @GET("/todos/{amount}")
    fun getDemo(@Path("amount") amount:Int): Call<DemoModel>

    @GET("/todos/{amount}")
    suspend fun getAsyncDemo(@Path("amount") amount:Int): DemoModel

    @GET("/company/all")
    suspend fun getDemo2(): DemoModel

}