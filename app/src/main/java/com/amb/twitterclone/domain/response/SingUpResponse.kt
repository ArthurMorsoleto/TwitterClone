package com.amb.twitterclone.domain.response

sealed class SingUpResponse {
    object Success : SingUpResponse()
    object Error : SingUpResponse()
    object UserAlreadyRegistered : SingUpResponse()
}