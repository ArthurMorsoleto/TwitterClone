@file:OptIn(ExperimentalCoroutinesApi::class)

package com.amb.twitterclone.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.amb.twitterclone.TestUtil
import com.amb.twitterclone.TestUtil.getValueForTest
import com.amb.twitterclone.domain.response.ProfileResponse
import com.amb.twitterclone.domain.usecases.LogoutUseCase
import com.amb.twitterclone.domain.usecases.ProfileUseCase
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainCoroutineRule = TestUtil.TestCoroutineRule()

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    private lateinit var subject: HomeViewModel
    private val logoutUseCase = mockk<LogoutUseCase>(relaxed = true)
    private val profileUseCase = mockk<ProfileUseCase>(relaxed = true)
    private val userDataResponse = ProfileResponse.UserData(
        userName = "user name",
        userImageUrl = "image"
    )

    @Before
    fun setup() {
        subject = HomeViewModel(logoutUseCase, profileUseCase)
    }

    @Test
    fun test_onLogoutButtonClick() {
        every { logoutUseCase() } just Runs
        subject.onLogoutButtonClick()
        verify { logoutUseCase() }
    }

    @Test
    fun test_onViewReady_FetchProfileData() {
        every { profileUseCase() } returns flow { emit(userDataResponse) }
        subject.onViewReady()
        assertEquals(userDataResponse.userImageUrl, subject.profileImageLiveData.getValueForTest())
    }
}