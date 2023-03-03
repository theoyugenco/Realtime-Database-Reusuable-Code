/*package com.example.realtimedatabasereusuablecodedoc

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

private lateinit var recyclerView: RecyclerView
private lateinit var searchView: SearchView
private lateinit var restaurantList: ArrayList<RestaurantDC>
private lateinit var adapter: ReviewRestaurantAdapter
private lateinit var firebaseAuth: FirebaseAuth
private lateinit var database: DatabaseReference
class ReviewSearch : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_search)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference()

        recyclerView = findViewById(R.id.restaurantSearchRecycler)
        searchView = findViewById(R.id.reviewSearchBar)
        restaurantList = ArrayList()
        adapter = ReviewRestaurantAdapter(this, restaurantList)

        //setup layout for recyclerview and connect adapter to recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        database.child("Users/Restaurants").addValueEventListener(object: ValueEventListener {
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
*/