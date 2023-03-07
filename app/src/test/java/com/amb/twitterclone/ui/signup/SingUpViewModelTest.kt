@file:OptIn(ExperimentalCoroutinesApi::class)

package com.amb.twitterclone.ui.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.amb.twitterclone.TestUtil
import com.amb.twitterclone.TestUtil.getValueForTest
import com.amb.twitterclone.domain.response.SingUpResponse
import com.amb.twitterclone.domain.usecases.SingUpUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SingUpViewModelTest {

    @get:Rule
    val mainCoroutineRule = TestUtil.TestCoroutineRule()

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    private lateinit var subject: SingUpViewModel

    private val singUpUseCase = mockk<SingUpUseCase>()
    private val userName = "userName"
    private val email = "email@email.com"
    private val password = "password"

    @Before
    fun setup() {
        subject = SingUpViewModel(singUpUseCase)
    }

    @Test
    fun test_onSingUp_Success() = runBlocking {
        coEvery { singUpUseCase(any(), any(), any()) } returns flow { emit(SingUpResponse.Success) }
        subject.onSingUpClick(userName, email, password)
        assertEquals(SingUpViewState.Success, subject.singUpStateLiveData.getValueForTest())
    }

    @Test
    fun test_onSingUp_UserAlreadyRegistered() = runBlocking {
        coEvery {
            singUpUseCase(any(), any(), any())
        } returns flow { emit(SingUpResponse.UserAlreadyRegistered) }
        subject.onSingUpClick(userName, email, password)
        assertEquals(
            SingUpViewState.UserAlreadyRegistered,
            subject.singUpStateLiveData.getValueForTest()
        )
    }

    @Test
    fun test_onSingUp_Error() = runBlocking {
        coEvery {
            singUpUseCase(any(), any(), any())
        } returns flow { emit(SingUpResponse.Error) }
        subject.onSingUpClick(userName, email, password)
        assertEquals(SingUpViewState.GenericError, subject.singUpStateLiveData.getValueForTest())
    }

    @Test
    fun test_onSingUp_UsernameError() = runBlocking {
        subject.onSingUpClick("", email, password)
        assertEquals(SingUpViewState.UsernameError, subject.singUpStateLiveData.getValueForTest())
    }

    @Test
    fun test_onSingUp_EmailError() = runBlocking {
        subject.onSingUpClick(userName, "", password)
        assertEquals(SingUpViewState.EmailError, subject.singUpStateLiveData.getValueForTest())
    }

    @Test
    fun test_onSingUp_PasswordError() = runBlocking {
        subject.onSingUpClick(userName, email, "")
        assertEquals(SingUpViewState.PasswordError, subject.singUpStateLiveData.getValueForTest())
    }
}