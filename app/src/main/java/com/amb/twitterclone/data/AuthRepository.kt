package com.amb.twitterclone.data

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun loginFireBase(email: String, password: String): AuthResult {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    fun logoutFirebase() {
        return firebaseAuth.signOut()
    }

    suspend fun createUserFirebase(email: String, password: String): AuthResult {
        return firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    fun getCurrentUserId(): String {
        return firebaseAuth.currentUser?.uid ?: ""
    }
}