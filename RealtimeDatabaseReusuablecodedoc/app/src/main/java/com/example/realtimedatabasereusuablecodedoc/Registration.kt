package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityRegistrationBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth



class Registration : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var userType: String
    private lateinit var dbase: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val account_type = resources.getStringArray(R.array.account_type_array)
        val spinner = findViewById<Spinner>(R.id.account_type_spinner)
        val arrayAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, account_type)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Toast.makeText(applicationContext, account_type[p2], Toast.LENGTH_SHORT).show()
                userType = account_type[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.btnRegister.setOnClickListener() {
            val fName = binding.firstName.text.toString()
            val lName = binding.lastName.text.toString()
            val email = binding.Email.text.toString()
            val uName = binding.userName.text.toString()
            val pWord = binding.passWord.text.toString()


//            dbase = FirebaseDatabase.getInstance().getReference("Users")
//            val UserDC = UserDC(fName, lName, pWord, uName, email, firebaseAuth.uid.toString())
//            dbase.child(uName).setValue(UserDC).addOnSuccessListener {
//                //The (registering) User successfully registered (valid inputs)
//                //Thus we clear the input text provided to sanitize (confused EXACTLY why)
//                if (userType == "Customer") {
//                    dbase = FirebaseDatabase.getInstance().getReference("Users/Customers")
//                    dbase.child(uName).setValue(UserDC)
//                }
//                else if (userType == "Merchant") {
//                    dbase = FirebaseDatabase.getInstance().getReference("Users/Merchants")
//                    dbase.child(uName).setValue(UserDC)
//                }
//                binding.firstName.text.clear()
//                binding.lastName.text.clear()
//                binding.Email.text.clear()
//                binding.userName.text.clear()
//                binding.passWord.text.clear()
//                firebaseAuth.createUserWithEmailAndPassword(email, pWord).addOnSuccessListener {
//                    Toast.makeText(this, "Your account has been successfully created and registered.", Toast.LENGTH_SHORT).show()
//                }.addOnFailureListener(){
//                    //The account should be made in the RealTime Database but NOT able to login due to the authentication issue.
//                    Toast.makeText(this, "Failure in creating an account with Authentication Functionality.", Toast.LENGTH_SHORT).show()
//                }
//
//
//
//
//
//
//
//            }.addOnFailureListener(){
//                Toast.makeText(this, "Failure to create and register account.  Please try again.", Toast.LENGTH_SHORT).show()
//            }
            firebaseAuth.createUserWithEmailAndPassword(email, pWord)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        dbase = FirebaseDatabase.getInstance().getReference("Users")
                        val UserDC =
                            UserDC(fName, lName, pWord, uName, email, firebaseAuth.uid.toString())
                        dbase.child(uName).setValue(UserDC).addOnSuccessListener {
                            //The (registering) User successfully registered (valid inputs)
                            //Thus we clear the input text provided to sanitize (confused EXACTLY why)
                            if (userType == "Customer") {
                                dbase =
                                    FirebaseDatabase.getInstance().getReference("Users/Customers")
                                dbase.child(uName).setValue(UserDC)
                            } else if (userType == "Merchant") {
                                dbase =
                                    FirebaseDatabase.getInstance().getReference("Users/Merchants")
                                dbase.child(uName).setValue(UserDC)
                            }
                            binding.firstName.text.clear()
                            binding.lastName.text.clear()
                            binding.Email.text.clear()
                            binding.userName.text.clear()
                            binding.passWord.text.clear()
                            Toast.makeText(
                                this,
                                "Your account has been successfully created and registered.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Failure to create and register account.  Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            binding.tvAdmin.setOnClickListener()
            {
                val intent = Intent(this, AdminDatabaseManipulator::class.java)
                startActivity(intent)
            }


        }
    }
}