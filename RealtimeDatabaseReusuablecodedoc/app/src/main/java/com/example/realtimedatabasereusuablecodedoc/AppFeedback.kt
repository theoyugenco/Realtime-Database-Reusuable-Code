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

/*
Kenneth Valero
An activity that will allow the user to give feedback to the developers
of MealSteal about the app.
 */
class AppFeedback : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var feedbackWindow: TextInputEditText
    private lateinit var submitButton: Button
    private lateinit var rate_text: TextView
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_feedback)

        /*
        Initialize rating bar and feedback window
         */
        rate_text = findViewById(R.id.app_rate)
        ratingBar = findViewById(R.id.app_rating_bar)
        submitButton = findViewById(R.id.submit_app_feedback)
        feedbackWindow = findViewById(R.id.app_feedback_window)

        auth = FirebaseAuth.getInstance()

        /*
        On clicking the submit button,
        add feedback entry to database.
         */
        submitButton.setOnClickListener {
            val feedback = feedbackWindow.text.toString()
            val getRatingValue = ratingBar.rating

            database = FirebaseDatabase.getInstance().getReference()
            val newFeedback = AppFeedbackDC(feedback, getRatingValue)

            //Create ID for database entry
            val feedbackID = database.child("ApplicationFeedback").push().key
            database.child("ApplicationFeedback/" + feedbackID).setValue(newFeedback)
        }
    }
}