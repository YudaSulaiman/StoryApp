package com.example.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.storyapp.data.response.*
import com.example.storyapp.data.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository(private val apiService: ApiService){

    fun userLogin(email: String, password: String): LiveData<Fetch<LoginResult>> = liveData {
        emit(Fetch.Loading)
        try {
            val response = apiService.login(email, password)
            if(!response.error) {
                emit(Fetch.Success(response.loginResult))
            } else {
                emit(Fetch.NotFound)
            }
        } catch (e: Exception){
            Log.d("Repository", "userLogin: ${e.message.toString()}")
            emit(Fetch.Error(e.message.toString()))
        }
    }

    fun userRegister(name: String, email: String, password: String): LiveData<Fetch<String>> = liveData {
        emit(Fetch.Loading)
        try {
            val response = apiService.register(name, email, password)
            if(!response.error) {
                emit(Fetch.Success(response.message))
            } else {
                emit(Fetch.NotFound)
            }
        } catch (e: Exception){
            Log.d("Repository", "userRegister: ${e.message.toString()}")
            emit(Fetch.Error(e.message.toString()))
        }
    }

    fun addStory(token: String, imageMultipart: MultipartBody.Part, desc: RequestBody): LiveData<Fetch<String>> = liveData {

        emit(Fetch.Loading)
        try {
            val response = apiService.addStory(token, imageMultipart, desc)
            if(!response.error) {
                emit(Fetch.Success(response.message))
            } else {
                emit(Fetch.NotFound)
            }
        } catch (e: Exception){
            Log.d("Repository", "addStory: ${e.message.toString()}")
            emit(Fetch.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(apiService: ApiService): Repository =
            instance ?: synchronized(this){
                instance ?: Repository(apiService)
            }.also { instance = it }
    }
}

