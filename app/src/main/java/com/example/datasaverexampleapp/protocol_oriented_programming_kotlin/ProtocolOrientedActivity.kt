package com.example.datasaverexampleapp.protocol_oriented_programming_kotlin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.room_db.DatabaseAccessor
import com.example.datasaverexampleapp.room_db.UserEntity

class ProtocolOrientedActivity : AppCompatActivity() {

    private lateinit var protocolViewModel: ProtocolOrientedViewModel
    private lateinit var protocolViewModel2: ProtocolOrientedViewModel2

    init{
        Log.i("TAG","Init non static")
    }

    companion object {

        init {
            Log.i("TAG","Init static")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_protocol_oriented)
        protocolViewModel = ViewModelProvider(this).get(ProtocolOrientedViewModel::class.java)
        protocolViewModel2 = ViewModelProvider(this).get(ProtocolOrientedViewModel2::class.java)

        protocolViewModel.getAllUsers().observe(this) {
            Log.i("TAG", "Protocol view model 1 result: ${it}")
        }

        protocolViewModel2.getAllUsers().observe(this) {
            Log.i("TAG", "Protocol view model 2 result: ${it}")
        }

        DatabaseAccessor.insertUser(UserEntity(name = "FirstnameTest 1", lastname = "LastnameTest 2", age = 23))
    }
}