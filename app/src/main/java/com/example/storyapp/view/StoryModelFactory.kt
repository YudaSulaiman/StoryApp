package com.example.storyapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.di.Injection
import com.example.storyapp.view.main.MainViewModel
import com.example.storyapp.view.maps.MapsViewModel

class StoryModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(Injection.provideStoryRepository(context)) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(Injection.provideStoryRepository(context)) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}