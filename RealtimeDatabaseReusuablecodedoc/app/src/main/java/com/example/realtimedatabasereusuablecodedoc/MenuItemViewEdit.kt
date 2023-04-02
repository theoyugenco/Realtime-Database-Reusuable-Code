package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.*
import com.google.firebase.database.*


class MenuItemViewEdit : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var menuItemRecyclerView: RecyclerView
    private lateinit var menuItemArrayList: ArrayList<MenuItemDC>
    //private lateinit var menuItemAdapter: MultiselectAdapter
    //private lateinit var binding:

    private var menuItemMenu: Menu? = null
    private lateinit var msAdapter: MenuItemViewEditMultiselectAdapter

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

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItemArrayList.clear()
                if (snapshot.exists()){
                    for (menuItemSnapshot in snapshot.children){
                        val menuItem = menuItemSnapshot.getValue(MenuItemDC::class.java)
                        menuItemArrayList.add(menuItem!!)

                    }

                    msAdapter = MenuItemViewEditMultiselectAdapter(menuItemArrayList)
                    { show ->
                        showDeleteMenu(show)
                    }
                    menuItemRecyclerView.adapter = msAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.delete -> { delete() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuItemMenu = menu
        menuInflater.inflate(R.menu.view_edit_menu,menuItemMenu)
        showDeleteMenu(false)
        return super.onCreateOptionsMenu(menu)
    }

    fun showDeleteMenu(show: Boolean){
        menuItemMenu?.findItem(R.id.delete)?.isVisible = show
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