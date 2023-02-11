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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassWord.text.toString()

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(baseContext, "Authentication successful! " + "Uid" + firebaseAuth.currentUser?.uid
                            + firebaseAuth.currentUser?.email,
                            Toast.LENGTH_SHORT).show()
//                        val user = firebaseAuth.currentUser
//                        user?.let {
//                            val name = user.displayName
//                            val email = user.email
//                            //val account_type =
//                        }

                        val intent = Intent(this, Profile::class.java)
                        finish()
                        startActivity(intent)

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
