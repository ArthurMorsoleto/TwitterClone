package com.amb.twitterclone.domain.model

sealed class LoginResponse {
    object Success : LoginResponse()
    object InvalidCredentials : LoginResponse()
    object UnknownUser : LoginResponse()
    object GenericError : LoginResponse()
}