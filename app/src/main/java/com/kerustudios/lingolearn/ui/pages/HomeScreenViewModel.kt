package com.kerustudios.lingolearn.ui.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerustudios.lingolearn.data.PreferenceKeys
import com.kerustudios.lingolearn.data.repositories.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    private var _uiState = MutableStateFlow(HomeScreenViewState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isFirstTime = preferencesRepository.getValue(PreferenceKeys.IS_FIRST_TIME) ?: true
            )
        }
    }

}

data class HomeScreenViewState(
    val isFirstTime: Boolean? = null
)