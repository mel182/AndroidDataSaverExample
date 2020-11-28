package com.example.datasaverexampleapp.room_db

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class RoomDBViewModel : ViewModel()
{
    fun getAllUsers(): LiveData<List<UserEntity>> {
        return DatabaseAccessor.userList
    }
}