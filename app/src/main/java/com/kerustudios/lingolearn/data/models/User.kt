package com.kerustudios.lingolearn.data.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uId: String = "",
    val email: String = "",
    val name: String = "",
    val picture: String = "",
    val phoneNumber: String = "",
    val language: Language? = null,
    val goals: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val completedQuizzes: List<QuizQuestion> = emptyList()
)