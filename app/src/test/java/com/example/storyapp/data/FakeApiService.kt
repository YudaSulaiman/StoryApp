package com.example.storyapp.data

import com.example.storyapp.data.response.AddStoryResponse
import com.example.storyapp.data.response.GetAllStoriesResponse
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.RegisterResponse
import com.example.storyapp.data.retrofit.ApiService
import com.example.storyapp.utils.DataDummy
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService: ApiService {

    private val dummyGetStoriesResponse = DataDummy.generateDummyGetStoriesResponse()
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyRegisterResponse = DataDummy.generateDummySignupResponse()
    private val dummyAddStoryResponse = DataDummy.generateDummyAddStoryResponse()

    override suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return dummyRegisterResponse
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        return LoginResponse(dummyLoginResponse, false, "success")
    }

    override suspend fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): AddStoryResponse {
        return dummyAddStoryResponse
    }

    override suspend fun getAllStories(
        token: String,
        page: Int?,
        size: Int?,
        location: Int
    ): GetAllStoriesResponse {
        return GetAllStoriesResponse(dummyGetStoriesResponse, false, "success")
    }
}