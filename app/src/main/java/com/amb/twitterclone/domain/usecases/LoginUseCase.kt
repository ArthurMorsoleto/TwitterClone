package com.amb.twitterclone.domain.usecases

import com.amb.twitterclone.domain.model.LoginResponse
import com.amb.twitterclone.domain.repository.AuthRepository
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
                var response: LoginResponse = LoginResponse.GenericError
                e.message?.let { error ->
                    when {
                        error.contains(invalidEmail) -> {
                            response = LoginResponse.InvalidCredentials
                        }
                        error.contains(invalidPassword) -> {
                            response = LoginResponse.InvalidCredentials
                        }
                        error.contains(invalidUser) -> {
                            response = LoginResponse.UnknownUser
                        }
                    }
                }
                emit(response)
            }
        }
    }

    companion object {
        const val invalidEmail = "The email address is badly formatted."
        const val invalidPassword =
            "The password is invalid or the user does not have a password."
        const val invalidUser =
            "There is no user record corresponding to this identifier. The user may have been deleted."
    }
}
