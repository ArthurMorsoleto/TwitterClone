package com.amb.twitterclone.ui

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.amb.twitterclone.R
import com.amb.twitterclone.ui.signup.SignUpActivity
import com.amb.twitterclone.ui.signup.SingUpViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SingUpActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val viewModel = mockk<SingUpViewModel>(relaxed = true)

    @Before
    fun init() {
        hiltRule.inject()
        launchActivity<SignUpActivity>()
    }

    @Test
    fun test_EmailAndPasswordView_DisplayedWithSuccess() {
        onView(withId(R.id.til_username_sing_up)).check(matches(isDisplayed()))
        onView(withId(R.id.til_email_sing_up)).check(matches(isDisplayed()))
        onView(withId(R.id.til_password_sing_up)).check(matches(isDisplayed()))
    }
}
