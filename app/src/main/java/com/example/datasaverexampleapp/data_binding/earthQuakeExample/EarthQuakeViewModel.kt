package com.example.datasaverexampleapp.data_binding.earthQuakeExample

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.datasaverexampleapp.earth_quake_example.model.Earthquake

class EarthQuakeViewModel : ViewModel()
{
    fun retrieveData(): LiveData<Earthquake>
    {
        return EarthQuakeRepository.currentData
    }
}