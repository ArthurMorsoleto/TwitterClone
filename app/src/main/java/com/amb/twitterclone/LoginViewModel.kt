package com.amb.twitterclone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amb.twitterclone.domain.model.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginStateLiveData: MutableLiveData<LoginState> = MutableLiveData()
    val loginStateLiveData: LiveData<LoginState> get() = _loginStateLiveData

    fun onLoginButtonClick(email: String, password: String) {
        _loginStateLiveData.value = LoginState.Loading

        var proceedLogin = true
        if (email.isEmpty()) {
            proceedLogin = false
            _loginStateLiveData.value = LoginState.EmailError
        }
        if (password.isEmpty()) {
            proceedLogin = false
            _loginStateLiveData.value = LoginState.PasswordError
        }
        if (proceedLogin) {
            viewModelScope.launch {
                loginUseCase(email, password).collect {
                    _loginStateLiveData.value = if (it) LoginState.Success else LoginState.Failure
                }
            }
        }
    }
}
