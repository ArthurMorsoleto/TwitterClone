package com.amb.twitterclone.domain.model

sealed class SingUpResponse {
    object Success : SingUpResponse()
    object Error : SingUpResponse()
    object UserAlreadyRegistered : SingUpResponse()
}