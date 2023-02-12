package com.example.realtimedatabasereusuablecodedoc


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var editEmail: EditText
    private lateinit var editPass: EditText
    private lateinit var btnLog: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassWord.text.toString()
            //val merchantStatus : Boolean = true

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(baseContext, "Authentication successful! " + "Uid" + firebaseAuth.currentUser?.uid
                                + firebaseAuth.currentUser?.email,
                            Toast.LENGTH_SHORT).show()
                        val userid : String? = firebaseAuth.currentUser?.uid
                        database = FirebaseDatabase.getInstance().getReference("Users/Merchants")
                        database.child(userid!!).get().addOnSuccessListener {
                            //If a node/entry of that specific UserName exists
                            if(it.exists()){

                                val intent = Intent(this, HomeMerchant::class.java)
                                finish()
                                startActivity(intent)
                            }
                            else{
                                Toast.makeText(baseContext, "CRIT ERROR!",
                                    Toast.LENGTH_SHORT).show()
                            }

                        }.addOnFailureListener(){
                            Toast.makeText(this, "Unable to read the User's data!", Toast.LENGTH_SHORT).show()
                        }
                        database = FirebaseDatabase.getInstance().getReference("Users/Customers")
                        database.child(userid!!).get().addOnSuccessListener {
                            //If a node/entry of that specific UserName exists
                            if(it.exists()){
                                val intent = Intent(this, HomeCustomer::class.java)
                                finish()
                                startActivity(intent)
                            }
                            else{
                                Toast.makeText(baseContext, "CRIT ERROR!!",
                                    Toast.LENGTH_SHORT).show()
                            }

                        }.addOnFailureListener(){
                            Toast.makeText(this, "Unable to read the User's data!", Toast.LENGTH_SHORT).show()
                        }


//                        val user = firebaseAuth.currentUser
//                        user?.let {
//                            val name = user.displayName
//                            val email = user.email
//                            //val account_type =
//                        }


                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

            binding.tvSignUp.setOnClickListener()
            {
                val intent = Intent(this, Registration::class.java)
                startActivity(intent)
            }

    }
}
