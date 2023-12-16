@file:Suppress("UNUSED_PARAMETER")

package com.jetpackcompose.bottomnavigtion.repository

import com.jetpackcompose.bottomnavigtion.interfaces.DemoEndpoint
import com.jetpackcompose.bottomnavigtion.interfaces.RepositoryCallback
import com.jetpackcompose.bottomnavigtion.model.DemoModel
import com.jetpackcompose.bottomnavigtion.extensions.parseError
import com.jetpackcompose.bottomnavigtion.viewmodels.DemoModelSummary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DemoRepository {

   private var demoEndpoint: DemoEndpoint

    init {

        val client = OkHttpClient.Builder()
        client.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {

                val request = chain.request().newBuilder().addHeader("X-Supports","test1").build()
                return chain.proceed(request)
            }

        })

        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create()).build()

//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://bf662bad319c.ngrok.io")
//            .addConverterFactory(GsonConverterFactory.create()).build()

        /*
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

httpClient.addInterceptor(new Interceptor() {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder().addHeader("parameter", "value").build();
        return chain.proceed(request);
    }
});
Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).client(httpClient.build()).build();
        */


        demoEndpoint = retrofit.create(DemoEndpoint::class.java)
    }

    suspend fun getDemoAsync(): Flow<DemoModelSummary>
    {
        return flow {
            coroutineScope {
                val requestModelResponse1 = async { getSingleDemoAsync(1) }
                val requestModelResponse2 = async { getSingleDemoAsync(2) }
                emit(DemoModelSummary(model1 = requestModelResponse1.await(),model2 = requestModelResponse2.await()))
            }
        }
    }

    private suspend fun getSingleDemoAsync(amount:Int): DemoModel
    {
        var modelResponse = DemoModel()

        try {
            val rawResponse = demoEndpoint.getAsyncDemo(amount)
            modelResponse = rawResponse
        }catch (requestError:Exception)
        {
            modelResponse.errorMessage = requestError.parseError()
        }

        return modelResponse
    }

    fun getDemo(amount:Int,repositoryCallback: RepositoryCallback<DemoModel>)
    {
        CoroutineScope(Dispatchers.IO).launch {

            try {
                val apiCall = async { demoEndpoint.getDemo2() }
                val response = apiCall.await()

                withContext(Dispatchers.Main){
                    repositoryCallback.onResponse(response)
                }
            }catch (requestError:java.lang.Exception)
            {
                val errorResponse = DemoModel()
                errorResponse.errorMessage = requestError.parseError()
                withContext(Dispatchers.Main){
                    repositoryCallback.onFailed(errorResponse)
                }
            }
        }
    }
}