package com.example.emailsplitter

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class HelloWorldEspressoTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testSplitButton() {
        onView(withText("Split Email")).perform(click())
        onView(withId(R.id.usernameView)).check(matches(withText("try")))
        onView(withId(R.id.emailInput)).perform(clearText())
            .perform(typeText("new@email.com"))
        onView(withText("Split Email")).perform(click())
        onView(withId(R.id.domainView)).check(matches(withText("email.com")))

    }
}