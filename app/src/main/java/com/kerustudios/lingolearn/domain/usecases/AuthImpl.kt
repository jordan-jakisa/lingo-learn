package com.kerustudios.lingolearn.domain.usecases

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.kerustudios.lingolearn.data.repositories.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthImpl @Inject constructor(
    private val googleIdOption: GetGoogleIdOption,
    private val credentialManager: CredentialManager,
    @ApplicationContext private val context: Context
) : AuthRepository {

    override suspend fun signInUser(
        launchIntent: ((Intent) -> Unit)?
    ): Result<Credential> {
        val signingRequest =
            GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()

        return try {
            val credential = credentialManager.getCredential(
                request = signingRequest,
                context = context,
            ).credential
            Log.e("google_signin","Credential success")
            Result.success(credential)
        } catch (e: NoCredentialException) {
            e.printStackTrace()
            Log.e("google_signin"," No credential found: ${e.message}")
            launchIntent?.let {
                launchIntent(getAddGoogleAccountIntent())
            }
            Result.failure(e)
        } catch (e: GetCredentialException) {
            Log.e("google_signin","Get credential exception: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    private fun getAddGoogleAccountIntent(): Intent {
        val intent = Intent(Settings.ACTION_ADD_ACCOUNT)
        intent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
        return intent
    }
}