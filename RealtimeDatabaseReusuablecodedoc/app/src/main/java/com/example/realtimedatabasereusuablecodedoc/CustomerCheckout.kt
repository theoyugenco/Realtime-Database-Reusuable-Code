package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CustomerCheckout : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var restaurantMenu: Menu? = null
    private lateinit var recyclerv: RecyclerView
    private lateinit var msAdapter: CustomerCheckoutAdapter
    private lateinit var restaurantArrayList: ArrayList<CustomerCheckoutDC>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_checkout)

        recyclerv = findViewById(R.id.cc_cartItemsRecycler)
        recyclerv.layoutManager = LinearLayoutManager(this)
        recyclerv.setHasFixedSize(true)
        restaurantArrayList = arrayListOf<CustomerCheckoutDC>()
        getUserData()
    }


    private fun getUserData() {
        database = FirebaseDatabase.getInstance().getReference("Restaurants/")
        auth = FirebaseAuth.getInstance()
        database.orderByChild("merchantID").equalTo(auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    restaurantArrayList.clear()
                    //Toast.makeText(this@RestaurantViewEdit, "onDataChanged!", Toast.LENGTH_SHORT)
                        //.show()
                    if (snapshot.exists()) {
                        for (menuItemSnapshot in snapshot.children) {
                            val menuItem = menuItemSnapshot.getValue(CustomerCheckoutDC::class.java)
                            restaurantArrayList.add(menuItem!!)
                        }

                        msAdapter =
                            CustomerCheckoutAdapter(restaurantArrayList) { show ->
                                showDeleteMenu(show)
                            }

                        recyclerv.adapter = msAdapter
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun showDeleteMenu(show: Boolean){
        restaurantMenu?.findItem(R.id.delete)?.isVisible = show
    }
}