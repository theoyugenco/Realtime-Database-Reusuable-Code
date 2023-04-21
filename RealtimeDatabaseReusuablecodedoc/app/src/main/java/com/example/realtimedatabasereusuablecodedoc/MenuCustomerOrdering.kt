
package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MenuCustomerOrdering : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var menuItemRecyclerView: RecyclerView
    private lateinit var menuItemArrayList: ArrayList<MenuItemDC>
    private var currentMenuArrayList: ArrayList<String> = ArrayList<String>()
    private var currentMenuItemArrayList: ArrayList<MenuItemDC> = ArrayList<MenuItemDC>()
    private var itemSelectedList : ArrayList<String> = ArrayList<String>()
    private var menuItemPriceArrayList: ArrayList<String> = ArrayList<String>()
    private lateinit var auth: FirebaseAuth
    private lateinit var msAdapter: MenuCustomerOrderingMultiselectAdapter
    private lateinit var rv: RecyclerView
    private var restaurantMenu: Menu? = null
    //private var rid: String =
    private var TAG: String? = null
    private var id: String = "-NRlZ5uQ6IJe8ogud6Fs"
    private var rid: String? = null
    //private lateinit var menuItemArrayList: ArrayList<MenuItemDC>
    //private lateinit var menuItemAdapter: MultiselectAdapter
    //private lateinit var binding:

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_customer_ordering)
        menuItemRecyclerView = findViewById(R.id.menuItemList)
        menuItemRecyclerView.layoutManager = LinearLayoutManager(this)
        menuItemRecyclerView.setHasFixedSize(true)


        val bundle = intent.extras
        //var id : String = ""
        if (bundle != null){
            rid = bundle.getString("restaurantID")!!
        }

        database = FirebaseDatabase.getInstance().getReference("Restaurants/")
        database.child(rid!!).get().addOnSuccessListener {
            if (it.exists()){
                Log.d(TAG, "MENU RETRIEVED SUCCESSFULLY")
                id = it.child("activeMenu").value.toString()
            }
        }


        //itemSelectedList = ArrayList<String>()
        menuItemArrayList = arrayListOf<MenuItemDC>()
        getAllMerchantMenuItems()
        getMenuItems()

    }

    private fun getMenuItems(){
        //Toast.makeText( this@MenuCustomerOrdering, "at least!!", Toast.LENGTH_SHORT ).show()
        database = FirebaseDatabase.getInstance().getReference("Menus/"+id+"/Menu Items/")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //restaurantArrayList.clear()
                    /*
                        We now get the Menu Item IDs of the current/ACTIVE Menu of this restaurant
                        The Menu ID of the current/ACTIVE Menu should already be provided.
                    */
                    for (menuItemSnapshot in snapshot.children) {
                        val menuItem = menuItemSnapshot.getValue()
                        Log.d (TAG, "string is: " + menuItem.toString())
                        currentMenuArrayList.add(menuItem!!.toString())
                    }

                    /*
                    suboptimal O(n^2) searching algorithm
                    However, the amount of Menu Items a merchant should have should be relatively low
                    We cut down a LOT of items by honing in only the Menu Items the Merchant of the current Restaurant has
                    */
                    for (i in menuItemArrayList){
                        //Log.d (TAG, "compare")
                        val iID : String = i.menuItemID.toString()
                        for (j in currentMenuArrayList){
                            if (iID.equals(j)){
                                //If we can, we would try to remove j
                                //No duplicate menu items ideally!

                                currentMenuItemArrayList.add(i)
                            }
                        }
                    }

                    msAdapter =
                        MenuCustomerOrderingMultiselectAdapter(currentMenuItemArrayList, id, itemSelectedList, menuItemPriceArrayList) { show ->
                            showCartMenu(show)
                        }

                    menuItemRecyclerView.adapter = msAdapter


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getAllMerchantMenuItems() {
        /*
        Unlike the Restaurant/Menu/Menu Item Add/View/Edit/Delete + Hierarchy for the Merchant
        There is a many to many relationship between Menu and Menu Items that we must be conscious of
        We must find gather all the Menu Items (of the Merchant of the current Restaurant)
        Then we must find which of those Menu Items are apart of the ACTIVE menu
         */

        database = FirebaseDatabase.getInstance().getReference("Menu Items/")
        auth = FirebaseAuth.getInstance()
        database.orderByChild("ownerUID").equalTo("2T2gX0AVHPStGhVKoJd8lsEkcnB2").addValueEventListener(object : ValueEventListener{//
        override fun onDataChange(snapshot: DataSnapshot) {
            menuItemArrayList.clear()
            if (snapshot.exists()) {
                //restaurantArrayList.clear()
                for (menuItemSnapshot in snapshot.children) {
                    val menuItem = menuItemSnapshot.getValue(MenuItemDC::class.java)
                    menuItemArrayList.add(menuItem!!)
                    Log.d (TAG, "run prior")
                }
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
                // val intent = Intent(this, MenuAddRestaurantViewAdd::class.java)
                // intent.putExtra("key", id)
                //startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showCartMenu(show: Boolean){
        restaurantMenu?.findItem(R.id.cart)?.isVisible = show
    }

    private fun add(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Checkout")
        alertDialog.setMessage("Would you like to proceed to Checkout?")
        alertDialog.setPositiveButton("Yes"){_,_ ->
            //msAdapter.addSelectedItem()
            showCartMenu(false)
            /*
            val intent = Intent(context, CheckoutActivity::class.java)
            intent.putStringArrayListExtra("items", itemNames)
            context.startActivity(intent)
            */


            val intent = Intent(this, CustomerCheckout::class.java)
            intent.putStringArrayListExtra("itemName", itemSelectedList)
            intent.putStringArrayListExtra("itemPrice", menuItemPriceArrayList)
            for (i in itemSelectedList){
                Log.d(TAG, "Name: " + i)
            }
            for (j in menuItemPriceArrayList){
                Log.d(TAG, "Price " + j)
            }
            startActivity(intent)

        }
        alertDialog.setNegativeButton("Cancel"){_,_ ->}
        alertDialog.show()
    }
}
