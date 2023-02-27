package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityRestaurantManagementBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import android.widget.TextView

class RestaurantManagement : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantManagementBinding
    private lateinit var btnAddRestaurant: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_management)

        binding = ActivityRestaurantManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnAddRestaurant.setOnClickListener() {
            val intent = Intent(this, RestaurantAdd::class.java)
            startActivity(intent)
        }



    }
}