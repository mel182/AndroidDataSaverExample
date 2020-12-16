package com.example.datasaverexampleapp.protocol_oriented_programming_kotlin

import androidx.lifecycle.LiveData
import com.example.datasaverexampleapp.room_db.DatabaseAccessor
import com.example.datasaverexampleapp.room_db.UserEntity

interface GetAllUsers {

    fun getAllUsers(): LiveData<List<UserEntity>> {
        return DatabaseAccessor.userList
    }
}