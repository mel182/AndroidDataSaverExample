package com.example.datasaverexampleapp.room_db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase()
{
    abstract fun userDao():UserDAO
}