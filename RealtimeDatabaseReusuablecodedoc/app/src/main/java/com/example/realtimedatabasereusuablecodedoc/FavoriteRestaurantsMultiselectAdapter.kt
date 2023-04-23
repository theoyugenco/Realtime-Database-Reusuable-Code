
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

/*
This class is incorrectly named since we changed LocationDC -> RestaurantDC
This class is planned to be renamed to RestaurantViewEditMultiselectAdapter after Second Work Review
 */
class FavoriteRestaurantsMultiselectAdapter(
    //private val showMenuEdit: (Boolean) -> Unit,
    private var locationList : ArrayList<RestaurantDC>,
    private val showMenuDelete: (Boolean) -> Unit,
    //private val showMenuEdit: (Boolean) -> Unit
): RecyclerView.Adapter<FavoriteRestaurantsMultiselectAdapter.MultiselectViewHolder>() {

    private lateinit var database: DatabaseReference
    private var isEnable = false
    private var itemSelectedList = ArrayList<String>()
    private var keyTBR : String? = null
    private var TAG: String? = null
    private lateinit var storage: StorageReference
    private lateinit var selectedImg: Uri

    class MultiselectViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.card)
        val name: TextView = view.findViewById(R.id.tvName)
        val address: TextView = view.findViewById(R.id.tvAddress)
        val description: TextView = view.findViewById(R.id.tvDescription)
        val check: ImageView = view.findViewById(R.id.checkSelect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiselectViewHolder {
        /*
        This run for every time a card is created
        a card is created for every single item
         */

        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.restaurant, parent, false)
        //Log.d(TAG, "do i run when deletion")
        return MultiselectViewHolder(adapterLayout)
    }


    override fun getItemCount(): Int {
        return locationList.size
    }

    private fun selectItem(
        holder: LocationViewEditMultiselectAdapter.MultiselectViewHolder,
        item: RestaurantDC,
        position: Int
    ) {
        //isEnable = true
        keyTBR = item.restaurantID //Change this code
        itemSelectedList.add(keyTBR!!)

        showMenuDelete(true)
    }

    override fun onBindViewHolder(holder: MultiselectViewHolder, position: Int) {

        val item = locationList[position]
        keyTBR = item.restaurantID

        //This is each card is created.
        holder.name.text = item.name.toString()
        holder.description.text = item.description.toString()
        var fullAddress : String? = item.streetAddress.toString() + ", " + item.city.toString() + ", " + item.state.toString() + " " + item.zipcode.toString()
        holder.address.text = fullAddress
        holder.check.visibility = View.GONE

        /*
        holder.card.setOnLongClickListener() {
            selectItem(holder, item, position)
            holder.check.visibility = View.VISIBLE
            true
        }
        */
        holder.card.setOnClickListener() {
            Log.d(TAG, "A CLICK HAPPENED!")
            keyTBR = item.restaurantID
            //is the item already checked
            if (itemSelectedList.contains(keyTBR)) {
                itemSelectedList.remove(keyTBR)
                holder.check.visibility = View.GONE

                //It wouldn't make sense to show the options if nothing were to be selected
                if (itemSelectedList.isEmpty()) {
                    showMenuDelete(false)
                }
            }
            else{
                holder.check.visibility = View.VISIBLE
                //keyTBR = item.restaurantID //Change this code
                itemSelectedList.add(keyTBR!!)

                showMenuDelete(true)
            }
        }
    }

    fun deleteSelectedItem(){
        if(itemSelectedList.isNotEmpty()){
            val size: Int = itemSelectedList.size
            var i : Int = 0

            val uid: String? = FirebaseAuth.getInstance().currentUser!!.uid
            for (i in 0..(size-1)){
                keyTBR = itemSelectedList.get(i)

                database = FirebaseDatabase.getInstance().getReference("Favorites/Favorite Restaurants/" + uid)
                val key: String? = keyTBR

                database.child(key!!).setValue(key).addOnFailureListener(){
                }.addOnSuccessListener {
                    Log.d(TAG, "Successfully added Favorite Restaurant on RTD")
                }.addOnFailureListener(){
                    Log.d(TAG, "Failed to add Favorite Restaurant on RTD")
                }
            }
            itemSelectedList.clear()
        }
    }
}
