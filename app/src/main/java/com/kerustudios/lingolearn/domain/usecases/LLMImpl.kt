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
    Generate a real-life scenario where a conversation can be held in a specific language. The scenario should be based on everyday situations like ordering food, asking for directions, or shopping.
    The languages can be any language as is input in the user prompt.

    Based on the conversation, create a practice quiz that includes both **Multiple Choice Questions (MCQs)** and **Fill-in-the-Blank** questions. These questions should focus on key vocabulary, phrases, grammar, and sentence structure used in the conversation.

    The user prompt: $prompt

    The format of the quiz should be as follows:
    
    ### Multiple Choice Questions (MCQ):
    Question: {
        'question': string (This should be derived from the conversation scenario),
        'options': Array<String> (Provide 4 options related to the conversation, where only one is correct),
        'answer': string (The correct answer from the options)
    }
    
    The quiz should contain a mix of these two types, with at least 3 MCQs and 3 fill-in-the-blank questions. Ensure the fill-in-the-blank questions focus on improving the user’s understanding of sentence structure and grammar.

    Example scenario: "A person is asking for directions to the nearest train station."

    Example quiz:

    #### Multiple Choice Question (MCQ):
    Question: {
        'question': 'What phrase can be used to ask for directions?',
        'options': ['Wo ist der Bahnhof?', 'Ich bin der Bahnhof.', 'Was ist der Bahnhof?', 'Ich gehe zum Bahnhof.'],
        'answer': 'Wo ist der Bahnhof?'
    }

    #### Fill-in-the-Blank Question:
    Question: {
        'question': 'Der Bahnhof ist _____ (next) der Schule.',
        'options': ['Wo ist der Bahnhof?', 'Ich bin der Bahnhof.', 'Was ist der Bahnhof?', 'Ich gehe zum Bahnhof.'],
        'answer': 'Wo ist der Bahnhof?'
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