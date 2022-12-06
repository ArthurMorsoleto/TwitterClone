package com.amb.twitterclone.domain.model

sealed class UpdateResponse {
    object Success : UpdateResponse()
    object Error : UpdateResponse()
}