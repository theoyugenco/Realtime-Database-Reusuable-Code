package com.example.realtimedatabasereusuablecodedoc

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide

class ReviewAdapter (val context: Context, var reviewList: ArrayList<ReviewDC>): RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    //value to be displayed in the view
    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username : TextView = itemView.findViewById<TextView>(R.id.text_username)
        val ratingBar : RatingBar = itemView.findViewById<RatingBar>(R.id.rating_bar)
        val feedback : TextView = itemView.findViewById<TextView>(R.id.text_feedback)
        val reviewImage : ImageView = itemView.findViewById<ImageView>(R.id.review_image)
    }

    //Setups the layout for a card view for a user in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.review_layout, parent, false)
        return ReviewViewHolder(view)
    }

    //get total number of users
    override fun getItemCount(): Int {
        return reviewList.size
    }

    //binds the current user's name to the view holder. carries over
    //name and UID of chosen user to represent a recipient in messaging
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val currentReview = reviewList[position]

        holder.username.text = currentReview.username
        holder.feedback.text = currentReview.feedback
        Glide.with(context).load(currentReview.picture).into(holder.reviewImage)
        val rating = currentReview.rating
        if (rating != null) {
            holder.ratingBar.rating = rating
        }
    }
}