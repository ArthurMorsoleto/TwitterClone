package com.amb.twitterclone.domain.usecases

import com.amb.twitterclone.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() {
        authRepository.logoutFirebase()
    }
}