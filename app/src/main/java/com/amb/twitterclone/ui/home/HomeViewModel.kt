package com.amb.twitterclone.ui.home

import androidx.lifecycle.ViewModel
import com.amb.twitterclone.domain.usecases.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    fun onLogoutButtonClick() {
        logoutUseCase()
    }
}