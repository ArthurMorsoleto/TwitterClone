package com.amb.twitterclone.domain.model

import kotlinx.coroutines.flow.Flow

interface LoginUseCase {

    suspend operator fun invoke(email: String, password: String): Flow<Boolean>
}