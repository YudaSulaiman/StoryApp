package com.example.storyapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storyapp.MainCoroutineRule
import com.example.storyapp.data.retrofit.ApiService
import com.example.storyapp.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RepositoryTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var apiService: ApiService
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        apiService = FakeApiService()
        repository = Repository(apiService)
    }

    @Test
    fun `when userLogin should return LoginResult and not null`() = mainCoroutineRule.runBlockingTest{
        val expectedResult = DataDummy.generateDummyLoginResponse()
        val data = apiService.login("email", "username")
        val actualResult = data.loginResult
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when userRegister Success should return success and not null`() = mainCoroutineRule.runBlockingTest{
        val expectedResult = DataDummy.generateDummySignupResponse()
        val actualResult = apiService.register("name","email", "username")
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when addStory Success should return success not null`() = mainCoroutineRule.runBlockingTest{
        val expectedResult = DataDummy.generateDummyAddStoryResponse()
        val actualResult = apiService.addStory("token", DataDummy.getImage(), DataDummy.getDesc())
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(expectedResult, actualResult)
    }
}