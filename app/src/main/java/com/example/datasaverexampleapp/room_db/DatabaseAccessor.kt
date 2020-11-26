package com.example.datasaverexampleapp.room_db

import android.content.Context
import androidx.room.Room
import com.example.datasaverexampleapp.application.AppContext

object DatabaseAccessor {

    private const val USER_DB_NAME = "user_db"
    private lateinit var userDatabase:UserDatabase
    var dataAccessObject:UserDAO? = null

    fun init(context:Context)
    {
        userDatabase = Room.databaseBuilder(context, UserDatabase::class.java,
            USER_DB_NAME).build()
        dataAccessObject = userDatabase.userDao()
    }
}