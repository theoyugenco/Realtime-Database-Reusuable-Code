package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import android.widget.TextView
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityMenuItemAddBinding


class MenuItemAdd : AppCompatActivity() {

    private lateinit var binding: ActivityItemMenuAddBinding
    private lateinit var editEmail: EditText
    private lateinit var editPass: EditText
    private lateinit var btnLog: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_item_add)
        binding = ActivityItemMenuAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        Theodore Yu
        Input Validation will not be done at this time.
        All Fields must be completed/filled to create.
         */

        binding.btnAdd.setOnClickListener(){
            val dishName : String? = binding.etName.text.toString()
            val description : String? = binding.etDescription.text.toString()
            val price : Float = binding.etPrice.text.toFloat()

            if ((!dishName.isNullOrEmpty()) && (!description.isNullOrEmpty()) && (!price.isNaN())){
                val item = MenuItemDC(dishName, description, price)
                firebaseAuth = FirebaseAuth.getInstance()
                val uid : String = firebaseAuth.uid.toString()
                database = FirebaseDatabase.getInstance().getReference("Users/Merchants/" + uid)
                database.child(dishName).setValue(item).addOnSuccessListener {
                    Toast.makeText(this, "The Menu Item has been created!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener() {
                    Toast.makeText(this, "The Menu Item was NOT able to be created.", Toast.LENGTH_SHORT).show()
                }

            }
            else{
                Toast.makeText(this, "Please fill out ALL fields.", Toast.LENGTH_SHORT).show()
            }
        }






    }
}