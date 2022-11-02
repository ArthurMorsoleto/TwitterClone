package com.amb.twitterclone.di

import com.amb.twitterclone.domain.model.LoginUseCase
import com.amb.twitterclone.domain.model.LoginUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCasesModule {

    @Binds
    abstract fun bindLoginUseCase(useCase: LoginUseCaseImpl): LoginUseCase
}
