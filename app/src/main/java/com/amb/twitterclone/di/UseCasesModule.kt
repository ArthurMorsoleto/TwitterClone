package com.amb.twitterclone.di

import com.amb.twitterclone.domain.repository.AuthRepository
import com.amb.twitterclone.domain.usecases.LoginUseCase
import com.amb.twitterclone.domain.usecases.LogoutUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCasesModule {

    @Provides
    fun providesLoginUseCase(authRepository: AuthRepository) = LoginUseCase(authRepository)

    @Provides
    fun providesLogoutUseCase(authRepository: AuthRepository) = LogoutUseCase(authRepository)
}
