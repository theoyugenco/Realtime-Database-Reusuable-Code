package com.example.realtimedatabasereusuablecodedoc

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

/*
Kenneth Valero
This adapter will represent a compilation of restaurants that the user wants to review
 */
class RestaurantSearchAdapter (val context: Context, var restaurantList: ArrayList<RestaurantDC>):
    RecyclerView.Adapter<RestaurantSearchAdapter.RestaurantSearchViewHolder>() {
    /*
    Values to be displayed in the view:
    Restaurant name, restaurant picture, and the address
     */
    class RestaurantSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById<TextView>(R.id.textLocation)
        val picture : ImageView = itemView.findViewById<ImageView>(R.id.imgLocation)
        val address: TextView = itemView.findViewById<TextView>(R.id.textAddress)
    }

    /*
    Filters the list of restaurants based on search query
     */
    fun setFilteredList(rList: ArrayList<RestaurantDC>) {
        this.restaurantList = rList
        notifyDataSetChanged()
    }

    /*
    Sets up the layout for a card representing a restaurant in the recyclerview
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantSearchViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.review_restaurant_layout, parent, false)
        return RestaurantSearchViewHolder(view)
    }

    /*
    Retrieve the total number of restaurants
     */
    override fun getItemCount(): Int {
        return restaurantList.size
    }

    /*
    Binds the current restaurant's attributes to the viewholder:
    restaurant name, restaurant picture, restaurant address.
    Sends the name, image, address, and restaurant ID to next activity
    when card is clicked.
     */
    override fun onBindViewHolder(holder: RestaurantSearchViewHolder, position: Int) {
        val currentRestaurant = restaurantList[position]

        holder.name.text = currentRestaurant.name
        var storageReference: StorageReference = FirebaseStorage.getInstance().getReference().child("Restaurants/"+currentRestaurant.restaurantID)
        val localFile = File.createTempFile("RestaurantPic", "jpg")
        storageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile((localFile.absolutePath))
            holder.picture.setImageBitmap(bitmap)
        }.addOnFailureListener {
            Toast.makeText(context, "No restaurant picture available", Toast.LENGTH_SHORT).show()
        }
        val full_address : String = currentRestaurant.streetAddress + ", " + currentRestaurant.city + ", " + currentRestaurant.state + ", " + currentRestaurant.zipcode
        holder.address.text = full_address
        holder.itemView.setOnClickListener {
            val intent = Intent(context, Review::class.java)
            intent.putExtra("name", currentRestaurant.name)
            intent.putExtra("logo", currentRestaurant.downloadURL)
            intent.putExtra("address", full_address)
            intent.putExtra("restaurantID", currentRestaurant.restaurantID)
            context.startActivity(intent)
        }
    }
}