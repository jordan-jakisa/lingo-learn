package com.kerustudios.lingolearn.data.repositories

import com.kerustudios.lingolearn.data.models.Language
import com.kerustudios.lingolearn.data.models.User

interface UserRepository {
    suspend fun updatePreferences(language: Language, goals: List<String>): Result<String>
    suspend fun getUserDetails(): Result<User>

}