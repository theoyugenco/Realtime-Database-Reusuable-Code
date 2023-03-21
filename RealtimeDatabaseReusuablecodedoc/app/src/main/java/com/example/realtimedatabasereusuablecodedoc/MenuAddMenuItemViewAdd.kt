/*package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MenuAddMenuItemViewAdd : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var menuItemRecyclerView: RecyclerView
    private lateinit var menuItemArrayList: ArrayList<MenuItemDC>
    private lateinit var auth: FirebaseAuth
    private lateinit var msAdapter: MenuAddMenuItemViewAddMultiselectAdapter
    private lateinit var rv: RecyclerView
    //private lateinit var menuItemArrayList: ArrayList<MenuItemDC>
    //private lateinit var menuItemAdapter: MultiselectAdapter
    //private lateinit var binding:

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_item_view_edit)
        menuItemRecyclerView = findViewById(R.id.menuItemList)
        menuItemRecyclerView.layoutManager = LinearLayoutManager(this)
        menuItemRecyclerView.setHasFixedSize(true)

        menuItemArrayList = arrayListOf<MenuItemDC>()
        getUserData()

    }

    private fun getUserData(){
        database = FirebaseDatabase.getInstance().getReference("Menu Items/")
        auth = FirebaseAuth.getInstance()
        database.orderByChild("merchantID").equalTo(auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItemArrayList.clear()
                Toast.makeText(this@MenuAddMenuItemViewAdd, "onDataChanged!", Toast.LENGTH_SHORT).show()
                if (snapshot.exists()) {
                    //restaurantArrayList.clear()
                    for (menuItemSnapshot in snapshot.children) {
                        val menuItem = menuItemSnapshot.getValue(MenuItemDC::class.java)
                        menuItemArrayList.add(menuItem!!)
                    }
                    msAdapter =
                        MenuAddMenuItemViewAddMultiselectAdapter(menuItemArrayList) { show ->
                            showDeleteMenu(show)
                        }

                    rv.adapter = msAdapter
                }
            }

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (menuItemSnapshot in snapshot.children){
                        val menuItem = menuItemSnapshot.getValue(MenuItemDC::class.java)
                        menuItemArrayList.add(menuItem!!)

                    }

                    menuItemRecyclerView.adapter = MenuItemViewEditMultiselectAdapter(menuItemArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}
*/