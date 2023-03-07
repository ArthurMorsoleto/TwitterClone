package com.amb.twitterclone.domain.response

sealed class ProfileResponse {
    data class UserData(
        val userEmail: String = "",
        val userName: String = "",
        val userImageUrl: String = "",
        val userFollowedHashTags: ArrayList<String> = arrayListOf(),
        val userFollowedUsers: ArrayList<String> = arrayListOf()
    ) : ProfileResponse()

    class Error(val errorType: ErrorType) : ProfileResponse()

    enum class ErrorType {
        NO_USER_FOUND,
        GENERIC
    }
}