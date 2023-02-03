package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbase: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerBtn. setOnClickListener(){
            val fName = binding.firstName.text.toString()
            val lName = binding.lastName.text.toString()
            val age = binding.age.text.toString()
            val uName = binding.userName.text.toString()

            dbase = FirebaseDatabase.getInstance().getReference("Users")
            val User = User(fname, lname, age, uName)
            dbase.child(uName).setValue(User).addOnSuccessListener {
                //The (registering) User successfully registered (valid inputs)
                //Thus we clear the input text provided to sanitize (confused EXACTLY why)
                binding.firstName.text.clear()
                binding.lastName.text.clear()
                binding.age.text.clear()
                binding.userName.text.clear()

                Toast.makeText(this, s)
            }
        }


    }
}