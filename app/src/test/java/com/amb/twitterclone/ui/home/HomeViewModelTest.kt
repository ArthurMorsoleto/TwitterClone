@file:OptIn(ExperimentalCoroutinesApi::class)

package com.amb.twitterclone.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.amb.twitterclone.TestUtil
import com.amb.twitterclone.domain.usecases.LogoutUseCase
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @Before
    fun setup() {
        subject = HomeViewModel(logoutUseCase)
    }

    @Test
    fun test_onLogoutButtonClick() {
        every { logoutUseCase() } just Runs
        subject.onLogoutButtonClick()
        verify { logoutUseCase() }
    }
}