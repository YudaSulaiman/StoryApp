package com.example.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.data.retrofit.ApiService
import com.example.storyapp.data.room.StoryDatabase
import com.example.storyapp.utils.wrapEspressoIdlingResource

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {

    fun getListStory(token: String): LiveData<PagingData<ListStoryItem>>{
        @OptIn(ExperimentalPagingApi::class)
        wrapEspressoIdlingResource {
            return Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
                remoteMediator = StoryRemoteMediator(token, storyDatabase, apiService),
                pagingSourceFactory = {
                    storyDatabase.storyDao().getAllStory()
                }
            ).liveData
        }

    }

    fun getLocationStory(token: String): LiveData<Fetch<List<ListStoryItem>>> = liveData {
        emit(Fetch.Loading)
        try {
            val response = apiService.getAllStories(token, null, 100, 1)
            if(!response.error) {
                emit(Fetch.Success(response.listStory))
            } else {
                emit(Fetch.NotFound)
            }
        } catch (e: Exception){
            Log.d("Repository", "getAllStories: ${e.message.toString()}")
            emit(Fetch.Error(e.message.toString()))
        }
    }
}