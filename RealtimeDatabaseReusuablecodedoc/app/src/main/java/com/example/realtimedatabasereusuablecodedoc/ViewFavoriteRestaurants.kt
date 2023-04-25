
package com.example.realtimedatabasereusuablecodedoc


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
It is determined that Restaurant, Menu, and Menu Item will have many of its core functionalites and classes shared
Menu is markedly more complex and difficult that Restaurant and Menu Item
However, getting the implementation of Restaurant (and Menu Item) will make considerable progress on Menu by proxy
Also these classes will use adapters
 */

class ViewFavoriteRestaurants : AppCompatActivity() {
    private var TAG: String? = null
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var restaurantMenu: Menu? = null
    private lateinit var rv: RecyclerView
    private lateinit var msAdapter: ViewFavoriteRestaurantsMultiselectAdapter
    private lateinit var restaurantArrayList: ArrayList<RestaurantDC>
    private var stringArrayList: ArrayList<String> = ArrayList<String>()
    //private var restaurantArrayList: ArrayList<RestaurantDC> = ArrayList<RestaurantDC>()
    //var onComplete : (()-> Unit)? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_favorite_restaurants)

        rv = findViewById(R.id.restaurantList)
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
        restaurantArrayList = arrayListOf<RestaurantDC>()
        getUserData()
    }

    //We call the user's data to populate it within the recyclerview
    private fun getUserData(){
        auth = FirebaseAuth.getInstance()
        Log.d(TAG, "user id is: " + auth.currentUser!!.uid)
        database = FirebaseDatabase.getInstance().getReference("Favorites/Favorite Restaurants/"+auth.currentUser!!.uid)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //restaurantArrayList.clear()
                    /*
                        We now get the Restaurant IDs
                    */
                    for (menuItemSnapshot in snapshot.children) {
                        val restaurant = menuItemSnapshot.getValue()
                        Log.d (TAG, "string is: " + restaurant.toString())
                        stringArrayList.add(restaurant!!.toString())
                    }



                    var done : Int = 1

                    /*
                    We then retrieve the restaurant items matching it with the restaurants push id strings
                     */

                    for (i in stringArrayList){
                        done++
                        database = FirebaseDatabase.getInstance().getReference("Restaurants")
                        database.orderByChild("restaurantID").equalTo(i).addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    //restaurantArrayList.clear()
                                    for (menuItemSnapshot in snapshot.children) {
                                        val menuItem = menuItemSnapshot.getValue(RestaurantDC::class.java)
                                        restaurantArrayList.add(menuItem!!)
                                    }
                                    msAdapter =
                                        ViewFavoriteRestaurantsMultiselectAdapter(restaurantArrayList) { show ->
                                            showDeleteMenu(show)
                                        }

                                    rv.adapter = msAdapter
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                            })
                    }
                    /*
                    if (done == stringArrayList.size){
                        msAdapter =
                            ViewFavoriteRestaurantsMultiselectAdapter(restaurantArrayList) { show ->
                                showDeleteMenu(show)
                            }

                        rv.adapter = msAdapter
                    }

                     */



                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        /*
database.addValueEventListener(object : ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) {
        Toast.makeText(this@RestaurantViewEdit, "onDataChanged!", Toast.LENGTH_SHORT).show()
        if (snapshot.exists()) {
            restaurantArrayList.clear()
            for (menuItemSnapshot in snapshot.children) {
                val menuItem = menuItemSnapshot.getValue(RestaurantDC::class.java)
                restaurantArrayList.add(menuItem!!)
            }
            msAdapter =
                LocationViewEditMultiselectAdapter(restaurantArrayList) { show ->
                        showDeleteMenu(show)
                }

            rv.adapter = msAdapter
        }
    }

    override fun onCancelled(error: DatabaseError) {
        TODO("Not yet implemented")
    }
})*/
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        restaurantMenu = menu
        menuInflater.inflate(R.menu.view_edit_menu,restaurantMenu)
        showDeleteMenu(false)
        showEditMenu(false)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.delete -> { delete() }
            /*
            R.id.cart-> {
                val intent = Intent(this, CustomerCheckout::class.java)
                startActivity(intent)
            }
            */
        }
        return super.onOptionsItemSelected(item)
    }

    fun showDeleteMenu(show: Boolean){
        restaurantMenu?.findItem(R.id.delete)?.isVisible = false
    }


    fun showEditMenu(show: Boolean){
        restaurantMenu?.findItem(R.id.edit)?.isVisible = false
    }


    //Accessing methods in the adapter
    private fun delete(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Delete Location(s)")
        alertDialog.setMessage("Do you want to delete these locations?")
        alertDialog.setPositiveButton("Delete"){_,_ ->
            msAdapter.deleteSelectedItem()
            showDeleteMenu(false)
        }
        alertDialog.setNegativeButton("Cancel"){_,_ ->}
        alertDialog.show()
    }
}
