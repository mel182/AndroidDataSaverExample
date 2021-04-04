package com.example.datasaverexampleapp.data_binding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel()
{
    val currentRandomFruitName: LiveData<String>
    get() = FruitRepository.currentRandomFruitName

    fun onChangeRandomFruitClick() = FruitRepository.changeCurrentRandomFruitName()

    val editTextContent = MutableLiveData<String>()

    private val _displayedEditTextContent = MutableLiveData<String>()
    val displayedEditTextContent: LiveData<String>
    get() = _displayedEditTextContent

    fun onDisplayEditTextContentClick() {
        _displayedEditTextContent.value = editTextContent.value
    }

    fun onSelectRandomEditTextFruit()
    {
        editTextContent.value = FruitRepository.getRandomFruitNames()
    }
}