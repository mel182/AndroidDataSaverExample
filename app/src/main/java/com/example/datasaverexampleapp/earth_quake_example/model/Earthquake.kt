package com.example.datasaverexampleapp.earth_quake_example.model

import android.location.Location
import java.util.*

data class Earthquake(private val _id:String? = "",
                      private val _date: Date? = Date(),
                      private val _details:String? = "",
                      private val _location:Location? = Location(""),
                      private val _magnitude:Double? = 0.0,
                      private val _link:String? = "")
{
    val id:String
    get() = _id?:""

    val date:Date
    get() = _date?: Date()

    val details:String
    get() = _details?:""

    val location:Location
    get() = _location?:Location("")

    val magnitude:Double
    get() = _magnitude?:0.0

    val link:String
    get() = _link?:""
}