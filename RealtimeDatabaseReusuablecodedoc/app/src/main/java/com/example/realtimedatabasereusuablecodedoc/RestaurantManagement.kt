package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityRestaurantManagementBinding
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import android.content.Intent
import android.provider.ContactsContract.Data
import android.widget.TextView
import com.google.firebase.database.*

class RestaurantManagement : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantManagementBinding
    private lateinit var btnAddRestaurant: Button
    private lateinit var database: DatabaseReference
    private var restaurantArrayList: ArrayList<RestaurantDC> = ArrayList<RestaurantDC>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_management)
        database = FirebaseDatabase.getInstance().getReference("Users/Restaurants/")


        binding = ActivityRestaurantManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnAddRestaurant.setOnClickListener() {
            val intent = Intent(this, RestaurantAdd::class.java)
            startActivity(intent)
        }

        binding.btnViewEditRestaurant.setOnClickListener() {
            val intent = Intent(this, RestaurantViewEdit::class.java)
            //intent.putExtra("key",restaurantArrayList);
            startActivity(intent)
        }


    }

    private fun getUserData(){
        database = FirebaseDatabase.getInstance().getReference("Users/Restaurants/")
        //Toast.makeText(this@RestaurantViewEdit, "Did getUser at least run?", Toast.LENGTH_SHORT).show()

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    //Toast.makeText(this@RestaurantViewEdit, "Did getUser at least run?", Toast.LENGTH_SHORT).show()
                    for (menuItemSnapshot in snapshot.children){
                        val menuItem = menuItemSnapshot.getValue(RestaurantDC::class.java)
                        Toast.makeText(this@RestaurantManagement, menuItem!!.name.toString(), Toast.LENGTH_SHORT).show()
                        restaurantArrayList.add(menuItem!!)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}