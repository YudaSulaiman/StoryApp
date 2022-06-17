package com.example.storyapp.view.addstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.Fetch
import com.example.storyapp.data.Repository
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
class CameraViewModelTest{

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository
    private lateinit var cameraViewModel: CameraViewModel
    private val dummyImage = DataDummy.getImage()
    private val dummyDesc = DataDummy.getDesc()
    private val token = DataDummy.getDummyToken()

    @Before
    fun setUp(){
        cameraViewModel = CameraViewModel(repository)
    }

    @Test
    fun `when userRegister Should Not Null and Return Success`() {
        val expectedResult = MutableLiveData<Fetch<String>>()
        expectedResult.value = Fetch.Success("success")
        Mockito.`when`(cameraViewModel.addStory(token, dummyImage, dummyDesc)).thenReturn(expectedResult)

        val actualResult = cameraViewModel.addStory(token, dummyImage, dummyDesc).getOrAwaitValue()

        Mockito.verify(repository).addStory(token, dummyImage, dummyDesc)
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Fetch.Success)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedResult = MutableLiveData<Fetch<String>>()
        expectedResult.value = Fetch.Error("error")
        Mockito.`when`(cameraViewModel.addStory(token, dummyImage, dummyDesc)).thenReturn(expectedResult)

        val actualResult = cameraViewModel.addStory(token, dummyImage, dummyDesc).getOrAwaitValue()

        Mockito.verify(repository).addStory(token, dummyImage, dummyDesc)
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Fetch.Error)
    }
}