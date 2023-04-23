package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ChooseFavorite : AppCompatActivity() {

    private lateinit var favoriteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_favorite)

        favoriteButton= findViewById(R.id.add_btn)
        favoriteButton.setOnClickListener{
            val intent = Intent(this, FavoriteRestaurants::class.java)
            startActivity(intent)
        }
    }
}