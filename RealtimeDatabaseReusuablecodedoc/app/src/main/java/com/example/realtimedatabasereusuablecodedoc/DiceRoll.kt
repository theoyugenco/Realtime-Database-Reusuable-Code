package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.Random

class DiceRoll : AppCompatActivity() {
    private lateinit var roll_Btn : Button
    private lateinit var dice_Num : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice_roll)

        roll_Btn = findViewById(R.id.roll_btn)
        dice_Num = findViewById(R.id.diceNum)

        roll_Btn.setOnClickListener(){
            rollDice()
        }
    }

    private fun rollDice() {
        var randomNum : Int = kotlin.random.Random.nextInt(until = 6) + 1

        dice_Num.text = randomNum.toString()

        Toast.makeText(this, "Rolled the Dice", Toast.LENGTH_SHORT).show()
    }
}