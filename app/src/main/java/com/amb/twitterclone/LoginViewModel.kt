package com.amb.twitterclone

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

    fun callLogin(email: String, password: String) {
        viewModelScope.launch {
            loginUseCase(email, password).collect {
                print(it)
            }
        }
    }
}
