package com.amb.twitterclone.ui.signup

sealed class SingUpViewState {
    object UsernameError : SingUpViewState()
    object EmailError : SingUpViewState()
    object PasswordError : SingUpViewState()
    object Loading : SingUpViewState()
    object GenericError : SingUpViewState()
    object Success : SingUpViewState()
    object UserAlreadyRegistered : SingUpViewState()
}