package com.example.storyapp.model

data class UserModel(
    var name: String? = null,
    var userId: String? = null,
    var token: String? = null,
    var isLogin: Boolean = false
)
