package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.util.Random

//This is the dice roll activity where if the user does not know exactly what to eat, they can list
//6 options and the dice will pick a number 1 through 6 to make a decision
class DiceRoll : AppCompatActivity() {
    //Declares variables that are used on the activity
    private lateinit var roll_Btn : Button
    private lateinit var dice_Num : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice_roll)

        //calls and declares the corresponding IDs found in the activity file
        roll_Btn = findViewById(R.id.roll_btn)
        dice_Num = findViewById(R.id.diceNum)

        //calls rollDice function when the button is clicked
        roll_Btn.setOnClickListener(){
            rollDice()
        }
    }

    //Function that will roll the dice
    private fun rollDice() {
        //creates a random value between 1 and 6
        val randomNum : Int = kotlin.random.Random.nextInt(until = 6) + 1

        //displays the image of the dice that corresponds with the number that is rolled above
        val diceDisplay = when(randomNum){
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> {
                R.drawable.dice_6
            }
        }
        dice_Num.setImageResource(diceDisplay)
    }
}