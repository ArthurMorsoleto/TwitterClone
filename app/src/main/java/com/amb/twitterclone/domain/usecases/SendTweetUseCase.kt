package com.amb.twitterclone.domain.usecases

import com.amb.twitterclone.data.AuthRepository
import com.amb.twitterclone.domain.response.SendTweetResponse
import com.amb.twitterclone.domain.model.Tweet
import com.amb.twitterclone.domain.model.User
import com.amb.twitterclone.util.DATABASE_TWEETS
import com.amb.twitterclone.util.DATABASE_USERS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SendTweetUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseDB: FirebaseFirestore
) {
    suspend operator fun invoke(
        tweetContent: String
    ): Flow<SendTweetResponse> {
        return callbackFlow {
            try {
                val userId = authRepository.getCurrentUserId()
                firebaseDB.collection(DATABASE_USERS)
                    .document(userId)
                    .get()
                    .addOnSuccessListener {
                        val user = it.toObject(User::class.java)
                        if (user == null || user.userName.isNullOrEmpty()) {
                            trySend(SendTweetResponse.SendTweetError)
                        } else {
                            val newTweet = Tweet(
                                tweetId = null,
                                userIds = arrayListOf(userId),
                                userName = user.userName,
                                text = tweetContent,
                                imageUrl = "imageUrl",
                                timestamp = System.currentTimeMillis(),
                                hashTags = arrayListOf(),
                                likes = arrayListOf()
                            )

                            this@callbackFlow.sendTweet(newTweet)
                        }
                    }
                    .addOnFailureListener {
                        trySend(SendTweetResponse.SendTweetError)
                    }
            } catch (e: Exception) {
                e.printStackTrace()
                trySend(SendTweetResponse.SendTweetError)
            }
            awaitClose { cancel() }
        }
    }

    private fun ProducerScope<SendTweetResponse>.sendTweet(tweet: Tweet) {
        with(this@sendTweet) {
            firebaseDB.collection(DATABASE_TWEETS).document().apply {
                val newTweet = tweet.copy(tweetId = id)
                set(newTweet)
                    .addOnCompleteListener {
                        trySend(SendTweetResponse.SendTweetSuccess)
                    }
                    .addOnFailureListener {
                        trySend(SendTweetResponse.SendTweetError)
                    }
            }
        }
    }
}
