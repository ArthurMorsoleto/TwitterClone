package com.amb.twitterclone

sealed class LoginState {
    object Success : LoginState()
    object Failure : LoginState()
    object EmailError : LoginState()
    object PasswordError : LoginState()
    object Loading : LoginState()
}