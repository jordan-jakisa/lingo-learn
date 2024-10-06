package com.kerustudios.lingolearn.di

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.datastore.preferences.preferencesDataStore
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Schema
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kerustudios.lingolearn.BuildConfig
import com.kerustudios.lingolearn.data.repositories.LLM
import com.kerustudios.lingolearn.domain.usecases.LLMImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = "preferences")

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
                responseSchema = Schema.arr(
                    name = "singleQuiz",
                    description = "A quiz to practice a given language which consists of the question, options and the correct answer. The Quiz is a list of questions",
                    items = Schema.obj(
                        name = "question",
                        description = "The question which is either a multiple choice question or a fill in the blank question",
                        contents = arrayOf(
                            Schema.str(
                                name = "question",
                                description = "The question in the quiz"
                            ),
                            Schema.arr(
                                name = "options",
                                description = """The possible answers to the question, make them as diverse as possible and 
                                   sometimes as similar as possible such that it is not so easy to tell which answers are 
                                   correct when a user is an advanced learner. The options should always be 4""".trimMargin(),
                                items = Schema.str(
                                    name = "option",
                                    description = "An optional answer for the question"
                                )
                            ),
                            Schema.str(
                                name = "answer",
                                description = "The correct answer to the question",
                            )
                        )
                    )
                )
            },
            systemInstruction = content { text("You are an intelligent system which is supposed to help students practice their language learning by generating quizes for them. Always generate 10 practice questions. Do not include questions which are supposed to have images! And each question should have 4 options for example a, b, c, d, keep the questions in english!") },
        )
    }

    @Provides
    @Singleton
    fun providesLLM(generativeModel: GenerativeModel): LLM {
        return LLMImpl(generativeModel)
    }

    @Provides
    @Singleton
    fun providesCredentialManager(@ApplicationContext context: Context) =
        CredentialManager.create(context)

    @Provides
    @Singleton
    fun providesGoogleIdOption(@ApplicationContext context: Context): GetGoogleIdOption {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.joinToString("") { "%02x".format(it) }

        return GetGoogleIdOption.Builder()
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .setNonce(hashedNonce)
            .build()
    }

    @Provides
    @Singleton
    fun providesFirebaseAuth() = Firebase.auth

    @Provides
    @Singleton
    fun providesSignInWithGoogleOption(): GetSignInWithGoogleOption {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.joinToString("") { "%02x".format(it) }

        return GetSignInWithGoogleOption.Builder(BuildConfig.WEB_CLIENT_ID)
            .setNonce(hashedNonce)
            .build()
    }

    @Provides
    @Singleton
    fun providesFirebaseFireStore() = Firebase.firestore


    @Provides
    @Singleton
    fun providesDataStorePreferences(@ApplicationContext context: Context) = context.dataStore

}