package com.kerustudios.lingolearn.data

import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

sealed class FirebasePaths(val path: String) {
    class UsersCollection() : FirebasePaths("users")
    class UserDocument(uId: String) : FirebasePaths("users/$uId")
}