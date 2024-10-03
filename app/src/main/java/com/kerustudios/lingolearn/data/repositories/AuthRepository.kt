package com.kerustudios.lingolearn.data.repositories

import android.content.Intent
import androidx.credentials.Credential

interface AuthRepository {
    suspend fun signInUser(launchIntent: ((Intent) -> Unit)?): Result<Credential>
}