package com.amb.twitterclone.domain.usecases

import android.net.Uri
import com.amb.twitterclone.data.AuthRepository
import com.amb.twitterclone.domain.response.UpdateResponse
import com.amb.twitterclone.util.DATABASE_IMAGES
import com.amb.twitterclone.util.DATABASE_USERS
import com.amb.twitterclone.util.DATA_USER_IMAGE_URL
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UpdateProfileImageUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseDB: FirebaseFirestore
) {
    operator fun invoke(imageUri: Uri): Flow<UpdateResponse> {
        val userId = authRepository.getCurrentUserId()
        val filePath = firebaseStorage.reference.child(DATABASE_IMAGES).child(userId)
        return callbackFlow {
            try {
                filePath.putFile(imageUri)
                    .addOnSuccessListener { updateProfileImage(filePath, userId) }
                    .addOnFailureListener { trySend(UpdateResponse.UpdateImageError) }
            } catch (e: Exception) {
                trySend(UpdateResponse.UpdateImageError)
            }
            awaitClose { close() }
        }
    }

    private fun ProducerScope<UpdateResponse>.updateProfileImage(
        filePath: StorageReference,
        userId: String
    ) {
        filePath.downloadUrl
            .addOnSuccessListener { uri ->
                val url = uri.toString()
                firebaseDB.collection(DATABASE_USERS)
                    .document(userId)
                    .update(DATA_USER_IMAGE_URL, url)
                    .addOnSuccessListener { trySend(UpdateResponse.UpdateImageSuccess(url)) }
            }
            .addOnFailureListener { trySend(UpdateResponse.UpdateImageError) }
    }
}