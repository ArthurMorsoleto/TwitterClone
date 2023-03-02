package com.amb.twitterclone.domain.model

sealed class UpdateResponse {
    object Success : UpdateResponse()
    object Error : UpdateResponse()
    object UpdateImageError : UpdateResponse()
    class UpdateImageSuccess(val url: String) : UpdateResponse()
}