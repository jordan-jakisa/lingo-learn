package com.kerustudios.lingolearn.data

sealed class FirebasePaths(val path: String) {
    class UsersCollection() : FirebasePaths("users")
    class UserDocument(uId: String) : FirebasePaths("users/$uId")
}