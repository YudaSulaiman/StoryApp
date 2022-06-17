package com.example.storyapp.utils

import com.example.storyapp.data.response.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {

    fun getDummyName(): String {
        return "1"
    }

    fun getDummyEmail(): String {
        return "1@1.com"
    }

    fun getDummyPassword(): String {
        return "1111111"
    }

    fun getDummyToken(): String {
        return "token123"
    }

    fun getImage(): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            "photo",
            "name",
        )
    }

    fun getDesc(): RequestBody{
        val desc = "dummy"
        return desc.toRequestBody("text/plain".toMediaType())
    }

    fun generateDummyLoginResponse(): LoginResult {
        return LoginResult("DummyName", "12345", "123")
    }

    fun generateDummySignupResponse(): RegisterResponse{
        return RegisterResponse(false, "success")
    }

    fun generateDummyAddStoryResponse(): AddStoryResponse{
        return AddStoryResponse(false, "success")
    }

    fun generateDummyStoryListEntity(): List<ListStoryItem>{
        val storyList = ArrayList<ListStoryItem>()
        for (i in 0..10){
            val story = ListStoryItem(
                "123",
                "https://story-api.dicoding.dev/images/stories/photos-1650186653947_5YF8VdHX.jpg",
                "2022-02-22T22:22:22Z",
                "dummy $i",
                "dummy desc $i",
                50.0,
                50.0
            )
            storyList.add(story)
        }
        return storyList
    }

    fun generateDummyGetStoriesResponse(): List<ListStoryItem>{
        val storyList: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100){
            val story = ListStoryItem(
                "123",
                "https://story-api.dicoding.dev/images/stories/photos-1650186653947_5YF8VdHX.jpg",
                "2022-02-22T22:22:22Z",
                "dummy $i",
                "dummy desc $i",
                50.0,
                50.0
            )
            storyList.add(story)
        }
        return storyList
    }


}