package com.example.storyapp.view.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.response.ListStoryItem

class MainViewModel(private val repository: StoryRepository) : ViewModel() {

    fun getStory(token: String) : LiveData<PagingData<ListStoryItem>> = repository.getListStory(token).cachedIn(viewModelScope)

}

