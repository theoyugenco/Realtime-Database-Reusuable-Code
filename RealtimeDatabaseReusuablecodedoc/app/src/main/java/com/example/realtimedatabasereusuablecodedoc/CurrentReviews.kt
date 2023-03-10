package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.Locale
/*
Kenneth Valero:
This activity is where past reviews for the restaurant the user
is currently viewing will be displayed.
 */
class CurrentReviews : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewList: ArrayList<ReviewDC>
    private lateinit var adapter: ReviewAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_reviews)

        //initialize firebase auth and reference
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference()

        //Retrieve variables from Review activity
        val restaurantID = intent.getStringExtra("restaurantID")

        //initialize views, list, and user adapter
        recyclerView = findViewById(R.id.reviewRecyclerView)
        reviewList = ArrayList()
        adapter = ReviewAdapter(this, reviewList)

        //setup layout for recyclerview and connect adapter to recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        //Populate the review array with reviews for the current restaurant being viewed
        database.child("Reviews").addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    reviewList.clear()
                    for (postSnapshot in snapshot.children) {
                        val currentReview = postSnapshot.getValue(ReviewDC::class.java)
                        if(restaurantID == currentReview?.restaurantID) {
                            reviewList.add(currentReview!!)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}