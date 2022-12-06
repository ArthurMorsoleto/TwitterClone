package com.amb.twitterclone.di

import com.amb.twitterclone.data.AuthRepository
import com.amb.twitterclone.domain.usecases.*
import com.google.firebase.firestore.FirebaseFirestore
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

    @Provides
    fun providesSingUpUseCase(
        authRepository: AuthRepository,
        fireStore: FirebaseFirestore
    ) = SingUpUseCase(authRepository, fireStore)

    @Provides
    fun providesFetchProfileUseCase(
        authRepository: AuthRepository,
        fireStore: FirebaseFirestore
    ) = ProfileUseCase(authRepository, fireStore)

    @Provides
    fun provideUpdateProfileUseCase(
        authRepository: AuthRepository,
        fireStore: FirebaseFirestore
    ) = UpdateProfileUseCase(authRepository, fireStore)
}
