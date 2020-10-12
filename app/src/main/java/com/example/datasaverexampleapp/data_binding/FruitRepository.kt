package com.example.datasaverexampleapp.data_binding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

object FruitRepository
{

    private val fruitNames: List<String> = listOf(
        "Apple","Banana","Orange","Kiwi","Grapes","Fig",
        "Pear","Strawberry","Gooseberry","Rasberry"
    )

    private val _currentRandomFruitName = MutableLiveData<String>()
    val currentRandomFruitName: LiveData<String>
    get() = _currentRandomFruitName

    init {
        _currentRandomFruitName.value = fruitNames.first()
    }

    fun getRandomFruitNames():String {
        val random = Random()
        return fruitNames[random.nextInt(fruitNames.size - 1)]
    }

    fun changeCurrentRandomFruitName()
    {
        _currentRandomFruitName.value = getRandomFruitNames()
    }

}