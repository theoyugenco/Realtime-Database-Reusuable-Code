/*
package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide

class Review : AppCompatActivity() {
    private lateinit var restaurantPic: ImageView
    private lateinit var feedbackWindow: TextView
    private lateinit var seeReviews: Button
    private lateinit var submitReview: Button
    private lateinit var ratingBar: RatingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        restaurantPic = findViewById(R.id.location_image)
        feedbackWindow = findViewById(R.id.feedback_window)
        seeReviews = findViewById(R.id.see_review_button)
        submitReview = findViewById(R.id.submit_review)
        ratingBar = findViewById(R.id.rating_bar)

        val name = intent.getStringExtra("name")
        val logo = intent.getStringExtra("logo")
        val address = intent.getStringExtra("address")


        Glide.with(this).load(logo).into(restaurantPic)
    }
}
*/