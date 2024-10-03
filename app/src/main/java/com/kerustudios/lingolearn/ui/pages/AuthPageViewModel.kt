package com.kerustudios.lingolearn.ui.pages

import android.content.Intent
import android.util.Log
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kerustudios.lingolearn.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class AuthPageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private var _uiState = MutableStateFlow(AuthPageState())
    val uiState = _uiState.asStateFlow()

    fun handleSignIn(launchIntent: ((Intent) -> Unit)?) {
        viewModelScope.launch {

            val credential = authRepository.signInUser {
                launchIntent?.let { it1 -> it1(it) }
            }.getOrThrow()

            when (credential) {
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        try {
                            val googleIdTokenCredential =
                                GoogleIdTokenCredential.createFrom(credential.data)
                            val googleIdToken = googleIdTokenCredential.idToken
                            val authCredential =
                                GoogleAuthProvider.getCredential(googleIdToken, null)
                            val user = auth.signInWithCredential(authCredential).await().user
                            user?.run {
                                //update UI state
                            }
                        } catch (e: GoogleIdTokenParsingException) {
                            Log.e("google_signin", "Received an invalid google id token response: ${e.message}", e)
                        } catch (e: Exception) {
                            Log.e("google_signin", "Unexpected error: ${e.message}")
                        }
                    } else {
                        Log.e("google_signin", "Unexpected type of credential")
                    }
                }
            }
        }
    }
}

data class AuthPageState(
    val error: String? = null
)