package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityMenuManagementBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import android.widget.TextView

class MenuManagement : AppCompatActivity() {
    private lateinit var binding: ActivityMenuManagementBinding
    private lateinit var btnAddMenu: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_management)

        binding = ActivityMenuManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /*
        binding.btnAddMenu.setOnClickListener() {
            val intent = Intent(this, MenuAdd::class.java)
            startActivity(intent)
        }
        */



    }
}