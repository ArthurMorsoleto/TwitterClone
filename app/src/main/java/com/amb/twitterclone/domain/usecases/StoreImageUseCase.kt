package com.amb.twitterclone.domain.usecases

import android.net.Uri
import com.amb.twitterclone.data.AuthRepository
import com.amb.twitterclone.util.DATABASE_TWEET_IMAGES
import com.amb.twitterclone.util.STORE_IMAGE_ERROR
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.text.SimpleDateFormat
import java.util.*

class StoreImageUseCase(
    private val authRepository: AuthRepository,
    private val firebaseStorage: FirebaseStorage
) {
    suspend operator fun invoke(image: Uri): Flow<String> {
        val userId = authRepository.getCurrentUserId()
        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.UK).format(Date())
        val filePath = firebaseStorage.reference.child(DATABASE_TWEET_IMAGES)
            .child("$userId/$currentTime")
        return callbackFlow {
            filePath.putFile(image).apply {
                addOnSuccessListener {
                    filePath.downloadUrl.addOnCompleteListener {
                        if (it.isSuccessful) {
                            trySend(it.result.toString())
                        } else {
                            trySend(STORE_IMAGE_ERROR)
                        }
                    }
                }
                addOnFailureListener { trySend(STORE_IMAGE_ERROR) }
            }
            awaitClose { cancel() }
        }
    }
}