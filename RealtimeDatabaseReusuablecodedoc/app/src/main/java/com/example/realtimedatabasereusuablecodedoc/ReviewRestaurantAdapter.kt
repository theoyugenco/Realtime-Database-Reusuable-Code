package com.example.realtimedatabasereusuablecodedoc

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ReviewRestaurantAdapter (val context: Context, var locationList: ArrayList<LocationDC>):
    RecyclerView.Adapter<ReviewRestaurantAdapter.ReviewRestaurantViewHolder>() {

    //value to be displayed in the view
    class ReviewRestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById<TextView>(R.id.textLocation)
        val picture : ImageView = itemView.findViewById<ImageView>(R.id.imgLocation)
    }

    //Sets user list to filtered list
    fun setFilteredList(lList: ArrayList<LocationDC>) {
        this.locationList = lList
        notifyDataSetChanged()
    }

    //Setups the layout for a card view for a user in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewRestaurantViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.review_restaurant_layout, parent, false)
        return ReviewRestaurantViewHolder(view)
    }

    //get total number of users
    override fun getItemCount(): Int {
        return locationList.size
    }

    //binds the current user's name to the view holder. carries over
    //name and UID of chosen user to represent a recipient in messaging
    override fun onBindViewHolder(holder: ReviewRestaurantViewHolder, position: Int) {
        val currentLocation = locationList[position]

        holder.name.text = currentLocation.brand
        holder.picture.setImageResource(locationList[position].picture)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, Review::class.java)
            intent.putExtra("name", currentLocation.brand)
            //will need to finalize identification method of restaurant image
            intent.putExtra("logo", currentLocation.picture)
            context.startActivity(intent)
        }
    }
}