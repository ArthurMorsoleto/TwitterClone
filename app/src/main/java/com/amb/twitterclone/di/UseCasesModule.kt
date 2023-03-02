package com.amb.twitterclone.di

import com.amb.twitterclone.data.AuthRepository
import com.amb.twitterclone.domain.usecases.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCasesModule {

    @Provides
    fun provideLoginUseCase(authRepository: AuthRepository) = LoginUseCase(authRepository)

    @Provides
    fun provideLogoutUseCase(authRepository: AuthRepository) = LogoutUseCase(authRepository)

    @Provides
    fun provideSingUpUseCase(
        authRepository: AuthRepository,
        fireStore: FirebaseFirestore
    ) = SingUpUseCase(authRepository, fireStore)

    @Provides
    fun provideFetchProfileUseCase(
        authRepository: AuthRepository,
        fireStore: FirebaseFirestore
    ) = ProfileUseCase(authRepository, fireStore)

    @Provides
    fun provideUpdateProfileUseCase(
        authRepository: AuthRepository,
        fireStore: FirebaseFirestore
    ) = UpdateProfileUseCase(authRepository, fireStore)

    @Provides
    fun provideUpdateProfileImageUseCase(
        authRepository: AuthRepository,
        firebaseStorage: FirebaseStorage,
        fireStore: FirebaseFirestore
    ) = UpdateProfileImageUseCase(authRepository, firebaseStorage, fireStore)
}
