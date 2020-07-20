package com.mateusmelodn.android.testquiz.db.entity

class Quiz(var language: Language, var words: List<Word>) {
    var currentWordIndex = 0

    fun getCurrentWord(): Word {
        if (currentWordIndex >= words.size) {
            currentWordIndex = 0
        }
        return words[currentWordIndex]
    }

    fun updateWordIndex() {
        currentWordIndex++
    }

    fun verifyAnswer(translatedWord: String): Boolean {
        return words[currentWordIndex].translatedWord.equals(translatedWord, true)
    }
}