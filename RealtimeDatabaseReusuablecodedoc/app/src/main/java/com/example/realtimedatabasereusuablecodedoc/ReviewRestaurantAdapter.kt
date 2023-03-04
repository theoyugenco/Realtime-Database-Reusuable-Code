
package com.example.realtimedatabasereusuablecodedoc

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.w3c.dom.Text
import java.io.File

class ReviewRestaurantAdapter (val context: Context, var restaurantList: ArrayList<RestaurantDC>):
    RecyclerView.Adapter<ReviewRestaurantAdapter.ReviewRestaurantViewHolder>() {

    //value to be displayed in the view
    class ReviewRestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById<TextView>(R.id.textLocation)
        val picture : ImageView = itemView.findViewById<ImageView>(R.id.imgLocation)
        val address: TextView = itemView.findViewById<TextView>(R.id.textAddress)
    }

    //Sets user list to filtered list
    fun setFilteredList(rList: ArrayList<RestaurantDC>) {
        this.restaurantList = rList
        notifyDataSetChanged()
    }

    //Setups the layout for a card view for a user in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewRestaurantViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.review_restaurant_layout, parent, false)
        return ReviewRestaurantViewHolder(view)
    }

    //get total number of users
    override fun getItemCount(): Int {
        return restaurantList.size
    }

    //binds the current user's name to the view holder. carries over
    //name and UID of chosen user to represent a recipient in messaging
    override fun onBindViewHolder(holder: ReviewRestaurantViewHolder, position: Int) {
        val currentRestaurant = restaurantList[position]

        holder.name.text = currentRestaurant.name
//        var imageUri: Uri = Uri.parse(currentRestaurant.downloadURL)
//        Glide.with(context).load(imageUri).into(holder.picture)
//        holder.picture.setImageURI(imageUri)
//        storageReference = FirebaseStorage.getInstance().getReference().child("Customers/"+auth.currentUser?.uid)
//        val localFile = File.createTempFile("ProfilePic", "jpg")
//        storageReference.getFile(localFile).addOnSuccessListener {
//            val bitmap = BitmapFactory.decodeFile((localFile.absolutePath))
//            profilePic.setImageBitmap(bitmap)
//        }.addOnFailureListener{
//            Toast.makeText(this, "No profile picture available...", Toast.LENGTH_SHORT).show()
//        }
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
