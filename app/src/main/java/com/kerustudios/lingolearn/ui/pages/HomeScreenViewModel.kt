package com.kerustudios.lingolearn.ui.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerustudios.lingolearn.data.PreferenceKeys
import com.kerustudios.lingolearn.data.models.User
import com.kerustudios.lingolearn.data.repositories.PreferencesRepository
import com.kerustudios.lingolearn.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private var _uiState = MutableStateFlow(HomeScreenViewState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                _uiState.value = _uiState.value.copy(
                    isFirstTime = preferencesRepository.getValue(PreferenceKeys.IS_FIRST_TIME)
                        ?: true
                )
            }
            launch {
                userRepository.getUserDetails().getOrNull().apply {
                    _uiState.value = _uiState.value.copy(user = this)
                }
            }
        }
    }

}

data class HomeScreenViewState(
    val isFirstTime: Boolean? = null,
    val user: User? = null
)