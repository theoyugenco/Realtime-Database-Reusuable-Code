package com.example.realtimedatabasereusuablecodedoc

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.io.IOException
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

/*
Kenneth Valero
Activity for searching for a restaurant the user wants to order from. Clicking on a restaurant
will let the user order from that restaurant.
 */
class RestaurantSearch : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var restaurantList: ArrayList<RestaurantDC>
    private lateinit var locationList: ArrayList<LocationDC>
    private lateinit var adapter: RestaurantSearchAdapter
    private lateinit var distanceAdapter: DistanceFilterAdapter
    private lateinit var searchDistanceButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var userLat: Double = 0.0
    private var userLong: Double = 0.0
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_search)
        //Initialize database instances
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

        /*
        searchDistanceButton.setOnClickListener {
            recyclerView.adapter = distanceAdapter
            retrieveDistance()
            distanceAdapter.notifyDataSetChanged()
        }
        */

        //Filters the recyclerview based on the search query
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
    /*
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

    private fun retrieveDistance() {
        var geocoder: Geocoder = Geocoder(this)
        var addressList: List<Address>
        locationList.clear()
        try {
            for (i in restaurantList) {
                addressList = geocoder.getFromLocationName(i.streetAddress.toString(), 1) as List<Address>

                var userLocation: Location = Location("User Location")
                userLocation.latitude = userLat
                userLocation.longitude = userLong
                var restaurantLocation: Location = Location("Restaurant Location")

                if (addressList != null) {
                    var restaurantLat: Double = addressList.get(0).latitude
                    var restaurantLong: Double = addressList.get(0).longitude

                    restaurantLocation.latitude = restaurantLat
                    restaurantLocation.longitude = restaurantLong

                    var distance: Float = userLocation.distanceTo(restaurantLocation)

                    locationList.add(LocationDC(i.name, i.streetAddress, i.city, i.state,
                    i.zipcode, i.description, i.restaurantID, distance))
                }
            }
            Collections.sort(locationList, DistanceComparison())
        } catch (e: IOException) {

        }
    }
    */
}