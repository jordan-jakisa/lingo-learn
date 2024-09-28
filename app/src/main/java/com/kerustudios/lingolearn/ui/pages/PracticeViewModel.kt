package com.kerustudios.lingolearn.ui.pages

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kerustudios.lingolearn.data.models.QuizQuestion
import com.kerustudios.lingolearn.data.repositories.LLM
import com.kerustudios.lingolearn.ui.navigation.PracticePage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val llm: LLM,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var _uiState = MutableStateFlow(PracticePageUiState())
    val uiState: StateFlow<PracticePageUiState>
        get() = _uiState

    private val practicePage = savedStateHandle.toRoute<PracticePage>()

    init {
        viewModelScope.launch {
            practicePage.topic?.let { getQuiz(it) }
        }
    }


    private suspend fun getQuiz(topic: String) {
        val response = llm.getContent<String>(topic).getOrNull()
        response?.let {
            try {
                val questions = Json.decodeFromString<List<QuizQuestion>>(it)
                _uiState.value = PracticePageUiState(questions = questions)
            } catch (e: Exception) {
                _uiState.value = PracticePageUiState(message = e.message ?: "Some error occurred!")
            }

        }
    }

}

data class PracticePageUiState(
    val questions: List<QuizQuestion>? = null,
    val message: String? = ""
)