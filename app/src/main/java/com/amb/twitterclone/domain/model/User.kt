package com.amb.twitterclone.domain.model

data class User(
    val email: String? = "",
    val userName: String? = "",
    val imageUrl: String? = "",
    val followedHashTags: ArrayList<String>? = arrayListOf(),
    val followedUsers: ArrayList<String>? = arrayListOf()
)