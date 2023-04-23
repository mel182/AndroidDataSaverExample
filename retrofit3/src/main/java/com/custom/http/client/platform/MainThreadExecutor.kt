package com.custom.http.client.platform

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

class MainThreadExecutor : Executor {

    companion object {
        val INSTANCE: Executor = MainThreadExecutor()
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun execute(runnable: Runnable?) {
        runnable?.let {
            handler.post(runnable)
        }
    }
}