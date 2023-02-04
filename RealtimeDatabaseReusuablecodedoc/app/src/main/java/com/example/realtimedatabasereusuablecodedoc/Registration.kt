package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityRegistrationBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Registration : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var dbase: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener(){
            val fName = binding.firstName.text.toString()
            val lName = binding.lastName.text.toString()
            val age = binding.age.text.toString()
            val uName = binding.userName.text.toString()

            dbase = FirebaseDatabase.getInstance().getReference("Users")
            val UserDC = UserDC(fName, lName, age, uName)
            dbase.child(uName).setValue(UserDC).addOnSuccessListener {
                //The (registering) User successfully registered (valid inputs)
                //Thus we clear the input text provided to sanitize (confused EXACTLY why)
                binding.firstName.text.clear()
                binding.lastName.text.clear()
                binding.age.text.clear()
                binding.userName.text.clear()

                Toast.makeText(this, "Your account has been successfully created and registered.", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener(){
                Toast.makeText(this, "Failure to create and register account.  Please try again.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvAdmin.setOnClickListener()
        {
            val intent = Intent(this, AdminDatabaseManipulator::class.java)
            startActivity(intent)
        }


    }
}