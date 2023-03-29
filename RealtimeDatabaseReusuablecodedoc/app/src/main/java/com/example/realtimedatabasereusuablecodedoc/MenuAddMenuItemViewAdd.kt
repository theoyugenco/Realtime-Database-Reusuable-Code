
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


class MenuAddMenuItemViewAdd : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var menuItemRecyclerView: RecyclerView
    private lateinit var menuItemArrayList: ArrayList<MenuItemDC>
    private var itemSelectedList : ArrayList<String> = ArrayList<String>()
    private lateinit var auth: FirebaseAuth
    private lateinit var msAdapter: MenuAddMenuItemViewAddMultiselectAdapter
    private lateinit var rv: RecyclerView
    private var restaurantMenu: Menu? = null
    private var id: String = ""
    //private lateinit var menuItemArrayList: ArrayList<MenuItemDC>
    //private lateinit var menuItemAdapter: MultiselectAdapter
    //private lateinit var binding:

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_item_view_edit)
        menuItemRecyclerView = findViewById(R.id.menuItemList)
        menuItemRecyclerView.layoutManager = LinearLayoutManager(this)
        menuItemRecyclerView.setHasFixedSize(true)

        val bundle = intent.extras
        //var id : String = ""
        if (bundle != null){
            id = bundle.getString("key")!!
        }


        //itemSelectedList = ArrayList<String>()
        menuItemArrayList = arrayListOf<MenuItemDC>()
        getUserData()

    }

    private fun getUserData() {
        database = FirebaseDatabase.getInstance().getReference("Menu Items/")
        auth = FirebaseAuth.getInstance()
        database.orderByChild("ownerUID").equalTo(auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener{//
                override fun onDataChange(snapshot: DataSnapshot) {
                    menuItemArrayList.clear()
                    Toast.makeText(
                        this@MenuAddMenuItemViewAdd,
                        "onDataChanged!",
                        Toast.LENGTH_SHORT
                    ).show()
                    if (snapshot.exists()) {
                        //restaurantArrayList.clear()
                        for (menuItemSnapshot in snapshot.children) {
                            val menuItem = menuItemSnapshot.getValue(MenuItemDC::class.java)
                            menuItemArrayList.add(menuItem!!)
                        }
                        msAdapter =
                            MenuAddMenuItemViewAddMultiselectAdapter(menuItemArrayList, id, itemSelectedList) { show ->
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

    private fun add(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Add Menu Items to current Menu?")
        alertDialog.setMessage("Would you want to add these Menu Items to the current Menu?")
        alertDialog.setPositiveButton("Create"){_,_ ->
            msAdapter.addSelectedItem()
            showCartMenu(false)
        }
        alertDialog.setNegativeButton("Cancel"){_,_ ->}
        alertDialog.show()
    }
}
