package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private lateinit var recyclerView: RecyclerView
private lateinit var searchView: SearchView
private lateinit var restaurantList: ArrayList<LocationDC>
private lateinit var adapter: ReviewRestaurantAdapter
private lateinit var firebaseAuth: FirebaseAuth
private lateinit var database: DatabaseReference
class ReviewSearch : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_search)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference()
    }
}