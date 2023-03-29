package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class RestaurantSearch : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var restaurantList: ArrayList<RestaurantDC>
    private lateinit var adapter: RestaurantSearchAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_search)
        //Initialize database instances
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference()

        //Initialize recyclerview, searchview, arraylist and adapter
        recyclerView = findViewById(R.id.restaurantRecycler)
        searchView = findViewById(R.id.restaurantSearchBar)
        restaurantList = ArrayList()
        adapter = RestaurantSearchAdapter(this, restaurantList)

        //setup layout for recyclerview and connect adapter to recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        //Populates arraylist with restaurants in response to any database changes
        database.child("Restaurants").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                restaurantList.clear()
                for(postSnapshot in snapshot.children) {
                    val currentRestaurant = postSnapshot.getValue(RestaurantDC::class.java)
                    restaurantList.add(currentRestaurant!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        //Filters the recyclerview based on the search query
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                filterList(p0)
                return true
            }

        })
    }

    /*
    Filters the list and makes sure only restaurants whose names contain
    the search query are included in the arraylist
     */
    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<RestaurantDC>()
            for (i in restaurantList) {
                if (i.name.toString().lowercase(Locale.ROOT).contains(query.lowercase())) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No data present", Toast.LENGTH_SHORT).show()
            }
            else {
                adapter.setFilteredList(filteredList)
            }
        }
    }
}