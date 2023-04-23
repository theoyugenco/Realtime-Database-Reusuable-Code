package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.util.Log

class ActiveRestaurantRecycler : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var restaurantRecyclerView: RecyclerView
    private lateinit var restaurantArrayList: ArrayList<RestaurantDC>
    private var itemSelectedList : ArrayList<String> = ArrayList<String>()
    private lateinit var auth: FirebaseAuth
    private lateinit var msAdapter: ActiveRestaurantRecyclerSingleselectAdapter
    private lateinit var rv: RecyclerView
    private var restaurantMenu: Menu? = null
    private var id: String = ""
    private var TAG: String? = null
    //private lateinit var menuItemArrayList: ArrayList<MenuItemDC>
    //private lateinit var menuItemAdapter: MultiselectAdapter
    //private lateinit var binding:

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_restaurant_recycler)
        restaurantRecyclerView = findViewById(R.id.restaurantList)
        restaurantRecyclerView.layoutManager = LinearLayoutManager(this)
        restaurantRecyclerView.setHasFixedSize(true)

        val bundle = intent.extras
        //var id : String = ""
        if (bundle != null){
            id = bundle.getString("key")!!
        }


        //itemSelectedList = ArrayList<String>()
        restaurantArrayList = arrayListOf<RestaurantDC>()
        getUserData()

    }

    private fun getUserData() {
        database = FirebaseDatabase.getInstance().getReference("Restaurants/")
        auth = FirebaseAuth.getInstance()
        database.orderByChild("merchantID").equalTo(auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener{//
        override fun onDataChange(snapshot: DataSnapshot) {
            restaurantArrayList.clear()
            Toast.makeText(
                this@ActiveRestaurantRecycler,
                "onDataChanged!",
                Toast.LENGTH_SHORT
            ).show()
            if (snapshot.exists()) {
                Log.d(TAG, "how many times")
                //restaurantArrayList.clear()
                for (restaurantSnapshot in snapshot.children) {
                    val restaurant = restaurantSnapshot.getValue(RestaurantDC::class.java)
                    restaurantArrayList.add(restaurant!!)
                }
                msAdapter =
                    ActiveRestaurantRecyclerSingleselectAdapter(restaurantArrayList, id, itemSelectedList) { show ->
                        showCartMenu(show)
                    }

                restaurantRecyclerView.adapter = msAdapter
            }
        }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        restaurantMenu = menu
        menuInflater.inflate(R.menu.cart_menu,restaurantMenu)
        showCartMenu(false)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){

            R.id.cart-> {
                add()
                //val intent = Intent(this, CustomerCheckout::class.java)
                //intent.putExtra("itemSelectedList", itemSelectedList)
                //startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showCartMenu(show: Boolean){
        restaurantMenu?.findItem(R.id.cart)?.isVisible = show
    }

    //This runs to confirm the list of Restaurants will use this menu as well.
    private fun add(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Add the current Menu to Restaurants?")
        alertDialog.setMessage("Would you want to add this current Menu to the selected Restaurants?")
        alertDialog.setPositiveButton("Create"){_,_ ->
            var restaurantID: String? = msAdapter.addSelectedItem()
            showCartMenu(false)
            val intent = Intent(this, ActiveMenuRecycler::class.java)
            intent.putExtra("rID", restaurantID!!)
            startActivity(intent)
        }
        alertDialog.setNegativeButton("Cancel"){_,_ ->}
        alertDialog.show()
    }
}