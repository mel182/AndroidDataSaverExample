package com.example.googlelogindemo.presentation.sign_in

data class SignInResult(val data: UserData?, val errorMessage: String?)

data class UserData(
    val userId: String,
    val userName: String?,
    val profilePictureUrl: String?,
)
