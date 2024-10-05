package com.kerustudios.lingolearn.data.repositories

import androidx.datastore.preferences.core.Preferences
import com.kerustudios.lingolearn.data.models.User


interface PreferencesRepository {
    suspend fun <T> getValue(key: Preferences.Key<T>): T?
    suspend fun <T> setValue(key: Preferences.Key<T>, value: T)
}