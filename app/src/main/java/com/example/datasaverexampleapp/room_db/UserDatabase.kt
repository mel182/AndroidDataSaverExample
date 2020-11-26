package com.example.datasaverexampleapp.room_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase()
{
    abstract fun userDao():UserDAO

//    companion object {
//
//        private var instance:UserDatabase? = null
//
//        @Synchronized
//        fun getInstance(context:Context):UserDatabase {
//
//            if (instance == null)
//                instance = Room.databaseBuilder(context,UserDatabase::class.java,"user_db")
//                    .fallbackToDestructiveMigration()
//                    .addCallback(roomCallback)
//                    .build()
//
//
//                return instance!!
//        }
//
//        private val roomCallback = object : Callback(){
//
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//                populateDatabase(instance!!)
//            }
//        }
//
//
//        private fun populateDatabase(db:UserDatabase)
//        {
//            val userDAO = db.userDao()
//            userDAO.insertUser(UserEntity(name = "Name1",lastname = "lastname1",age = 23))
//            userDAO.insertUser(UserEntity(name = "Name2",lastname = "lastname2",age = 23))
//        }
//
//    }

}