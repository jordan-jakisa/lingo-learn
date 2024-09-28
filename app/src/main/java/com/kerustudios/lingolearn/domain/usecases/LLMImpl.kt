package com.kerustudios.lingolearn.domain.usecases

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.kerustudios.lingolearn.data.repositories.LLM
import javax.inject.Inject

class LLMImpl @Inject constructor(
    private val generativeModel: GenerativeModel
) : LLM {

    override suspend fun <T> getContent(prompt: String): Result<T> {

        val aiPrompt = """
            Generate a random scenario where a conversation can be held, then generate a quiz based
            on the conversation.
            Generate for me practice quiz for $prompt
            Question: {
                            'question': string,
                            'options': Array<string> example ['Ja', 'Ich bin', 'du bist', 'magst du'],
                            'answer': string example 'Ich bin'
                          }
            Return: Array<Question>
        """.trimIndent()

        val response = generativeModel.generateContent(aiPrompt)

        return if (response.text != null) {
            Log.d("response", response.text ?: "Null or empty")
            Result.success(response.text as T)
        } else {
            Result.failure(Exception("Response was null"))
        }
    }

}