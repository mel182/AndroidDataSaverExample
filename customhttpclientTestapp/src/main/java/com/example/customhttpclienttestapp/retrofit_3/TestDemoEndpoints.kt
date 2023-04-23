package com.example.customhttpclienttestapp.retrofit_3

import com.custom.http.client.annotation.http.call_properties.Path
import com.custom.http.client.annotation.http.method.GET

interface TestDemoEndpoints {

    @GET("todos/{amount}")
    suspend fun getDemoDataAsync(@Path("amount") amount:Int): DemoModel

}