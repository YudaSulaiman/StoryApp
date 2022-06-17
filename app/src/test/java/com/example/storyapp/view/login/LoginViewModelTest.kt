package com.example.storyapp.view.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.Fetch
import com.example.storyapp.data.Repository
import com.example.storyapp.data.response.LoginResult
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest{

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyData = DataDummy.generateDummyLoginResponse()
    private val dummyEmail = DataDummy.getDummyEmail()
    private val dummyPassword = DataDummy.getDummyPassword()

    @Before
    fun setUp(){
        loginViewModel = LoginViewModel(repository)
    }

    @Test
    fun `when userLogin Should Not Null and Return Success`() {
        val expectedResult = MutableLiveData<Fetch<LoginResult>>()
        expectedResult.value = Fetch.Success(dummyData)
        `when`(loginViewModel.userLogin(dummyEmail, dummyPassword)).thenReturn(expectedResult)

        val actualResult = loginViewModel.userLogin(dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(repository).userLogin(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Fetch.Success)
    }

    @Test
    fun `when Network Error or Wrong Email or Password Should Return Error`() {
        val expectedResult = MutableLiveData<Fetch<LoginResult>>()
        expectedResult.value = Fetch.Error("error")
        `when`(loginViewModel.userLogin(dummyEmail, dummyPassword)).thenReturn(expectedResult)

        val actualResult = loginViewModel.userLogin(dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(repository).userLogin(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Fetch.Error)
    }

}