package com.kerustudios.lingolearn.ui.pages

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kerustudios.lingolearn.data.models.Quiz
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



    suspend fun getQuiz(topic: String) {
        val response = llm.getContent<String>(topic).getOrNull()
        response?.let {
            val quiz = Json.decodeFromString<Quiz>(it)
            Log.d("response", quiz.toString())
            _uiState.value = PracticePageUiState(quiz = quiz)
        }
    }

}

data class PracticePageUiState(
    val quiz: Quiz? = null,
)