package com.kerustudios.lingolearn.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Language(
    val emoji: String = "",
    val name: String = "",
)
