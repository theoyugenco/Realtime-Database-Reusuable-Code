package com.example.realtimedatabasereusuablecodedoc

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityHomeCustomerBinding
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

/*
Theodore Yu
The home page for the Merchant
 */

class HomeCustomer : AppCompatActivity() {

    private lateinit var chatButton: ImageButton
    private lateinit var binding: ActivityHomeCustomerBinding
    private lateinit var profButton: ImageButton
    private lateinit var reviewButton: ImageView
    private lateinit var homeButton: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_customer)

        binding = ActivityHomeCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /*
        Theodore Yu
        Takes us to Chatting Functionality
         */
        chatButton = findViewById(R.id.message_btn)
        chatButton.setOnClickListener {
            val intent = Intent(this, ChatSearch::class.java)
            startActivity(intent)
        }

        /*
        Theodore Yu
        Takes the User to their Profile
         */
        profButton = findViewById(R.id.profile_btn)
        profButton.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        homeButton = findViewById(R.id.home_btn)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeCustomer::class.java)
            startActivity(intent)
        }
        /*
        reviewButton = findViewById(R.id.reviewButton)
        reviewButton.setOnClickListener {
            val intent = Intent(this, ReviewSearch::class.java)
            startActivity(intent)


        }

         */
    }


}



