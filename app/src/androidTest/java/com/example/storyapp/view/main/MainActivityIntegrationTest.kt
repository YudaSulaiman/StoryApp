package com.example.storyapp.view.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.storyapp.R
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.utils.EspressoIdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@MediumTest
class MainActivityIntegrationTest{

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp(){
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun getStoryListSuccess(){
        ActivityScenario.launch(MainActivity::class.java)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.rv_story))
            .check(matches(isDisplayed()))

        onView(withText("Maid"))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rv_story))
            .perform(
                swipeUp()
            )

        onView(withText("1234567"))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rv_story))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    7,
                    click()
                )
            )

        onView(withText("By Abiyansa"))
            .check(matches(isDisplayed()))
    }
}