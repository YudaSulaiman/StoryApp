package com.example.storyapp.view.addstory

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CameraViewModel(private val repository: Repository) : ViewModel() {
    fun addStory(token: String, imageMultipart: MultipartBody.Part, desc: RequestBody) = repository.addStory(token, imageMultipart, desc)
}