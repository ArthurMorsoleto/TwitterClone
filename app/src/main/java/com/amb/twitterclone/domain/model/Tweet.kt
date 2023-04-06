package com.amb.twitterclone.domain.model

import com.amb.twitterclone.util.Extensions.emptyString

/**
 * Represents a Tweet from TwitterClone
 *
 * @property tweetId the [String] value for current tweet id.
 * @property userIds the [List] of users that will tweet or retweet a particularly tweet.
 * @property userName the [String] value of the user name that has tweeted.
 * @property text the [String] value with the content of a particularly tweet.
 * @property imageUrl the [String] value with the content of a image url from a particularly tweet.
 * @property timestamp the [Long] value with the time stamp from the tweet.
 * @property hashTags the [List] of hashTags related to the tweet.
 * @property likes the [List] of users that has liked the tweet.
 */
data class Tweet(
    val tweetId: String? = null,
    val userIds: ArrayList<String> = arrayListOf(),
    val userName: String = emptyString(),
    val text: String = emptyString(),
    val imageUrl: String = "",
    val timestamp: Long = 0,
    val hashTags: ArrayList<String> = arrayListOf(),
    val likes: ArrayList<String> = arrayListOf()
)
