package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.view.View
import android.widget.*
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Profile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: DatabaseReference

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        //val test = auth.currentUser.
        database = FirebaseDatabase.getInstance().getReference("Users")
        val currentUserUID = auth.currentUser!!.uid

        database.child(currentUserUID).get().addOnSuccessListener {
            //If a node/entry of that specific Email exists
            if (it.exists()) {
                binding.tvEmail.editText!!.setText(auth.currentUser!!.email)

                val fName = it.child("fname").value
                val lName = it.child("lname").value

                val name = "$fName $lName"
                val name2 = "" + fName + " " + lName

                binding.tvName.editText!!.setText("test me")

            }
        }



    }
}