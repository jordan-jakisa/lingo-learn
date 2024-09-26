package com.kerustudios.lingolearn.utils

fun String.isCorrect(answer: String): Boolean {
    return if (answer.length == 1){
        return this.first().toString().lowercase() == answer.lowercase()
    } else {
        this.lowercase() == answer.lowercase()
    }
}

