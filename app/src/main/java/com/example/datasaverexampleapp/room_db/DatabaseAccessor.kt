package com.example.datasaverexampleapp.room_db

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.datasaverexampleapp.application.AppContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DatabaseAccessor {

    private const val USER_DB_NAME = "user_db"
    private lateinit var userDatabase:UserDatabase
    var dataAccessObject:UserDAO? = null

    private val _userList = MutableLiveData<List<UserEntity>>()
    val userList: LiveData<List<UserEntity>>
        get() = _userList

    fun init(context:Context)
    {
        userDatabase = Room.databaseBuilder(context, UserDatabase::class.java,
            USER_DB_NAME).build()
        dataAccessObject = userDatabase.userDao()
    }

    fun insertUser(newUser:UserEntity)
    {
       CoroutineScope(Dispatchers.IO).launch {

           dataAccessObject?.insertUser(newUser)

           val result = dataAccessObject?.loadAllUsers()

           result?.let { updatedList ->

               withContext(Dispatchers.Main){

                   Log.i("TAG","Update user list in database accessor")
                   _userList.value = updatedList

               }
           }
       }
    }

}