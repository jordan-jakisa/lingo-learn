package com.kerustudios.lingolearn.data.repositories

import com.google.ai.client.generativeai.type.Content

interface LLM {

    suspend fun <T> getContent(prompt: String): Result<T>
}