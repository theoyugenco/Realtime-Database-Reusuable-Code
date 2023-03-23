package com.example.realtimedatabasereusuablecodedoc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class CustomerCheckoutAdapter(/*private val checkoutList : ArrayList<CustomerCheckoutDC>*/
    private var checkoutList : ArrayList<RestaurantDC>,
    private val showMenuDelete: (Boolean) -> Unit,
    ) : RecyclerView.Adapter<CustomerCheckoutAdapter.MultiselectViewHolder>() {

    private lateinit var database: DatabaseReference
    private var isEnable = false
    private var itemSelectedList = ArrayList<String>()
    private var keyTBR : String? = null
    private var TAG: String? = null
    private lateinit var storage: StorageReference
    private lateinit var selectedImg: Uri

    class MultiselectViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(com.example.realtimedatabasereusuablecodedoc.R.id.card)
        val name: TextView = view.findViewById(com.example.realtimedatabasereusuablecodedoc.R.id.tvName)
        val address: TextView = view.findViewById(com.example.realtimedatabasereusuablecodedoc.R.id.tvAddress)
        val description: TextView = view.findViewById(com.example.realtimedatabasereusuablecodedoc.R.id.tvDescription)
        val check: ImageView = view.findViewById(com.example.realtimedatabasereusuablecodedoc.R.id.checkSelect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiselectViewHolder {
        /*
        This run for every time a card is created
        a card is created for every single item
         */

        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(com.example.realtimedatabasereusuablecodedoc.R.layout.restaurant, parent, false)
        //Log.d(TAG, "do i run when deletion")
        return MultiselectViewHolder(adapterLayout)
    }


    override fun getItemCount(): Int {
        return checkoutList.size
    }

    private fun selectItem(
        holder: CustomerCheckoutAdapter.MultiselectViewHolder,
        item: RestaurantDC,
        position: Int
    ) {
        //isEnable = true
        keyTBR = item.restaurantID //Change this code
        itemSelectedList.add(keyTBR!!)

        showMenuDelete(true)
    }

    override fun onBindViewHolder(holder: MultiselectViewHolder, position: Int) {

        val item = checkoutList[position]
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

/*
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiselectViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.restaurant, parent, false)
        return MultiselectViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: CustomerCheckoutAdapter.MultiselectViewHolder, position: Int) {
        val item = checkoutList[position]
        //keyTBR = item.restaurantID

        //This is each card is created.
        holder.name.text = item.name.toString()
        holder.description.text = item.description.toString()
        var fullAddress : String? = item.streetAddress.toString() + ", " + item.city.toString() + ", " + item.state.toString() + " " + item.zipcode.toString()
        holder.address.text = fullAddress
        holder.check.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return checkoutList.size
    }

    class MultiselectViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.card)
        val name: TextView = view.findViewById(R.id.tvName)
        val address: TextView = view.findViewById(R.id.tvAddress)
        val description: TextView = view.findViewById(R.id.tvDescription)
        val check: ImageView = view.findViewById(R.id.checkSelect)
    }

 */
}