package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.*
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


/*
theodore Yu
Where Customer can custom their profile and retrieve personal information
 */
class Profile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: DatabaseReference

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage

    private lateinit var chosenImage: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        binding.userImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

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