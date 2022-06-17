package com.example.storyapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storyapp.MainCoroutineRule
import com.example.storyapp.data.retrofit.ApiService
import com.example.storyapp.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*

@ExperimentalCoroutinesApi
class StoryRepositoryTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        apiService = FakeApiService()
    }

    @Test
    fun `when getListStory Should Not Null`() = mainCoroutineRule.runBlockingTest {
        val expectedResult = DataDummy.generateDummyGetStoriesResponse()
        val actualResult = apiService.getAllStories("apiKey", null, null, 0)
        Assert.assertNotNull(actualResult.listStory)
        Assert.assertEquals(expectedResult.size, actualResult.listStory.size)
    }

    @Test
    fun `when getLocationStory Should Not Null and have LatLng Properties`() = mainCoroutineRule.runBlockingTest{
        val expectedResult = DataDummy.generateDummyGetStoriesResponse()
        val actualResult = apiService.getAllStories("apiKey", null, null, 1)
        Assert.assertNotNull(actualResult.listStory)
        Assert.assertNotNull(actualResult.listStory[0].lat)
        Assert.assertNotNull(actualResult.listStory[0].lon)
        Assert.assertEquals(expectedResult.size, actualResult.listStory.size)
    }
}