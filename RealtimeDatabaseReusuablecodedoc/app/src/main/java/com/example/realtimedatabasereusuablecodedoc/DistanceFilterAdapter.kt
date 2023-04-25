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
Adapter used to sort a list of locations based on distance relative to
the user.
 */
class DistanceFilterAdapter (val context: Context, var locationList: ArrayList<LocationDC>):
    RecyclerView.Adapter<DistanceFilterAdapter.DistanceFilterViewHolder>() {
    /*
    Values to be displayed in the view:
    Restaurant name, restaurant picture, address, and distance
     */
    class DistanceFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById<TextView>(R.id.textLocation)
        val picture : ImageView = itemView.findViewById<ImageView>(R.id.imgLocation)
        val address: TextView = itemView.findViewById<TextView>(R.id.textAddress)
        val distance: TextView = itemView.findViewById<TextView>(R.id.textDistance)
    }

    /*
    Filters the list of restaurants based on search query
     */
    fun setFilteredDistanceList(lList: ArrayList<LocationDC>) {
        this.locationList = lList
        notifyDataSetChanged()
    }

    /*
    Sets up the layout for a card representing a location in the recyclerview
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistanceFilterViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.restaurant_search_card, parent, false)
        return DistanceFilterViewHolder(view)
    }

    /*
    Retrieve the total number of locations
     */
    override fun getItemCount(): Int {
        return locationList.size
    }

    /*
    Binds the current location's attributes to the viewholder:
    restaurant name, restaurant picture, restaurant address, and distance
    Sends the name, address, and restaurant ID to next activity
    when card is clicked.
     */
    override fun onBindViewHolder(holder: DistanceFilterViewHolder, position: Int) {
        val currentLocation = locationList[position]

        holder.name.text = currentLocation.name
        var storageReference: StorageReference = FirebaseStorage.getInstance().getReference().child("Restaurants/"+currentLocation.restaurantID)
        val localFile = File.createTempFile("RestaurantPic", "jpg")
        storageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile((localFile.absolutePath))
            holder.picture.setImageBitmap(bitmap)
        }.addOnFailureListener {
            Toast.makeText(context, "No restaurant picture available", Toast.LENGTH_SHORT).show()
        }
        val full_address : String = currentLocation.streetAddress + ", " + currentLocation.city + ", " + currentLocation.state + ", " + currentLocation.zipcode
        holder.address.text = full_address
        holder.distance.text = currentLocation.distanceFromUser.toString() + " m"
        holder.itemView.setOnClickListener {
            val intent = Intent(context, MenuCustomerOrdering::class.java)
            intent.putExtra("name", currentLocation.name)
            intent.putExtra("address", full_address)
            intent.putExtra("restaurantID", currentLocation.restaurantID)
            context.startActivity(intent)
        }
    }
}