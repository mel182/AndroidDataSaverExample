package com.example.datasaverexampleapp.room_db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity (@PrimaryKey(autoGenerate = true) val id:Int = 0, val name:String, val lastname:String, val age:Int)