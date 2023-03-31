
package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private lateinit var auth: FirebaseAuth
    private lateinit var msAdapter: MenuAddMenuItemViewAddMultiselectAdapter
    private lateinit var rv: RecyclerView
    private var restaurantMenu: Menu? = null
    //private var rid: String =
    private var id: String = "NRlZ5uQ6IJe8ogud6Fs"
    //private lateinit var menuItemArrayList: ArrayList<MenuItemDC>
    //private lateinit var menuItemAdapter: MultiselectAdapter
    //private lateinit var binding:

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_item_view_edit)
        menuItemRecyclerView = findViewById(R.id.menuItemList)
        menuItemRecyclerView.layoutManager = LinearLayoutManager(this)
        menuItemRecyclerView.setHasFixedSize(true)

        /*
        val bundle = intent.extras
        //var id : String = ""
        if (bundle != null){
            id = bundle.getString("key")!!
        }
        */

        //itemSelectedList = ArrayList<String>()
        menuItemArrayList = arrayListOf<MenuItemDC>()
        getAllMerchantMenuItems()
        getMenuItems()

    }

    private fun getMenuItems(){
        Toast.makeText(
            this@MenuCustomerOrdering,
            "at least!!",
            Toast.LENGTH_SHORT
        ).show()
        database = FirebaseDatabase.getInstance().getReference("Menus/"+id+"/Menu Items")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                Toast.makeText(
                    this@MenuCustomerOrdering,
                    "yohooo!",
                    Toast.LENGTH_SHORT
                ).show()
                if (snapshot.exists()) {
                    Toast.makeText(
                        this@MenuCustomerOrdering,
                        "comeon!",
                        Toast.LENGTH_SHORT
                    ).show()
                    //restaurantArrayList.clear()
                    for (menuItemSnapshot in snapshot.children) {
                        val menuItem = menuItemSnapshot.getValue(MenuItemDC::class.java)
                        currentMenuArrayList.add(menuItem!!.menuItemID.toString())
                    }
                    for (i in menuItemArrayList){
                        val iID : String = i.menuItemID.toString()
                        for (j in currentMenuArrayList){
                            //Toast.makeText(this, "calgery", Toast.LENGTH_SHORT).show()
                            if (iID.equals(j)){
                                //If we can, we would try to remove j
                                //No duplicate menu items ideally!

                                currentMenuItemArrayList.add(i)
                                //Toast.makeText(this, "current is: " + iID, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    /*
                    msAdapter =
                        MenuAddMenuItemViewAddMultiselectAdapter(menuItemArrayList, id, itemSelectedList) { show ->
                            showCartMenu(show)
                        }

                    menuItemRecyclerView.adapter = msAdapter

                     */
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
            Toast.makeText(
                this@MenuCustomerOrdering,
                "onDataChanged!",
                Toast.LENGTH_SHORT
            ).show()
            if (snapshot.exists()) {
                //restaurantArrayList.clear()
                for (menuItemSnapshot in snapshot.children) {
                    val menuItem = menuItemSnapshot.getValue(MenuItemDC::class.java)
                    menuItemArrayList.add(menuItem!!)
                    Toast.makeText(this@MenuCustomerOrdering, "rangers lead teh way!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
        })
        /*
        We now get the Menu Item IDs of the current/ACTIVE Menu of this restaurant
        The Menu ID of the current/ACTIVE Menu should already be provided.
         */



        /*
        suboptimal O(n^2) searching algorithm
        However, the amount of Menu Items a merchant should have should be relatively low
        We cut down a LOT of items by honing in only the Menu Items the Merchant of the current Restaurant has
        */

        Toast.makeText(this, "spearhead", Toast.LENGTH_SHORT).show()
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
        alertDialog.setTitle("Add Menu Items to current Menu?")
        alertDialog.setMessage("Would you want to add these Menu Items to the current Menu?")
        alertDialog.setPositiveButton("Create"){_,_ ->
            msAdapter.addSelectedItem()
            showCartMenu(false)
            val intent = Intent(this, MenuAddRestaurantViewAdd::class.java)
            intent.putExtra("key", id)
            startActivity(intent)
        }
        alertDialog.setNegativeButton("Cancel"){_,_ ->}
        alertDialog.show()
    }
}
