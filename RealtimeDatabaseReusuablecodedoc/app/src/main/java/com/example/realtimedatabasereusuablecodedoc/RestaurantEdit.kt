/*
package com.example.realtimedatabasereusuablecodedoc

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class RestaurantEdit : AppCompatActivity() {
    private var TAG: String? = null
    private lateinit var database: DatabaseReference
    private var restaurantMenu: Menu? = null
    private lateinit var rv: RecyclerView
    private lateinit var msAdapter: LocationViewEditMultiselectAdapter
    private lateinit var restaurantArrayList: ArrayList<RestaurantDC>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_edit)

        rv = findViewById(R.id.restaurantList)
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
        restaurantArrayList = arrayListOf<RestaurantDC>()
        getUserData()
    }



    private fun getUserData(){
        database = FirebaseDatabase.getInstance().getReference("Users/Restaurants/")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                restaurantArrayList.clear()
                if (snapshot.exists()) {
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
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        restaurantMenu = menu
        menuInflater.inflate(R.menu.view_edit_menu,restaurantMenu)
        showDeleteMenu(false)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.delete -> { delete() }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showDeleteMenu(show: Boolean){
        restaurantMenu?.findItem(R.id.delete)?.isVisible = show
    }


    fun showEditMenu(show: Boolean){
        restaurantMenu?.findItem(R.id.edit)?.isVisible = show
    }

}

 */