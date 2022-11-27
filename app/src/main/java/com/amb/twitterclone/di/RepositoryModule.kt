package com.amb.twitterclone.di

import com.amb.twitterclone.data.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideFireBaseAuth() = FirebaseAuth.getInstance()

    @Provides
    fun providesAuthRepository(firebaseAuth: FirebaseAuth) = AuthRepository(firebaseAuth)
}
