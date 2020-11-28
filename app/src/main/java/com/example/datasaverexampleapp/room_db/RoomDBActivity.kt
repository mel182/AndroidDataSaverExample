package com.example.datasaverexampleapp.room_db

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_room_d_b.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomDBActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_d_b)

        container1?.apply {
            val content = DbObserverFragment().setTitle("Container 1")
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(this.id,content)
            transaction.commit()
        }

        container2?.apply {
            val content = DbObserverFragment().setTitle("Container 2")
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(this.id,content)
            transaction.commit()
        }

        CoroutineScope(Dispatchers.IO).launch {
            DatabaseAccessor.dataAccessObject?.deleteAllUsers()
        }

        startLoadingData()
    }

    private var userEntityID = 0

    private fun startLoadingData()
    {
        if (userEntityID != 6)
        {
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