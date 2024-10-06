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
import com.google.firebase.firestore.FirebaseFirestore
import com.kerustudios.lingolearn.data.FirebasePaths
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
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
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
                                db.collection(FirebasePaths.UsersCollection().path)
                                    .document(this.uid).set(
                                        hashMapOf(
                                            "uId" to this.uid,
                                            "email" to this.email,
                                            "name" to this.displayName,
                                            "picture" to this.photoUrl,
                                            "createdAt" to System.currentTimeMillis(),
                                            "phoneNumber" to auth.currentUser?.phoneNumber
                                        )
                                    )
                                _uiState.value = _uiState.value.copy(
                                    authStatus = AuthStatus.Success("Sign in success")
                                )
                            }
                        } catch (e: GoogleIdTokenParsingException) {
                            Log.e(
                                "google_signin",
                                "Received an invalid google id token response: ${e.message}",
                                e
                            )
                            _uiState.value = _uiState.value.copy(
                                authStatus = AuthStatus.Error("Error signing in, please try again")
                            )
                        } catch (e: Exception) {
                            Log.e("google_signin", "Unexpected error: ${e.message}")
                            _uiState.value = _uiState.value.copy(
                                authStatus = AuthStatus.Error("Error signing in, please try again")
                            )
                        }
                    } else {
                        Log.e("google_signin", "Unexpected type of credential")
                        _uiState.value = _uiState.value.copy(
                            authStatus = AuthStatus.Error("Error signing in, please try again")
                        )
                    }
                }
            }
        }
    }
}

data class AuthPageState(
    val authStatus: AuthStatus = AuthStatus.None
)

sealed class AuthStatus {
    data object None : AuthStatus()
    data class Error(val message: String) : AuthStatus()
    data class Success(val data: Any) : AuthStatus()
}