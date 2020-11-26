package com.example.datasaverexampleapp.application

import android.app.Application
import android.content.Context
import com.example.datasaverexampleapp.room_db.DatabaseAccessor

class AppContext : Application()
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


//    init {
//        instance = this
//    }

//    companion object {
//
//        private lateinit var instance:AppContext
//
//        fun getGlobalContext():Context
//        {
//            if(!this::instance.isInitialized)
//                instance = AppContext()
//
//
////            if (instance == null)
////            {
////                instance = AppContext()
////            }
//
//            return instance.applicationContext
//        }
//    }




    /*

    private static Singleton instance = null;

    private Singleton() {

    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    */



}


/*

   class MainApplication : Application() {

init {
instance = this
}

companion object {
private var instance: MainApplication? = null

fun applicationContext() : Context {
return instance!!.applicationContext
}
}

override fun onCreate() {
super.onCreate()
// initialize for any

// Use ApplicationContext.
// example: SharedPreferences etc...
val context: Context = MainApplication.applicationContext()
}
}

   */