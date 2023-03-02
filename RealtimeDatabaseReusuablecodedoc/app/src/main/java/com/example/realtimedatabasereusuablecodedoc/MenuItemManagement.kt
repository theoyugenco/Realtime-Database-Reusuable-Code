package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityMenuItemManagementBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import android.widget.TextView

class MenuItemManagement : AppCompatActivity() {
    private lateinit var binding: ActivityMenuItemManagementBinding
    private lateinit var btnAddMenuItem: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_management)

        binding = ActivityMenuItemManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnAddMenuItem.setOnClickListener() {
            val intent = Intent(this, MenuItemAdd::class.java)
            startActivity(intent)
        }

        binding.btnEdit.setOnClickListener(){
            val intent = Intent(this, MenuItemViewEdit::class.java)
            startActivity(intent)
        }



    }
}