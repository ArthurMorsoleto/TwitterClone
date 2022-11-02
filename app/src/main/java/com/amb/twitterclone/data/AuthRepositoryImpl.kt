package com.amb.twitterclone.data

import com.amb.twitterclone.domain.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun loginFireBase(email: String, password: String): AuthResult {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }
}