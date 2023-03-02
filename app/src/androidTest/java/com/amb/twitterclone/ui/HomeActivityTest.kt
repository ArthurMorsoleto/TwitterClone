package com.amb.twitterclone.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.amb.twitterclone.R
import com.amb.twitterclone.ui.home.HomeActivity
import com.amb.twitterclone.ui.home.HomeViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HomeActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val viewModel = mockk<HomeViewModel>(relaxed = true)

    private lateinit var subject: ActivityScenario<HomeActivity>

    @Before
    fun init() {
        hiltRule.inject()
        subject = launchActivity()
    }

    @Test
    fun test_EmailAndPasswordView_DisplayedWithSuccess() {
        onView(withId(R.id.button_logout)).check(matches(isDisplayed()))
        onView(withId(R.id.vp_sections)).check(matches(isDisplayed()))
        onView(withId(R.id.tab_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.image_user)).check(matches(isDisplayed()))
    }

    @Test
    fun test_LogoutButton_Click() {
        subject = launchActivity()
        onView(withId(R.id.button_logout)).perform(click())
        subject.onActivity { assertTrue(it.isFinishing) }
    }

    @Test
    fun test_SwipeLeft_Search_Selected() {
        onView(withId(R.id.vp_sections)).perform(swipeLeft())
        subject.onActivity {
            val tabLayout = it.getTabItem(1)
            tabLayout?.select()
            assertTrue(tabLayout?.isSelected ?: false)
        }
    }

    @Test
    fun test_SectionsTab_Home_Selected() {
        subject.onActivity {
            val tabLayout = it.getTabItem(0)
            tabLayout?.select()
            assertTrue(tabLayout?.isSelected ?: false)
        }
    }

    @Test
    fun test_SectionsTab_Search_Selected() {
        subject.onActivity {
            val tabLayout = it.getTabItem(1)
            tabLayout?.select()
            assertTrue(tabLayout?.isSelected ?: false)
        }
    }

    @Test
    fun test_SectionsTab_MyActivity_Selected() {
        subject.onActivity {
            val tabLayout = it.getTabItem(2)
            tabLayout?.select()
            assertTrue(tabLayout?.isSelected ?: false)
        }
    }

    private fun HomeActivity.getTabItem(itemIndex: Int) =
        this.findViewById<TabLayout>(R.id.tab_layout).getTabAt(itemIndex)
}
