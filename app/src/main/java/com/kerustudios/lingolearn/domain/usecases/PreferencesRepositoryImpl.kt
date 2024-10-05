package com.kerustudios.lingolearn.domain.usecases

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.kerustudios.lingolearn.data.repositories.PreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class PreferencesRepositoryImpl @Inject constructor(
    private val preferences: DataStore<Preferences>
) : PreferencesRepository {

    override suspend fun <T> getValue(key: Preferences.Key<T>): T? {
        return preferences.data.map { it[key] }.first()
    }

    override suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        preferences.edit {
            it[key] = value
        }
    }

}