package com.example.storyapp.view.maps

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.StoryRepository

class MapsViewModel (private val repository: StoryRepository) : ViewModel() {

    fun getLocation(token: String) = repository.getLocationStory(token)

}