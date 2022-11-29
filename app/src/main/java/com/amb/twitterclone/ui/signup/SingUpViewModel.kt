package com.amb.twitterclone.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amb.twitterclone.domain.model.SingUpResponse
import com.amb.twitterclone.domain.usecases.SingUpUseCase
import com.amb.twitterclone.ui.signup.SingUpViewState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingUpViewModel @Inject constructor(
    private val singUpUseCase: SingUpUseCase
) : ViewModel() {

    private val _singUpStateLiveData: MutableLiveData<SingUpViewState> = MutableLiveData()
    val singUpStateLiveData: LiveData<SingUpViewState> get() = _singUpStateLiveData

    fun onSingUpClick(userName: String, email: String, password: String) {
        _singUpStateLiveData.value = Loading

        var proceedLogin = true
        if (userName.isEmpty()) {
            proceedLogin = false
            _singUpStateLiveData.value = UsernameError
        }
        if (email.isEmpty()) {
            proceedLogin = false
            _singUpStateLiveData.value = EmailError
        }
        if (password.isEmpty()) {
            proceedLogin = false
            _singUpStateLiveData.value = PasswordError
        }
        if (proceedLogin) {
            viewModelScope.launch {
                singUpUseCase(userName, email, password).collect {
                    _singUpStateLiveData.value = when (it) {
                        SingUpResponse.Error -> GenericError
                        SingUpResponse.Success -> Success
                        SingUpResponse.UserAlreadyRegistered -> UserAlreadyRegistered
                    }
                }
            }
        }
    }
}