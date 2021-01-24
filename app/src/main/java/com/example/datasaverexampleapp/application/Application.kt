package com.example.datasaverexampleapp.application

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.example.datasaverexampleapp.room_db.DatabaseAccessor

class AppContext : MultiDexApplication()
{
    companion object {
        lateinit var appContext:Context
        private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
        DatabaseAccessor.init(appContext)
    }
}