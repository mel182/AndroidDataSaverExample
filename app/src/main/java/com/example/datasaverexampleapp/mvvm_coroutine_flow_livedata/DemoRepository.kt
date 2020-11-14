package com.example.datasaverexampleapp.mvvm_coroutine_flow_livedata

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DemoRepository {

   private var demoEndpoint: DemoEndpoint

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create()).build()

//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://bf662bad319c.ngrok.io")
//            .addConverterFactory(GsonConverterFactory.create()).build()

        demoEndpoint = retrofit.create(DemoEndpoint::class.java)
    }

    suspend fun getDemoAsync(): Flow<DemoModelSummary>
    {
        return flow {

            coroutineScope {

                val requestModelResponse1 = async { getSingleDemoAsync(1) }
                val requestModelResponse2 = async { getSingleDemoAsync(2) }


//            val model1 = async {
//
//                var modelResponse = DemoModel()
//
//                try {
//                    val rawresponse = demoEndpoint.getAsyncDemo(1)
//                    modelResponse = rawresponse
//                }catch (requestError:Exception)
//                {
//                    modelResponse.errorMessage = requestError.parseError()
//                }
//
//                modelResponse
//            }
//
//            // -- Model 2
//            val model2 = async {
//
//                var modelResponse = DemoModel()
//
//                try {
//                    val rawresponse = demoEndpoint.getAsyncDemo(2)
//                    modelResponse = rawresponse
//                }catch (requestError:Exception)
//                {
//                    modelResponse.errorMessage = requestError.parseError()
//                }
//
//                modelResponse
//            }

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

    fun getDemo(amount:Int,repositoryCallback:RepositoryCallback<DemoModel>)
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