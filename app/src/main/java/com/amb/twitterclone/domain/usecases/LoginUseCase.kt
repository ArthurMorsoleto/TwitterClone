package com.amb.twitterclone.domain.usecases

import com.amb.twitterclone.data.AuthRepository
import com.amb.twitterclone.domain.model.LoginResponse
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Flow<LoginResponse> {
        return flow {
            try {
                val result = authRepository.loginFireBase(email, password)
                if (result.user?.uid != null) {
                    emit(LoginResponse.Success)
                }
            } catch (e: Exception) {
                val response = when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        LoginResponse.InvalidCredentials
                    }
                    is FirebaseAuthInvalidUserException -> {
                        LoginResponse.UnknownUser
                    }
                    else -> {
                        LoginResponse.GenericError
                    }
                }
                emit(response)
            }
        }
    }
}
