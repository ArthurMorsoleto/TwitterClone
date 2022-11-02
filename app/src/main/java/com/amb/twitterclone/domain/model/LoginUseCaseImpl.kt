package com.amb.twitterclone.domain.model

import com.amb.twitterclone.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : LoginUseCase {

    override suspend fun invoke(email: String, password: String): Flow<Boolean> {
        return flow {
            try {
                val result = authRepository.loginFireBase(email, password)
                emit(result.user?.uid != null)
            } catch (e: Exception) {
                // TODO map invalid credentials response
                emit(false)
            }
        }
    }
}
