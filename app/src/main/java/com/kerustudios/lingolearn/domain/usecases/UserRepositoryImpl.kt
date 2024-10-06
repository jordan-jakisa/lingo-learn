package com.kerustudios.lingolearn.domain.usecases

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kerustudios.lingolearn.data.FirebasePaths
import com.kerustudios.lingolearn.data.models.Language
import com.kerustudios.lingolearn.data.models.User
import com.kerustudios.lingolearn.data.repositories.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : UserRepository {

    override suspend fun updatePreferences(
        language: Language,
        goals: List<String>
    ): Result<String> {
        val uId = auth.currentUser?.uid
        return try {
            uId?.let {
                db.collection(FirebasePaths.UsersCollection().path).document(uId).update(
                    hashMapOf(
                        "language" to language,
                        "goals" to goals.toList(),
                    )
                ).await()
            }
            Result.success("Preferences updated")
        } catch (e: FirebaseException) {
            Result.failure(e)
        }
    }

    override suspend fun getUserDetails(): Result<User> {
        return try {
            val userId = auth.currentUser?.uid
            val snapshot = db.document(FirebasePaths.UserDocument(userId ?: "").path).get().await()
            val user = snapshot.toObject(User::class.java)
            Log.d("UserRepositoryImpl", "User ID: $userId")
            Log.d("UserRepositoryImpl", "RetrievedDetails: $user")
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(FirebaseException("User not found"))
            }
        } catch (e: FirebaseException) {
            Result.failure(e)
        }
    }

}