package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ChooseFavorite : AppCompatActivity() {
    /*
    Theodore Yu
    Takes the customer to two different recycler views of restaurants
    One is where they can see all available restaurants to favorite them
    The other is where they can see restaurants that they favorited
     */

    private lateinit var favoriteButton: Button
    private lateinit var viewButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_favorite)

        favoriteButton= findViewById(R.id.add_btn)
        favoriteButton.setOnClickListener{
            val intent = Intent(this, FavoriteRestaurants::class.java)
            startActivity(intent)
        }

        viewButton = findViewById(R.id.view_btn)
        viewButton.setOnClickListener(){
            val intent = Intent(this, ViewFavoriteRestaurants::class.java)
            startActivity(intent)
        }

    }
}