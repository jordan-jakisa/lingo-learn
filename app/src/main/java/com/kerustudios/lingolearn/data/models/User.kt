package com.kerustudios.lingolearn.data.models

data class User(
    val uId: String,
    val email: String,
    val name: String,
    val picture: String,
    val phoneNumber: String,
    val language: Language,
    val goals: List<String>,
)
