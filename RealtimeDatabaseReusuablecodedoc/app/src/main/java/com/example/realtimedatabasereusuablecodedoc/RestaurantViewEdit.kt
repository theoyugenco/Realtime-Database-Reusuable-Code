
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
It is determined that Restaurant, Menu, and Menu Item will have many of its core functionalites and classes shared
Menu is markedly more complex and difficult that Restaurant and Menu Item
However, getting the implementation of Restaurant (and Menu Item) will make considerable progress on Menu by proxy
Also these classes will use adapters
 */

class RestaurantViewEdit : AppCompatActivity() {
    private var TAG: String? = null
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var restaurantMenu: Menu? = null
    private lateinit var rv: RecyclerView
    private lateinit var msAdapter: LocationViewEditMultiselectAdapter
    private lateinit var restaurantArrayList: ArrayList<RestaurantDC>
    //private var restaurantArrayList: ArrayList<RestaurantDC> = ArrayList<RestaurantDC>()
    //var onComplete : (()-> Unit)? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_view_edit)

        rv = findViewById(R.id.restaurantList)
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
        restaurantArrayList = arrayListOf<RestaurantDC>()
        getUserData()
    }

    //We call the user's data to populate it within the recyclerview
    private fun getUserData(){
        database = FirebaseDatabase.getInstance().getReference("Restaurants/")
        auth = FirebaseAuth.getInstance()
        database.orderByChild("merchantID").equalTo(auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                restaurantArrayList.clear()
                Toast.makeText(this@RestaurantViewEdit, "onDataChanged!", Toast.LENGTH_SHORT).show()
                if (snapshot.exists()) {
                    //restaurantArrayList.clear()
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
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.delete -> { delete() }

            R.id.cart-> {
                val intent = Intent(this, CustomerCheckout::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showDeleteMenu(show: Boolean){
        restaurantMenu?.findItem(R.id.delete)?.isVisible = show
    }


    fun showEditMenu(show: Boolean){
        restaurantMenu?.findItem(R.id.edit)?.isVisible = show
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
