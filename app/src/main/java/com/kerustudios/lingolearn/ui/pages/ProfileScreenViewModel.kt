package com.kerustudios.lingolearn.ui.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerustudios.lingolearn.data.models.User
import com.kerustudios.lingolearn.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileScreenViewState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                userRepository.getUserDetails().getOrNull().apply {
                    _uiState.value = _uiState.value.copy(user = this)
                }
            }
        }
    }
}

data class ProfileScreenViewState(
    val user: User? = null
)