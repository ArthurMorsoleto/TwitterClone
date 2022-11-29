package com.amb.twitterclone.ui.login

sealed class LoginViewState {
    object Success : LoginViewState()
    object Failure : LoginViewState()
    object EmailError : LoginViewState()
    object PasswordError : LoginViewState()
    object Loading : LoginViewState()
    object InvalidCredentials : LoginViewState()
    object UnknownUser : LoginViewState()
}