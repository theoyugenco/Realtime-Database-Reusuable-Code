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

/*
Theodore Yu
This allows us to manipulate data from the RealTimeDatabase
We may draw snippets of code from here
 */

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

        binding.btnEdit.setOnClickListener(){
            val email : String? = binding.etEmail.text.toString()
            val uName : String? = binding.etUserName.text.toString()
            val pWord : String? = binding.etPassWord.text.toString()
            val fName : String? = binding.etFirstName.text.toString()
            val lName : String? = binding.etLastName.text.toString()


            if (uName.isNullOrEmpty()){
                Toast.makeText(this, "You must provide the UserName of the User you wish to edit!", Toast.LENGTH_SHORT).show()
            }
            else if ((fName.isNullOrEmpty()) && (lName.isNullOrEmpty()) && (pWord.isNullOrEmpty())){
                Toast.makeText(this, "You must provide at least one parameter to edit!", Toast.LENGTH_SHORT).show()
            }
            else{
                editData(uName, fName, lName, pWord)
            }
        }

        binding.btnDelete.setOnClickListener(){
            val uName : String = binding.etUserName.text.toString()

            if (uName.isNotEmpty()){
                deleteData(uName)
            }
            else{
                Toast.makeText(this, "Please enter the Username", Toast.LENGTH_SHORT).show()
            }
        }



    }

    private fun editData(uName: String?, fName: String?, lName: String?, pWord: String?) {
        var firstName : String? = fName
        var lastName : String? = lName
        var passWord : String? = pWord
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(uName!!).get().addOnSuccessListener {
            if(it.exists()) {
                //If a node/entry of that specific UserName exists
                if (firstName.isNullOrEmpty()) {
                    firstName = it.child("fname").value as String?
                }
                if (lastName.isNullOrEmpty()) {
                    lastName = it.child("lname").value as String?
                }
                if (passWord.isNullOrEmpty()) {
                    passWord = it.child("age").value as String?
                }

                val updated_user = mapOf<String, String>(
                    "fname" to firstName!!,
                    "lname" to lastName!!,
                    "pword" to passWord!!
                )
                database.child(uName!!).updateChildren(updated_user).addOnSuccessListener {
                    binding.etUserName.text.clear()
                    binding.etFirstName.text.clear()
                    binding.etLastName.text.clear()
                    binding.etPassWord.text.clear()
                    Toast.makeText(this, "Successfully updated the User's information\nUpdated Profile showcased below!", Toast.LENGTH_SHORT).show()
                    readData(uName)
                }.addOnFailureListener(){
                    Toast.makeText(this, "Unsuccessfully in updating User's information", Toast.LENGTH_SHORT).show()
                }
            }
            //If they provided a Username that does not exist (no such entry or node with that Username)
            else{
                Toast.makeText(this, "A User with that UserName does NOT exist!", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener(){
            Toast.makeText(this, "Unable to read the User's data for editting!", Toast.LENGTH_SHORT).show()
        }



    }

    private fun deleteData(uName: String) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(uName).removeValue().addOnSuccessListener {
            Toast.makeText(this, "Successfully Deleted User!", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener(){
            Toast.makeText(this, "Failed to Delete User", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readData(uName: String) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(uName).get().addOnSuccessListener {
            //If a node/entry of that specific UserName exists
            if(it.exists()){
                val fName = it.child("fname").value
                val lName = it.child("lname").value
                val age = it.child("pword").value

                Toast.makeText(this, "Successfully retrieved and read User's data!", Toast.LENGTH_SHORT).show()
                binding.etUserName.text.clear()
                binding.tvFirstName.text = fName.toString()
                binding.tvLastName.text = lName.toString()
                binding.tvPassWord.text = age.toString()


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