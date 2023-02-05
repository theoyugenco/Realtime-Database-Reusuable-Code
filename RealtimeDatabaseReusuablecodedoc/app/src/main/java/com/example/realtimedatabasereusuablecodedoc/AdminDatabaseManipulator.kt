package com.example.realtimedatabasereusuablecodedoc


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityMainBinding
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityRegistrationBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityAdminDatabaseManipulatorBinding

class AdminDatabaseManipulator : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDatabaseManipulatorBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_database_manipulator)

        binding = ActivityAdminDatabaseManipulatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRead.setOnClickListener(){
            val uName : String = binding.etUserName.text.toString()

            if (uName.isNotEmpty()){
                readData(uName)
            }
            else{
                Toast.makeText(this, "Please enter the Username", Toast.LENGTH_SHORT).show()
            }

        }






    }

    private fun readData(uName: String) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(uName).get().addOnSuccessListener {
            //If a node/entry of that specific UserName exists
            if(it.exists()){
                val fName = it.child("fname").value
                val lName = it.child("lname").value
                val age = it.child("age").value

                Toast.makeText(this, "Successfully retrieved and read User's data!", Toast.LENGTH_SHORT).show()
                binding.etUserName.text.clear()
                binding.tvFirstName.text = fName.toString()
                binding.tvLastName.text = lName.toString()
                binding.tvAge.text = age.toString()


            }
            //If they provided a Username that does not exist (no such entry or node with that Username)
            else{
                Toast.makeText(this, "A User with that UserName does NOT exist!", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener(){
            Toast.makeText(this, "Unable to read the User's data!", Toast.LENGTH_SHORT).show()
        }


    }
}