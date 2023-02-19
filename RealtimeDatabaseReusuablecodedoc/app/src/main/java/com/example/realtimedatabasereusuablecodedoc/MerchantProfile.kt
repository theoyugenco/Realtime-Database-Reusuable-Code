package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast


/*
theodore Yu
Where Merchants can custom their profile and retrieve personal information
 */

class MerchantProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merchant_profile)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.merchant_item,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_location->{
                val intent = Intent(this, ItemLocation::class.java)
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
                val intent = Intent(this, ItemMenuItem::class.java)
                startActivity(intent)
                Toast.makeText(this, "Menu Item Clicked", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}