package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityMerchantProfileBinding
import com.google.firebase.storage.FirebaseStorage
import org.w3c.dom.Text
import java.io.File

/*
theodore Yu
Where Merchants can custom their profile and retrieve personal information
 */

class MerchantProfile : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMerchantProfileBinding
    private var TAG: String? = null
    private lateinit var tvFName: TextInputLayout
    private lateinit var tvUName: TextInputLayout
    private lateinit var tvPWord: TextInputLayout
    private lateinit var tvEmail: TextInputLayout
    private lateinit var tvPhone: TextInputLayout
    private lateinit var tvAddress: TextInputLayout
    private lateinit var tvPayment: TextInputLayout

    private lateinit var tvNameLabel: TextView
    private lateinit var tvPWordLabel: TextView


    //private lateinit var fullName: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merchant_profile)

        binding = ActivityMerchantProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        tvFName = findViewById(R.id.tvFName)
        tvUName = findViewById(R.id.tvUName)
        tvPWord = findViewById(R.id.tvPWord)
        tvEmail = findViewById(R.id.tvEmail)
        tvPhone = findViewById(R.id.tvPhone)
        tvAddress = findViewById(R.id.tvAddress)
        tvPayment = findViewById(R.id.tvPayment)

        //database = FirebaseDatabase.getInstance().getReference("Users/Merchants")

        readData()
        retrieveImage()
        /*
        auth = FirebaseAuth.getInstance()
        val uid: String? = auth.currentUser!!.uid

        val storage = FirebaseStorage.getInstance().reference.child("Restaurants/$uid.jpg")

        val merchantProfileImageFile = File.createTempFile("merchantProfileImage", "jpg")
        storage.getFile(merchantProfileImageFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(merchantProfileImageFile.absolutePath)
            binding.userImage.setImageBitmap(bitmap)
        }.addOnFailureListener{
            Toast.makeText(this, "Failed to retrieve Merchant's Profile Picture.", Toast.LENGTH_SHORT).show()
        }
        */

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.merchant_item,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_location->{
                val intent = Intent(this, RestaurantManagement::class.java)
                startActivity(intent)
                Toast.makeText(this, "Location Clicked", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.menu_menu-> {
                val intent = Intent(this, ItemMenu::class.java)
                startActivity(intent)
                Toast.makeText(this,"Menu Clicked", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.menu_item->{
                val intent = Intent(this, MenuItemManagement::class.java)
                startActivity(intent)
                Toast.makeText(this, "Menu Item Clicked", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun retrieveImage(){



        auth = FirebaseAuth.getInstance()
        val uid: String? = auth.currentUser!!.uid
        //Log.d(TAG, "uid is: " + uid)
        val storage = FirebaseStorage.getInstance().reference.child("Merchants/"+uid+".jpg")
        //Log.d(TAG, "storage is: " + storage)
        val merchantProfileImageFile = File.createTempFile("merchantProfileImage", "jpg")
        if (merchantProfileImageFile.exists()){
            //Log.d(TAG, "jacked up and good to go")
            storage.getFile(merchantProfileImageFile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(merchantProfileImageFile.absolutePath)
                binding.userImage.setImageBitmap(bitmap)
            }.addOnFailureListener{
                Toast.makeText(this, "Failed to retrieve Merchant's Profile Picture.", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            //Log.d(TAG, "no bingo")
        }

    }

    private fun readData() {
        auth = FirebaseAuth.getInstance()
        val uid: String? = auth.currentUser!!.uid
        database = FirebaseDatabase.getInstance().getReference("Users/Merchants")
        database.child(uid!!).get().addOnSuccessListener {
            //If a node/entry of that specific UserName exists
            if(it.exists()){
                val fName: String? = it.child("fname").value.toString()
                val lName: String? = it.child("lname").value.toString()
                val fullName = fName + " " + lName
                val uName: String? = it.child("uname").value.toString()
                val pWord:String? = it.child("pword").value.toString()
                val email: String? = it.child("email").value.toString()



                Toast.makeText(this, "Successfully retrieved and read User's data!", Toast.LENGTH_SHORT).show()
                tvFName.getEditText()!!.setText(fullName)
                tvUName.getEditText()!!.setText(uName)
                tvPWord.getEditText()!!.setText(pWord)
                tvEmail.getEditText()!!.setText(email)


            //binding.tvName.text.clear()
                //binding.tvName.= fName.toString()
                //binding.tvLastName.text = lName.toString()
                //binding.tvPassWord.text = age.toString()


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