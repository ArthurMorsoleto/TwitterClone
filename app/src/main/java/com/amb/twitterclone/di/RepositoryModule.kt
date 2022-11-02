package com.amb.twitterclone.di

import com.amb.twitterclone.data.AuthRepositoryImpl
import com.amb.twitterclone.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(repository: AuthRepositoryImpl): AuthRepository

    @Module
    @InstallIn(ViewModelComponent::class)
    object FireBaseModule {

        @Provides
        fun provideFireBaseAuth() = FirebaseAuth.getInstance()
    }
}
