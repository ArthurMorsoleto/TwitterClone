package com.amb.twitterclone.domain.usecases

import androidx.core.net.toUri
import com.amb.twitterclone.data.AuthRepository
import com.amb.twitterclone.domain.model.Tweet
import com.amb.twitterclone.domain.model.User
import com.amb.twitterclone.domain.response.SendTweetResponse
import com.amb.twitterclone.util.DATABASE_IMAGES
import com.amb.twitterclone.util.DATABASE_TWEETS
import com.amb.twitterclone.util.DATABASE_USERS
import com.amb.twitterclone.util.Extensions.getHashTags
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SendTweetUseCase @Inject constructor(
    authRepository: AuthRepository,
    private val firebaseDB: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
) {
    private val userId = authRepository.getCurrentUserId()

    suspend operator fun invoke(
        params: SendTweetParams
    ): Flow<SendTweetResponse> {
        return callbackFlow {
            try {
                firebaseDB.collection(DATABASE_USERS)
                    .document(userId)
                    .get()
                    .addOnSuccessListener {
                        val user = it.toObject(User::class.java)
                        if (user == null || user.userName.isNullOrEmpty()) {
                            trySend(SendTweetResponse.SendTweetError)
                        } else {
                            this@callbackFlow.storeImageAndSendTweet(user.userName, params)
                        }
                    }
                    .addOnFailureListener { trySend(SendTweetResponse.SendTweetError) }
            } catch (e: Exception) {
                e.printStackTrace()
                trySend(SendTweetResponse.SendTweetError)
            }
            awaitClose { cancel() }
        }
    }

    private fun ProducerScope<SendTweetResponse>.storeImageAndSendTweet(
        userName: String,
        params: SendTweetParams
    ) {
        val tweetImage = params.tweetImage
        with(this@storeImageAndSendTweet) {
            if (tweetImage.isNullOrEmpty()) {
                sendTweet(userName, params)
            } else {
                //TODO move to another useCase and return uri
                val filePath = firebaseStorage.reference.child(DATABASE_IMAGES).child(userId)
                filePath.putFile(tweetImage.toUri())
                    .addOnSuccessListener {
                        filePath.downloadUrl.addOnCompleteListener {
                            if (it.isSuccessful) {
                                val tweet = params.copy(tweetImage = it.toString())
                                sendTweet(userName, tweet)
                            } else {
                                trySend(SendTweetResponse.SendTweetError)
                            }
                        }
                    }
                    .addOnFailureListener { trySend(SendTweetResponse.SendTweetError) }
            }
        }
    }

    private fun ProducerScope<SendTweetResponse>.sendTweet(
        userName: String,
        params: SendTweetParams
    ) {
        with(this@sendTweet) {
            firebaseDB.collection(DATABASE_TWEETS).document().apply {
                val newTweet = Tweet(
                    tweetId = id,
                    userIds = arrayListOf(userId),
                    userName = userName,
                    text = params.tweetContent,
                    imageUrl = params.tweetImage ?: "",
                    timestamp = System.currentTimeMillis(),
                    hashTags = params.tweetContent.getHashTags(),
                    likes = arrayListOf()
                )
                set(newTweet)
                    .addOnCompleteListener { trySend(SendTweetResponse.SendTweetSuccess) }
                    .addOnFailureListener { trySend(SendTweetResponse.SendTweetError) }
            }
        }
    }

    data class SendTweetParams(
        var tweetContent: String,
        var tweetImage: String? = null
    )
}
