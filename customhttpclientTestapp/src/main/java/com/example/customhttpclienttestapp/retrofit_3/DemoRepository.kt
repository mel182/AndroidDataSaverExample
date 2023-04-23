package com.example.customhttpclienttestapp.retrofit_3

import com.custom.http.client.Retrofit3
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

object DemoRepository {

    private var demoEndpoint:TestDemoEndpoints

    init {

        val client = OkHttpClient.Builder()
        client.addInterceptor(Interceptor { chain ->
            val request = chain.request().newBuilder().addHeader("X-Supports","test1").build()
            chain.proceed(request)
        })

        val retrofit = Retrofit3.Builder()



    }


}