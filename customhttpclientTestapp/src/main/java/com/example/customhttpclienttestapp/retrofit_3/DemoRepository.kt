package com.example.customhttpclienttestapp.retrofit_3

import android.util.Log
import com.custom.http.client.retrofit_3.Retrofit3Builder
import com.google.gson.Gson
import com.retrofit3.retrofit_converter.gson.GsonConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient

object DemoRepository {

    private var demoEndpoint:TestDemoEndpoints

    init {

        val client = OkHttpClient.Builder()
        client.addInterceptor(Interceptor { chain ->
            val request = chain.request().newBuilder().addHeader("X-Supports","test1").build()
            chain.proceed(request)
        })

        val retrofit = Retrofit3Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(client.build())
            .addConverterFactory(GsonConverterFactory(Gson()))
            .build()

        demoEndpoint = retrofit.create(TestDemoEndpoints::class.java)
    }

    suspend fun getDemoData(amount:Int): DemoModel {

        var response = DemoModel()
        try {
            val rawResponse = demoEndpoint.getDemoDataAsync(amount)
            Log.i("TAG55","raw response suspend repo: ${rawResponse}")
            response = rawResponse
        }catch (requestError:Exception) {
            Log.i("TAG55","Request error: ${requestError.message}")
            response.errorMessage = ErrorResponse("Request error: ${requestError.message}",500)
        }

        return response
    }

}