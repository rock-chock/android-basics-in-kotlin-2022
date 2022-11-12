package com.example.android.unscramble.ui.game

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

// Use ViewModel to separate data from UI
// This allows to prevent losing data on configuration changes when the activity is destroyed and recreated
class GameViewModel : ViewModel() {

    // Use backing property to make the property:
    // - mutable and private inside this class (GameViewModel)
    // - immutable and public outside this class (GameFragment)
    // Use val (not var) because the value of the LiveData/MutableLiveData object will remain the same,
    // and only the data stored within the object will change.
    private val _currentScrambledWord = MutableLiveData<String>()
    // Make the word accessible for talk back feature
    val currentScrambledWord: LiveData<Spannable> = Transformations.map(_currentScrambledWord) {
        if (it == null) {
            SpannableString("")
        } else {
            val scrambledWord = it.toString()
            val spannable: Spannable = SpannableString(scrambledWord)
            spannable.setSpan(
                TtsSpan.VerbatimBuilder(scrambledWord).build(),
                0,
                scrambledWord.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannable
        }
    }

    private val _score = MutableLiveData<Int>(0)
    val score: LiveData<Int>
        get() = _score

    private val _currentWordCount = MutableLiveData<Int>(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    // Create a list to track used words
    private var wordsList: MutableList<String> = mutableListOf()

    // Hold the word the player is trying to unscramble
    // Use backing property again
    // - private, var and _ : mutable and private inside this class (GameViewModel)
    // - val: immutable and public outside this class (GameFragment)
    private lateinit var _currentWord: String
    val currentWord: String
        get() = _currentWord

    init {
        // Display a scrambled word at the start of the app
        getNextWord()
    }

    private fun getNextWord() {

        _currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        while (String(tempWord) == currentWord) {
            tempWord.shuffle()
        }

        if (wordsList.contains(currentWord) and (wordsList.size < allWordsList.size)) {
            getNextWord()
        } else {
            // To access the data within a LiveData object, use the [value] property.
            _currentScrambledWord.value = String(tempWord)
            // inc() performs null-safe incrementing
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            wordsList.add(currentWord)
        }
    }

    /*
    * Returns true if the current word count is less than MAX_NO_OF_WORDS.
    * Updates the next word.
    */
    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    // Increase the score variable
    private fun increaseScore() {
        // plus() performs null-safety addition
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

    // Validate player's word
    // if the guess is correct, increase the score
    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    // Reset the game data to play the game again
    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }
}