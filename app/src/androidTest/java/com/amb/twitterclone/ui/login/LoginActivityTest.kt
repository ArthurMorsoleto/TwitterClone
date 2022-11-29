package com.amb.twitterclone.ui.login

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.amb.twitterclone.R
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val viewModel = mockk<LoginViewModel>(relaxed = true)

    @Before
    fun init() {
        hiltRule.inject()
        launchActivity<LoginActivity>()
    }

    @Test
    fun test_EmailAndPasswordView_DisplayedWithSuccess() {
        onView(withId(R.id.passwordTIL)).check(matches(isDisplayed()))
        onView(withId(R.id.emailTIL)).check(matches(isDisplayed()))
    }
}
