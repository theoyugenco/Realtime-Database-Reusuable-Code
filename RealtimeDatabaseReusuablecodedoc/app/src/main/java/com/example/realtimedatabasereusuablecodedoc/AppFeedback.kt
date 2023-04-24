package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AppFeedback : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var feedbackWindow: TextInputEditText
    private lateinit var submitButton: Button
    private lateinit var rate_text: TextView
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var key: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_feedback)

        rate_text = findViewById(R.id.app_rate)
        ratingBar = findViewById(R.id.app_rating_bar)
        submitButton = findViewById(R.id.submit_app_feedback)
        feedbackWindow = findViewById(R.id.app_feedback_window)

        auth = FirebaseAuth.getInstance()

        val pushedRestaurant: DatabaseReference = database.push()
        key = pushedRestaurant.key.toString()

        submitButton.setOnClickListener {
            val feedback = feedbackWindow.text.toString()
            val getRatingValue = ratingBar.rating

            database = FirebaseDatabase.getInstance().getReference("Application Feedback")
            val newFeedback = AppFeedbackDC(feedback, getRatingValue)
            database.child(key!!).setValue(newFeedback)
        }
    }
}