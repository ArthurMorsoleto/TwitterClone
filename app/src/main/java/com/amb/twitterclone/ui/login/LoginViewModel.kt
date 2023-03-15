package com.amb.twitterclone.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amb.twitterclone.domain.response.LoginResponse.*
import com.amb.twitterclone.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginStateLiveData: MutableLiveData<LoginViewState> = MutableLiveData()
    val loginStateLiveData: LiveData<LoginViewState> get() = _loginStateLiveData

    fun onLoginButtonClick(email: String, password: String) {
        _loginStateLiveData.value = LoginViewState.Loading

        var proceedLogin = true
        if (email.isEmpty()) {
            proceedLogin = false
            _loginStateLiveData.value = LoginViewState.EmailError
        }
        if (password.isEmpty()) {
            proceedLogin = false
            _loginStateLiveData.value = LoginViewState.PasswordError
        }
        if (proceedLogin) {
            viewModelScope.launch {
                loginUseCase(email, password).collect { response ->
                    _loginStateLiveData.value = when (response) {
                        is Success -> LoginViewState.Success
                        is GenericError -> LoginViewState.Failure
                        is InvalidCredentials -> LoginViewState.InvalidCredentials
                        is UnknownUser -> LoginViewState.UnknownUser
                    }
                }
            }
        }
    }
}
