package com.example.storyapp.view.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.Fetch
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest{

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: StoryRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyData = DataDummy.generateDummyGetStoriesResponse()
    private val token = DataDummy.getDummyToken()

    @Before
    fun setUp(){
        mapsViewModel = MapsViewModel(repository)
    }

    @Test
    fun `when Maps Should Not Null and Return Success`() {
        val expectedResult = MutableLiveData<Fetch<List<ListStoryItem>>>()
        expectedResult.value = Fetch.Success(dummyData)
        Mockito.`when`(mapsViewModel.getLocation(token)).thenReturn(expectedResult)

        val actualResult = mapsViewModel.getLocation(token).getOrAwaitValue()

        Mockito.verify(repository).getLocationStory(token)
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Fetch.Success)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedResult = MutableLiveData<Fetch<List<ListStoryItem>>>()
        expectedResult.value = Fetch.Error("error")
        Mockito.`when`(mapsViewModel.getLocation(token)).thenReturn(expectedResult)

        val actualResult = mapsViewModel.getLocation(token).getOrAwaitValue()

        Mockito.verify(repository).getLocationStory(token)
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Fetch.Error)
    }

}