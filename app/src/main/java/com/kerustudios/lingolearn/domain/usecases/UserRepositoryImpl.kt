package com.kerustudios.lingolearn.domain.usecases

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kerustudios.lingolearn.data.FirebasePaths
import com.kerustudios.lingolearn.data.models.Language
import com.kerustudios.lingolearn.data.repositories.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : UserRepository {

    override suspend fun updatePreferences(language: Language, goals: Set<String>): Result<String> {
        val uId = auth.currentUser?.uid
        return try {
            uId?.let {
                db.collection(FirebasePaths.UsersCollection().path).add(
                    hashMapOf(
                        "language" to language.name,
                        "goals" to goals.toList(),
                        "uId" to uId,
                        "picture" to auth.currentUser?.photoUrl,
                        "name" to auth.currentUser?.displayName,
                        "email" to auth.currentUser?.email,
                        "createdAt" to System.currentTimeMillis(),
                        "phone_number" to auth.currentUser?.phoneNumber
                    )
                ).await()
            }
            Result.success("Preferences updated")
        } catch (e: FirebaseException) {
            Result.failure(e)
        }
    }

}