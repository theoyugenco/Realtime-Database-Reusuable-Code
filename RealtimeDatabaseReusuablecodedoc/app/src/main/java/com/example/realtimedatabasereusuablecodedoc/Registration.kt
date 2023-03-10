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

/*
David Nguyen was responsible for the .xml file for this Activity
 */
class Registration : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var userType: String
    private lateinit var dbase: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        /*
        Theodore Yu
        Boiler Plate code that is similar across many different activities
         */

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        /*
        Kenneth Valero
        Code to setup the spinner that allows the user to choose between
        account types customer and merchant
         */
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

        /*
        Theodore Yu

        The code that adds the users' information into the database via the Register Button
         */
        binding.btnRegister.setOnClickListener() {

            val fName = binding.etFName.text.toString()
            val lName = binding.etLName.text.toString()
            val email = binding.etEmail.text.toString()
            val uName = binding.etUName.text.toString()
            val pWord = binding.etPWord.text.toString()
            val streetAddress = binding.etStreetAddress.text.toString()
            val city = binding.etCity.text.toString()
            val state = binding.etState.text.toString()
            val zipcode = binding.etZip.text.toString()
            val phone = binding.etPhone.text.toString()



            firebaseAuth.createUserWithEmailAndPassword(email, pWord)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        /*
                        Theodore Yu
                        We are able to successfully create the user's account using Firebase Authentication
                        Now we must record their account in the RealTimeDataBase
                         */
                        val uid: String = firebaseAuth.uid.toString()
                        val UserDC =
                            UserDC(fName, lName, pWord, uName, email, uid, streetAddress, city, state, zipcode, phone)
                        /*
                        Theodore Yu
                        It is important to delineate between Users (Customers and Merchants)
                        Although they share many features, their accounts and uses are fundamentally different
                         */

                        if (userType == "Customer") {
                            dbase =
                                FirebaseDatabase.getInstance().getReference("Users/Customers")
                            dbase.child(uid).setValue(UserDC)
                        } else if (userType == "Merchant") {
                            dbase =
                                FirebaseDatabase.getInstance().getReference("Users/Merchants")
                            dbase.child(uid).setValue(UserDC)
                        }

                        binding.etFName.text.clear()
                        binding.etLName.text.clear()
                        binding.etEmail.text.clear()
                        binding.etUName.text.clear()
                        binding.etPWord.text.clear()
                        binding.etStreetAddress.text.clear()
                        binding.etCity.text.clear()
                        binding.etState.text.clear()
                        binding.etZip.text.clear()
                        binding.etPhone.text.clear()

                        Toast.makeText(
                            this,
                            "Your account has been successfully created and registered.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    /*
                    Theodore Yu
                    This brings up to an activity where we can test our ability to manipulate data
                     */
                    binding.tvAdmin.setOnClickListener()
                    {
                        val intent = Intent(this, AdminDatabaseManipulator::class.java)
                        startActivity(intent)
                    }


                }
        }
    }
}
