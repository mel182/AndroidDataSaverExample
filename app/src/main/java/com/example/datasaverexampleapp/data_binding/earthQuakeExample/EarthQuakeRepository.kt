package com.example.datasaverexampleapp.data_binding.earthQuakeExample

import androidx.lifecycle.MutableLiveData
import com.example.datasaverexampleapp.earth_quake_example.model.Earthquake
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*

object EarthQuakeRepository {

    private val earthQuakeList = listOf(
        Earthquake("0",Date(),"San Jose",null, 7.3,null),
        Earthquake("1",Date(),"LA",null, 6.5,null),
        Earthquake("2",Date(),"LA12",null, 7.0,null)
    )

    val currentData:MutableLiveData<Earthquake> = MutableLiveData()

    init {
        currentData.value = earthQuakeList[0]
    }

    fun loadData(index:Int)
    {
        if(index <= earthQuakeList.size - 1)
        {
            currentData.value = earthQuakeList[index]
        }
    }
}