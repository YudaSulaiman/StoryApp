package com.example.storyapp.view.signup

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.Repository

class SignupViewModel(private val repository: Repository) : ViewModel() {

    fun userSignup(name: String, email: String, password: String) = repository.userRegister(name, email, password)

}