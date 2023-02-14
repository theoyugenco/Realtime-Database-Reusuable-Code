package com.example.realtimedatabasereusuablecodedoc

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityHomeCustomerBinding
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

private lateinit var chatButton: ImageView
private lateinit var binding: ActivityHomeCustomerBinding
private lateinit var profButton: ImageView

/*
Theodore Yu
The home page for the Merchant
 */

class HomeCustomer : AppCompatActivity() {
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
        chatButton = findViewById(R.id.messagingButton)
        chatButton.setOnClickListener {
            val intent = Intent(this, ChatSearch::class.java)
            startActivity(intent)
        }

        /*
        Theodore Yu
        Takes the User to their Profile
         */
        profButton = findViewById(R.id.profileButton)
        profButton.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }
    }
}


