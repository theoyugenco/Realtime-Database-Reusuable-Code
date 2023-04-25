package com.example.realtimedatabasereusuablecodedoc

//import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityHomeCustomerBinding

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityHomeCustomerBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import java.util.*


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

        /*
        Kenneth Valero
        Retrieve the user's current location and record latitude and longitude.
         */
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
        //Kenneth Valero: Initialize recyclerview, searchview, arraylist and adapter
        recyclerView = findViewById(R.id.restaurantSearchRecycler)
        searchView = findViewById(R.id.restaurantSearchBar)
        restaurantList = ArrayList()
        locationList = ArrayList()
        adapter = RestaurantSearchAdapter(this, restaurantList)
        distanceAdapter = DistanceFilterAdapter(this, locationList)

        /*
        Kenneth Valero
        Set button for search filter
         */
        searchDistanceButton = findViewById(R.id.distanceFilter)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        /*
        Kenneth Valero
        Populates arraylist with restaurants in response to any database changes
         */
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
        /*
        Kenneth Valero
        Filters restaurant list based on query
         */
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (recyclerView.adapter == adapter) {
                    filterList(p0)
                }
                else if (recyclerView.adapter == distanceAdapter) {
                    filterDistanceList(p0)
                }
                return true

            }

        })

        /*
        Kenneth Valero
        Sets up list based on distance upon clicking the filter button.
         */
        searchDistanceButton.setOnClickListener {
            recyclerView.adapter = distanceAdapter
            locationList.clear()
            for (i in restaurantList) {
                var currentRestaurantLocation = i
                retrieveDistance(currentRestaurantLocation)
            }
            //Using the distancecomparison function
            Collections.sort(locationList, DistanceComparison())
            distanceAdapter.notifyDataSetChanged()
        }

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
    /*
    Kenneth Valero
    Function to sort the list based on query
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
    /*
    Kenneth Valero
    Sort the distance filter list based on query
     */
    private fun filterDistanceList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<LocationDC>()
            for (i in locationList) {
                if (i.name.toString().lowercase(Locale.ROOT).contains(query.lowercase())) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No data present", Toast.LENGTH_SHORT).show()
            }
            else {
                distanceAdapter.setFilteredDistanceList(filteredList)
            }
        }
    }

    /*
    Kenneth Valero
    Function to retrieve the distance of the current location from
    the user and record it in the location list.
     */
    @Suppress("DEPRECATION")
    private fun retrieveDistance(restaurant: RestaurantDC) {
        var geocoder = Geocoder(this)
        var userLocation = Location("User Location")
        userLocation.latitude = userLat
        userLocation.longitude = userLong
        val geo: GeoApiContext = GeoApiContext.Builder()
            .apiKey("AIzaSyBsE3izG69P7nzQa13Ne0CK8Tfl3SvobQw")
            .build()
        val result = GeocodingApi.geocode(geo, restaurant.streetAddress).await()
        var restaurantLocation = Location("Restaurant Location")
        restaurantLocation.latitude = result[0].geometry.location.lat
        restaurantLocation.longitude = result[0].geometry.location.lng
        var distance: Float = userLocation.distanceTo(restaurantLocation)

        locationList.add(LocationDC(restaurant.name, restaurant.streetAddress, restaurant.city,
            restaurant.state, restaurant.zipcode, restaurant.description, restaurant.restaurantID, distance))
    }

}



