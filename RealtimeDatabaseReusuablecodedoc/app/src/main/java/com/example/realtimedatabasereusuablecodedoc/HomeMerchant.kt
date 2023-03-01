package com.example.realtimedatabasereusuablecodedoc

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityHomeMerchantBinding
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

/*
Theodore Yu
The home page for the Merchant
 */

class HomeMerchant : AppCompatActivity() {
    private lateinit var chatButton: ImageView
    private lateinit var binding: ActivityHomeMerchantBinding
    private lateinit var profButton: ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_merchant)

        binding = ActivityHomeMerchantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        Theodore Yu
        Takes us to Chatting Functionality
         */

        chatButton = findViewById(R.id.messagingButton)
        chatButton.setOnClickListener {
            val intent = Intent(this, ChatSearch::class.java)
            startActivity(intent)
        }

        /*
        Theodore Yu
        Takes the User to their Profile
         */
        profButton = findViewById(R.id.profileButton)
        profButton.setOnClickListener {
            val intent = Intent(this, MerchantProfile::class.java)
            startActivity(intent)
        }
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
}