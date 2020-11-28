package com.example.datasaverexampleapp.room_db

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class RoomDBViewModel : ViewModel()
{
//    private var databaseResponse: MutableLiveData<List<UserEntity>> = MutableLiveData()

    fun getAllUsers(): LiveData<List<UserEntity>>
    {
        return DatabaseAccessor.userList


//        CoroutineScope(Dispatchers.IO).launch {
//
//            val result = DatabaseAccessor.dataAccessObject?.loadAllUsers()
//
//            withContext(Dispatchers.Main){
//                databaseResponse.value = result
//            }
//        }
//
//        return databaseResponse
    }

//    fun loadMore()
//    {
//
////        getAllUsers()
//
//        CoroutineScope(Dispatchers.IO).launch {
//
//
//
//            val result = DatabaseAccessor.dataAccessObject?.loadAllUsers()
//            Log.i("TAG","Load all users result: ${result}")
//
//            withContext(Dispatchers.Main) {
//                Log.i("TAG","Load all user live data value changed")
//                databaseResponse = MutableLiveData()
//                databaseResponse.value = result
//            }
//        }
//    }


}