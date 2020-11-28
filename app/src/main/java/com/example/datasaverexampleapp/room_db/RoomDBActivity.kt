package com.example.datasaverexampleapp.room_db

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_room_d_b.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomDBActivity : AppCompatActivity() {

    private var dataListAdapter = DatabaseListAdapter()
    private lateinit var roomDBViewModel: RoomDBViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_d_b)

        roomDBViewModel = ViewModelProviders.of(this).get(RoomDBViewModel::class.java)

        user_recycler_view?.apply {
            layoutManager = LinearLayoutManager(this@RoomDBActivity)
            adapter = dataListAdapter
        }

        CoroutineScope(Dispatchers.IO).launch {
            DatabaseAccessor.dataAccessObject?.deleteAllUsers()
        }

        roomDBViewModel.getAllUsers().observe(this, {
            dataListAdapter.addUsers(it)
        })
        startLoadingData()
    }

    private var userEntityID = 0

    private fun startLoadingData()
    {

        if (userEntityID == 6)
        {
            dataListAdapter.remove()
        } else {
            userEntityID++

            object : CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i("TAG","seconds remaining: " + millisUntilFinished / 1000)
                }

                override fun onFinish() {
                    DatabaseAccessor.insertUser(UserEntity(name = "FirstnameTest ${userEntityID}", lastname = "LastnameTest ${userEntityID}", age = 23))
                    startLoadingData()
                }
            }.start()
        }
    }
}