package com.example.diceroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

/**
 * Allow the user to roll a dice and view the result on the screen.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // sets up the MainActivity by using code from the imports
        super.onCreate(savedInstanceState)
        // sets the starting layout
        setContentView(R.layout.activity_main)

        // saves a reference to the Button object in a variable
        // findViewById() - finds the Button in the layout
        //  R.id.button - the resource ID for the Button
        val rollButton: Button = findViewById(R.id.button)

        // set a click listener using a lambda
        rollButton.setOnClickListener { rollDice() }

        // Do a dice roll when the app starts
        rollDice()
    }

    /**
     * Roll the dice and update the screen with the result.
     */
    private fun rollDice() {
        // Create new Dice object with 6 sides and roll it
        val dice = Dice(6)
        val diceRolled = dice.roll()

        // Update the screen with the dice roll
//         val resultTextView: TextView = findViewById(R.id.textView)
//         resultTextView.text = diceRolled.toString()

        // Find the ImageView in the layout
        val resultImageView: ImageView = findViewById(R.id.imageView)

        // Determine which drawable resource ID to use based on the dice roll
        val drawableResourceDice = when (diceRolled) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }

        // Update the ImageView with the correct drawable resource ID
        resultImageView.setImageResource(drawableResourceDice)

        // Update the content description
        resultImageView.contentDescription = diceRolled.toString()

    }
}

class Dice(private val numSides: Int) {

    fun roll(): Int {
        return (1..numSides).random()
    }
}