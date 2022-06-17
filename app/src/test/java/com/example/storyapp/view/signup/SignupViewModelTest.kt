package com.example.storyapp.view.signup

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
class SignupViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository
    private lateinit var signupViewModel: SignupViewModel
    private val dummyEmail = DataDummy.getDummyEmail()
    private val dummyPassword = DataDummy.getDummyPassword()
    private val dummyName = DataDummy.getDummyName()

    @Before
    fun setUp(){
        signupViewModel = SignupViewModel(repository)
    }

    @Test
    fun `when userRegister Should Not Null and Return Success`() {
        val expectedResult = MutableLiveData<Fetch<String>>()
        expectedResult.value = Fetch.Success("User Created")
        Mockito.`when`(signupViewModel.userSignup(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedResult)

        val actualResult = signupViewModel.userSignup(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(repository).userRegister(dummyName,dummyEmail, dummyPassword)
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Fetch.Success)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedResult = MutableLiveData<Fetch<String>>()
        expectedResult.value = Fetch.Error("error")
        Mockito.`when`(signupViewModel.userSignup(dummyName,dummyEmail, dummyPassword)).thenReturn(expectedResult)

        val actualResult = signupViewModel.userSignup(dummyName,dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(repository).userRegister(dummyName,dummyEmail, dummyPassword)
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Fetch.Error)
    }
}