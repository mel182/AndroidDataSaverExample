package com.example.datasaverexampleapp.room_db

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_room_d_b.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class RoomDBActivity : AppCompatActivity() {

    private var dataListAdapter = DatabaseListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_d_b)

        user_recycler_view?.apply {
            layoutManager = LinearLayoutManager(this@RoomDBActivity)
            adapter = dataListAdapter
        }


        DatabaseAccessor.dataAccessObject?.apply {

            CoroutineScope(Dispatchers.IO).launch{

                insertUser(UserEntity(name = "Firstname 1", lastname = "Lastname 1", age = 23))
                insertUser(UserEntity(name = "Firstname 2",lastname = "Lastname 2",age = 24))

                val allUsers = loadAllUsers()

                CoroutineScope(Dispatchers.Main).launch {
                    allUsers.forEach {
                        Log.i("TAG","User entity: id ${it.id} name: ${it.name} last name: ${it.lastname} age: ${it.age}")
                        dataListAdapter.add(it)
                    }
                }
            }
        }
    }
}