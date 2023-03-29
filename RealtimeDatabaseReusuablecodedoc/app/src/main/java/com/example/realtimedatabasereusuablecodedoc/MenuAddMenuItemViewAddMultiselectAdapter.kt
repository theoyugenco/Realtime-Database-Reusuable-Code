package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

//class MenuAddMenuItemViewAddMultiselectAdapter (private val menuItemClickListener: ArrayList<MenuItemDC>):RecyclerView.Adapter<Menu>


class MenuAddMenuItemViewAddMultiselectAdapter (
    private val menuItemList : ArrayList<MenuItemDC>,
    private val id : String,
    private val itemSelectedList : ArrayList<String>,
    private val showMenuCart: (Boolean) -> Unit
):RecyclerView.Adapter<MenuAddMenuItemViewAddMultiselectAdapter.MultiselectViewHolder>() {

    private lateinit var database: DatabaseReference
    private var isEnable = false
    //private var itemSelectedList = ArrayList<String>()
    private var keyTBR : String? = null
    private var TAG: String? = null
    private lateinit var storage: StorageReference
    private lateinit var selectedImg: Uri

    class MultiselectViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.card)
        val name: TextView = view.findViewById(R.id.tvName)
        val price: TextView = view.findViewById(R.id.tvPrice)
        val description: TextView = view.findViewById(R.id.tvDescription)
        //val check: ImageView = view.findViewById(R.id.checkSelect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiselectViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        return MultiselectViewHolder(adapterLayout)
    }
/*
    override fun onBindViewHolder(holder: MultiselectViewHolder, position: Int) {
        val currentMenuItem = menuItemList[position]

        holder.name.text = currentMenuItem.name
        holder.description.text = currentMenuItem.description
        holder.price.text = currentMenuItem.price
    }
*/
    override fun onBindViewHolder(holder: MultiselectViewHolder, position: Int) {

        val item = menuItemList[position]
        keyTBR = item.menuItemID

        //This is each card is created.
        holder.name.text = item.name.toString()
        holder.description.text = item.description.toString()
        holder.price.text = item.price.toString()
        //holder.check.visibility = View.GONE

        /*
        holder.card.setOnLongClickListener() {
            selectItem(holder, item, position)
            holder.check.visibility = View.VISIBLE
            true
        }
        */
        holder.card.setOnClickListener() {
            Log.d(TAG, "A CLICK HAPPENED!")
            //menuItemID is
            keyTBR = item.menuItemID
            //is the item already checked
            if (itemSelectedList.contains(keyTBR)) {
                itemSelectedList.remove(keyTBR)
                //holder.check.visibility = View.GONE

                //It wouldn't make sense to show the options if nothing were to be selected
                if (itemSelectedList.isEmpty()) {
                    showMenuCart(false)
                }
            }
            else{
                //holder.check.visibility = View.VISIBLE
                //keyTBR = item.restaurantID //Change this code
                itemSelectedList.add(keyTBR!!)

                showMenuCart(true)
            }
        }
    }

    /*
    Instead of deleting items from the database, we are adding items to the database again
    However, instead of adding the actual Menu Items to the database again (duplication),
    we are merely adding the Menu Items' IDs/Keys

     */
    fun addSelectedItem(){
        database = FirebaseDatabase.getInstance().getReference("Menus/" + id + "/Menu Items/")
        if(itemSelectedList.isNotEmpty()){
            val size: Int = itemSelectedList.size
            var i : Int = 0
            for (i in 0..(size-1)){
                keyTBR = itemSelectedList.get(i)
                val key: String? = keyTBR
                database.child(key!!).setValue(key).addOnSuccessListener {

                }.addOnFailureListener(){

                }
            }
            itemSelectedList.clear()
        }
    }

    override fun getItemCount(): Int {
        return menuItemList.size
    }

    private fun selectItem(
        holder: MenuAddMenuItemViewAddMultiselectAdapter.MultiselectViewHolder,
        item: MenuItemDC,
        position: Int
    ) {
        //isEnable = true
        keyTBR = item.menuItemID //Change this code
        itemSelectedList.add(keyTBR!!)

        showMenuCart(true)
    }


}

