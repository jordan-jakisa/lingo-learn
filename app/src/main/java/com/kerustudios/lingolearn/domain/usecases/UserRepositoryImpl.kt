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
                db.document(FirebasePaths.UserDocument(it).path).update(
                    hashMapOf(
                        "language" to language.name,
                        "goals" to goals.toList()
                    )
                ).await()
            }
            Result.success("Preferences updated")
        } catch (e: FirebaseException) {
            Result.failure(e)
        }
    }

}