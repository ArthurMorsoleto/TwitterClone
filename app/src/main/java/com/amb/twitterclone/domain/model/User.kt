package com.amb.twitterclone.domain.model

/**
 * Represents a User from TwitterClone.
 *
 * @property email the [String] value of user's email address.
 * @property userName the [String] value of user's name.
 * @property imageUrl the [String] value of user's profile avatar.
 * @property followedHashTags the [ArrayList] value of user's followed hashTags.
 * @property followedUsers the [ArrayList] value of user's followed users.
 */
data class User(
    val email: String? = "",
    val userName: String? = "",
    val imageUrl: String? = "",
    val followedHashTags: ArrayList<String>? = arrayListOf(),
    val followedUsers: ArrayList<String>? = arrayListOf()
)