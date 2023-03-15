@file:OptIn(ExperimentalCoroutinesApi::class)

package com.amb.twitterclone.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.amb.twitterclone.TestUtil
import com.amb.twitterclone.TestUtil.getValueForTest
import com.amb.twitterclone.domain.response.LoginResponse
import com.amb.twitterclone.domain.usecases.LoginUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val mainCoroutineRule = TestUtil.TestCoroutineRule()

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    private lateinit var subject: LoginViewModel
    private val loginUseCase = mockk<LoginUseCase>()
    private val email = "email@email.com"
    private val password = "pass123"

    @Before
    fun setup() {
        subject = LoginViewModel(loginUseCase)
    }

    @Test
    fun test_onButtonClick_EmailError() = runBlocking {
        subject.onLoginButtonClick("", password)
        assertEquals(LoginViewState.EmailError, subject.loginStateLiveData.getValueForTest())
    }

    @Test
    fun test_onButtonClick_PasswordError() = runBlocking {
        subject.onLoginButtonClick(email, "")
        assertEquals(LoginViewState.PasswordError, subject.loginStateLiveData.getValueForTest())
    }

    @Test
    fun test_onButtonClick_LoginSuccess() = runBlocking {
        coEvery { loginUseCase(any(), any()) } returns flow { emit(LoginResponse.Success) }
        subject.onLoginButtonClick(email, password)
        assertEquals(LoginViewState.Success, subject.loginStateLiveData.getValueForTest())
    }

    @Test
    fun test_onButtonClick_LoginInvalidCredentials() = runBlocking {
        coEvery { loginUseCase(any(), any()) } returns flow { emit(LoginResponse.InvalidCredentials) }
        subject.onLoginButtonClick(email, password)
        assertEquals(LoginViewState.InvalidCredentials, subject.loginStateLiveData.getValueForTest())
    }

    @Test
    fun test_onButtonClick_LoginUnknownUser() = runBlocking {
        coEvery { loginUseCase(any(), any()) } returns flow { emit(LoginResponse.UnknownUser) }
        subject.onLoginButtonClick(email, password)
        assertEquals(LoginViewState.UnknownUser, subject.loginStateLiveData.getValueForTest())
    }

    @Test
    fun test_onButtonClick_Failure() = runBlocking {
        coEvery { loginUseCase(any(), any()) } returns flow { emit(LoginResponse.GenericError) }
        subject.onLoginButtonClick(email, password)
        assertEquals(LoginViewState.Failure, subject.loginStateLiveData.getValueForTest())
    }
}