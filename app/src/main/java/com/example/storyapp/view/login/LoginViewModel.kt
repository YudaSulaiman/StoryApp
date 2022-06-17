package com.example.storyapp.view.login

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.Repository

class   LoginViewModel(private val repository: Repository) : ViewModel() {

    fun userLogin(email: String, password: String) = repository.userLogin(email, password)

}