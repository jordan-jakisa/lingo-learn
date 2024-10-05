package com.kerustudios.lingolearn.data.repositories

import com.kerustudios.lingolearn.data.models.Language

interface UserRepository {
    suspend fun updatePreferences(language: Language, goals: List<String>): Result<String>
}