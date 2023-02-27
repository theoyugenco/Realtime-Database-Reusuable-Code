package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityItemMenuItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import android.widget.TextView


class ItemMenuItem : AppCompatActivity() {

    private lateinit var binding: ActivityItemMenuItemBinding
    private lateinit var editEmail: EditText
    private lateinit var editPass: EditText
    private lateinit var btnLog: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_menu_item)
        binding = ActivityItemMenuItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        btnAddItem.setOnclickListener {
            val intent = Intent(this, MenuItemAdd::class.java)
            startActivity(intent)
        }

        btnEditItem.setOnClickListener {
            val intent = Intent(this, MenuItemEdit::class.java)
            startActivity(intent)
        }

        btnViewItem.setOnClickListener {
            val intent = Intent(this, MenuItemView::class.java)
            startActivity(intent)
        }

         */
    }
}