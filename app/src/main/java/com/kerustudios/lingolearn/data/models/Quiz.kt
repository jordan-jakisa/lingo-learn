package com.kerustudios.lingolearn.data.models

import kotlinx.serialization.Serializable

@Serializable
data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val answer: String
)