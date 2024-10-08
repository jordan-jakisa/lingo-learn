package com.kerustudios.lingolearn.ui.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerustudios.lingolearn.data.PreferenceKeys
import com.kerustudios.lingolearn.data.models.Language
import com.kerustudios.lingolearn.data.repositories.PreferencesRepository
import com.kerustudios.lingolearn.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    fun updatePreferences(language: Language, goals: List<String>) {
        viewModelScope.launch {
            launch { userRepository.updatePreferences(language, goals) }
            launch { preferencesRepository.setValue(PreferenceKeys.IS_FIRST_TIME, false) }
        }
    }
}