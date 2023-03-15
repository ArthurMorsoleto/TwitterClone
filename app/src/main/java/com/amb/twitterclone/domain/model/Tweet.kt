package com.amb.twitterclone.domain.model

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
    val tweetId: String?,
    val userIds: ArrayList<String>,
    val userName: String,
    val text: String,
    val imageUrl: String = "",
    val timestamp: Long,
    val hashTags: ArrayList<String> = arrayListOf(),
    val likes: ArrayList<String> = arrayListOf()
)
