/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.example.android.unscramble.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment where the game is played, contains the game logic.
 */
class GameFragment : Fragment() {
    // Initialize the GameViewModel using the by viewModels() Kotlin property delegate
    // This allows to retrieve the previous state to handle configuration changes (e.g. device rotation)
    private val viewModel: GameViewModel by viewModels()

    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: GameFragmentBinding

    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same GameViewModel instance created by the
    // first fragment
    // Inflate the game_fragment layout XML using the binding object
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        // Inflate the layout XML file and return a binding object instance
        // Use Data Binding (instead of View binding): this allows to bind data to views + views to code
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the layout variables gameViewModel and maxNoOfWords (for Data Binding)
        // Bind the variable set in layout [gameViewModel] and [maxNoOfWords] with the view model instance (GameViewModel.kt) for Data Binding
        binding.gameViewModel = viewModel
        binding.maxNoOfWords = MAX_NO_OF_WORDS

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates.
        // [lifecycleOwner] observes changes of LiveData in this binding.
        // [viewLifecycleOwner] represents the Fragment's View lifecycle.
        binding.lifecycleOwner = viewLifecycleOwner

        // Setup a click listener for the Submit and Skip buttons.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }

    }

    // Creates and shows an AlertDialog with the final score.
    private fun showFinalScoreDialog() {
        // Use the MaterialAlertDialogBuilder class to build up parts of the dialog step-by-step
        MaterialAlertDialogBuilder(requireContext())
            // Set the title on the alert dialog
            .setTitle(getString(R.string.congratulations))
            // Set the message to show the final score
            .setMessage(getString(R.string.you_scored, viewModel.score.value))
            // Make your alert dialog not cancelable when the back key is pressed
            .setCancelable(false)
            // Add two text buttons EXIT and PLAY AGAIN
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                restartGame()
            }
            // Create and display the alert dialog
            .show()
    }

    /*
    * Checks the user's word, and updates the score accordingly when the Submit button is clicked.
    * If the next word is available, display a new word and update the score.
    * If another word is not available, update the display the alert dialog with the final score.
    */
    private fun onSubmitWord() {
        // Extract a word from input text field
        val playerWord = binding.textInputEditText.text.toString()

        // Check the player's word against the original
        if (viewModel.isUserWordCorrect(playerWord)) {
            // Remove the error message
            setErrorTextField(false)

            // Check if next word is available and show final score if it's not available
            if (!viewModel.nextWord()) {
                showFinalScoreDialog()
            }
        } else {
            setErrorTextField(true)
        }

    }

    /*
     * Skips the current word without changing the score.
     * Increases the word count.
     */
    private fun onSkipWord() {
        // Remove the error message
        setErrorTextField(false)

        // Check if next word is available
        if (viewModel.nextWord()) {
            setErrorTextField(false)
        } else {
            showFinalScoreDialog()
        }
    }

    /*
     * Re-initializes the data in the ViewModel and updates the views with the new data, to
     * restart the game.
     */
    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)
    }

    // Exit the game
    private fun exitGame() {
        activity?.finish()
    }

    // Set and reset the text field error status.
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            // Reset input field's text
            binding.textInputEditText.text = null
        }
    }
}
