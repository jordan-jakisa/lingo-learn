package com.kerustudios.lingolearn.di

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.kerustudios.lingolearn.BuildConfig
import com.kerustudios.lingolearn.data.repositories.LLM
import com.kerustudios.lingolearn.domain.usecases.LLMImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AIModule {

    @Provides
    @Singleton
    fun providesGenerativeModel(): GenerativeModel {
        return GenerativeModel(
            "gemini-1.5-flash-002",
            BuildConfig.GEMINI_API_KEY,
            generationConfig = generationConfig {
                temperature = 1f
                topK = 30
                topP = 0.95f
                maxOutputTokens = 8192
                responseMimeType = "application/json"
            },
            systemInstruction = content { text("You are an intelligent system which is supposed to help students practice their language learning by generating quizes for them. Always generate 10 practice questions. Do not include questions which are supposed to have images! And each question should have 4 options for example a, b, c, d, keep the questions in english!") },
        )
    }

    @Provides
    @Singleton
    fun providesLLM(generativeModel: GenerativeModel): LLM {
        return LLMImpl(generativeModel)
    }

}