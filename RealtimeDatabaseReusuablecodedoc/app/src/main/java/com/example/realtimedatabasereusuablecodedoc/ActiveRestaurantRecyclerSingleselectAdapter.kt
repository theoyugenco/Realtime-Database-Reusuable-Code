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

/*
This adapter follows closely to that of MenuAddMenuItemViewAddMultiselectAdapter
 */

class ActiveRestaurantRecyclerSingleselectAdapter (
    private val restaurantList : ArrayList<RestaurantDC>,
    private val id : String,
    private val itemSelectedList : ArrayList<String>,
    private val showMenuCart: (Boolean) -> Unit
):RecyclerView.Adapter<ActiveRestaurantRecyclerSingleselectAdapter.SingleselectViewHolder>() {

    private lateinit var database: DatabaseReference
    private var isEnable = false
    //private var itemSelectedList = ArrayList<String>()
    private var keyTBR : String? = null
    private var TAG: String? = null
    private lateinit var storage: StorageReference
    private lateinit var selectedImg: Uri
    private var singleitem_selection_position: Int = -1
    private var first_click: Boolean = true
    private lateinit var previous : SingleselectViewHolder

    class SingleselectViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.card)
        val name: TextView = view.findViewById(R.id.tvName)
        val price: TextView = view.findViewById(R.id.tvPrice)
        val description: TextView = view.findViewById(R.id.tvDescription)
        val check: ImageView = view.findViewById(R.id.checkSelect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleselectViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        return SingleselectViewHolder(adapterLayout)
    }
    /*
        override fun onBindViewHolder(holder: MultiselectViewHolder, position: Int) {
            val currentMenuItem = menuItemList[position]

            holder.name.text = currentMenuItem.name
            holder.description.text = currentMenuItem.description
            holder.price.text = currentMenuItem.price
        }
    */
    override fun onBindViewHolder(holder: SingleselectViewHolder, position: Int) {

        val item = restaurantList[position]
        keyTBR = item.restaurantID
        Log.d(TAG, "keytbr is " + keyTBR)

        //This is each card is created.
        holder.name.text = item.name.toString()
        holder.description.text = item.description.toString()
        holder.price.text = item.streetAddress.toString()
        holder.check.visibility = View.GONE


        for (i in itemSelectedList){
            Log.d(TAG, "this should run in one round: i is " + i)
        }


        holder.card.setOnClickListener() {
            keyTBR = item.restaurantID
            if (first_click == true){
                Log.d(TAG, "Run oneces")
                first_click = false

                itemSelectedList.add(keyTBR!!)
                holder.check.visibility = View.VISIBLE
                showMenuCart(true)
                previous = holder
            }
            else{

                Log.d(TAG, "clickety")
                Log.d(TAG, "currentkeytbr is: " + keyTBR)
                for (i in itemSelectedList){
                    Log.d(TAG, "i is " + i)
                }
                previous.check.visibility = View.GONE
                holder.check.visibility = View.VISIBLE


                if (itemSelectedList.contains(keyTBR)) {
                    Log.d(TAG, "only on dubs")
                    itemSelectedList.remove(keyTBR)
                    itemSelectedList.clear()
                    holder.check.visibility = View.GONE

                    //It wouldn't make sense to show the options if nothing were to be selected
                    if (itemSelectedList.isEmpty()) {
                        showMenuCart(false)
                    }
                }
                else{
                    Log.d(TAG, "fresh blood")
                    itemSelectedList.clear()
                    previous.check.visibility = View.GONE
                    holder.check.visibility = View.VISIBLE

                    itemSelectedList.add(keyTBR!!)
                    previous = holder
                    showMenuCart(true)
                }





            }

        }
    }

    /*
    Instead of deleting items from the database, we are adding items to the database again
    However, instead of adding the actual Restaurants to the database again (duplication),
    we are merely adding the Restaurants' IDs/Keys

     */
    fun addSelectedItem() : String{
        var key : String? = null
        for (i in itemSelectedList) {
            Log.d(TAG, "strict run once")
            key = i
        }
        return key!!
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    private fun selectItem(
        holder: MenuAddMenuItemViewAddMultiselectAdapter.MultiselectViewHolder,
        item: RestaurantDC,
        position: Int
    ) {
        Log.d(TAG, "should not run")
        //isEnable = true
        keyTBR = item.restaurantID//Change this code
        itemSelectedList.add(keyTBR!!)

        showMenuCart(true)
    }


}

