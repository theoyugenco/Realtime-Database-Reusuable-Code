package com.example.realtimedatabasereusuablecodedoc

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
//import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityHomeCustomerBinding
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityHomeCustomerBinding
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

/*
Theodore Yu
The home page for the Merchant
 */

class HomeCustomer : AppCompatActivity() {

    private lateinit var chatButton: ImageButton
    private lateinit var binding: ActivityHomeCustomerBinding
    private lateinit var profButton: ImageButton
    private lateinit var favoriteButton: Button
    private lateinit var orderButton: Button
    private lateinit var reviewButton: ImageView
    private lateinit var homeButton: ImageButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var restaurantList: ArrayList<RestaurantDC>
    private lateinit var locationList: ArrayList<LocationDC>
    private lateinit var adapter: RestaurantSearchAdapter
    private lateinit var distanceAdapter: DistanceFilterAdapter
    private lateinit var searchDistanceButton: Button
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var userLat: Double = 0.0
    private var userLong: Double = 0.0

    private lateinit var rollDice : Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_customer)

        binding = ActivityHomeCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (applicationContext.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    if (it != null) {
                        userLat = it.latitude
                        userLong = it.longitude
                    }
                }
            }
        }
        //Initialize recyclerview, searchview, arraylist and adapter
        recyclerView = findViewById(R.id.restaurantSearchRecycler)
        searchView = findViewById(R.id.restaurantSearchBar)
        restaurantList = ArrayList()
        locationList = ArrayList()
        adapter = RestaurantSearchAdapter(this, restaurantList)
        distanceAdapter = DistanceFilterAdapter(this, locationList)

        searchDistanceButton = findViewById(R.id.distanceFilter)

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

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (recyclerView.adapter == adapter) {
                    filterList(p0)
                }
                /*
                else if (recyclerView.adapter == distanceAdapter) {
                    filterDistanceList(p0)
                }
                 */
                return true

            }

        })
        /*
        Theodore Yu
        Takes us to Chatting Functionality
         */
        chatButton = findViewById(R.id.message_btn)
        chatButton.setOnClickListener {
            val intent = Intent(this, ChatSearch::class.java)
            startActivity(intent)
        }

        /*
        Theodore Yu
        Takes the User to their Profile
         */
        profButton = findViewById(R.id.profile_btn)

        profButton.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        favoriteButton= findViewById(R.id.favorite)

        favoriteButton.setOnClickListener{
            val intent = Intent(this, ChooseFavorite::class.java)
            startActivity(intent)
        }


        orderButton = findViewById(R.id.ch_orders_btn)
        orderButton.setOnClickListener() {
            val intent = Intent(this, OrdersOptions::class.java)
            startActivity(intent)
        }


        homeButton = findViewById(R.id.home_btn)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeCustomer::class.java)
            startActivity(intent)
        }

        rollDice = findViewById(R.id.ch_roll_dice)
        rollDice.setOnClickListener {
            val intent = Intent(this, DiceRoll::class.java)
            startActivity(intent)
        }
        /*
        reviewButton = findViewById(R.id.reviewButton)
        reviewButton.setOnClickListener {
            val intent = Intent(this, ReviewSearch::class.java)
            startActivity(intent)
        }
         */
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



