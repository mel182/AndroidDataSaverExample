package com.jetpackcompose.bottomnavigtion.interfaces

interface RepositoryCallback<T> {
    fun onResponse(response:T)
    fun onFailed(error:T)
}