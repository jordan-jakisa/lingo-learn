package com.kerustudios.lingolearn.data.models

import kotlinx.serialization.Serializable

@Serializable
data class QuizQuestion(
    val question: String = "",
    val options: List<String> = emptyList(),
    val answer: String = "",
    val explanation: String = ""
)