package com.amb.twitterclone.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amb.twitterclone.domain.model.ProfileResponse
import com.amb.twitterclone.domain.model.UpdateResponse
import com.amb.twitterclone.domain.usecases.ProfileUseCase
import com.amb.twitterclone.domain.usecases.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private val _profileViewState = MutableLiveData<ProfileViewState>()
    val profileViewState: LiveData<ProfileViewState> get() = _profileViewState

    fun onViewReady() {
        _profileViewState.value = ProfileViewState.Loading
        viewModelScope.launch {
            profileUseCase().collect {
                when (it) {
                    is ProfileResponse.Error -> {
                        _profileViewState.value = when (it.errorType) {
                            ProfileResponse.ErrorType.NO_USER_FOUND -> ProfileViewState.EmptyData
                            ProfileResponse.ErrorType.GENERIC -> ProfileViewState.GenericError
                        }
                    }
                    is ProfileResponse.UserData -> {
                        _profileViewState.value = ProfileViewState.ProfileData(it.userName)
                    }
                }
            }
        }
    }

    fun onApply(newUserName: String) {
        _profileViewState.value = ProfileViewState.Loading
        viewModelScope.launch {
            updateProfileUseCase(newUserName).collect {
                _profileViewState.value = when (it) {
                    UpdateResponse.Error -> ProfileViewState.UpdateError
                    UpdateResponse.Success -> ProfileViewState.UpdateSuccess
                }
            }
        }
    }
}
