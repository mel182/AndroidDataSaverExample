package com.example.datasaverexampleapp.type_alias

import com.example.datasaverexampleapp.R

data class Triple<T,U,Q>(val firstName:T, val lastName: U, val age:Q)

typealias User = Triple<String,String,Int>

// For drawable and layout example
typealias Layout = R.layout
typealias Menu = R.menu
typealias AppString = R.string
typealias ViewByID = R.id
typealias Drawable = R.drawable

typealias CustomTextView = com.example.datasaverexampleapp.customTextview.FontTextView