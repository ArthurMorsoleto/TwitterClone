package com.amb.twitterclone.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amb.twitterclone.domain.model.ProfileResponse
import com.amb.twitterclone.domain.usecases.LogoutUseCase
import com.amb.twitterclone.domain.usecases.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val profileUseCase: ProfileUseCase
) : ViewModel() {

    private val _profileImageLiveData = MutableLiveData<String>()
    val profileImageLiveData: LiveData<String> get() = _profileImageLiveData

    fun onLogoutButtonClick() {
        logoutUseCase()
    }

    fun onViewReady() {
        viewModelScope.launch {
            profileUseCase().collect {
                if (it is ProfileResponse.UserData) {
                    _profileImageLiveData.value = it.userImageUrl
                }
            }
        }
    }
}