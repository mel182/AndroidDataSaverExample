package com.jetpackcompose.imageediting.type_alias

import com.jetpackcompose.imageediting.R

data class Triple<T,U,Q>(val firstName:T, val lastName: U, val age:Q)

typealias User = Triple<String, String, Int>

// For drawable and layout example
typealias Layout = R.layout
typealias AppString = R.string
typealias ViewByID = R.id
typealias Drawable = R.drawable
typealias AppColor = R.color