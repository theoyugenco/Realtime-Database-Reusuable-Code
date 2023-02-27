package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Review : AppCompatActivity() {
    private lateinit var restaurantPic: ImageView
    private lateinit var reviewUpload: ImageView
    private lateinit var restaurantName: TextView
    private lateinit var restaurantAddress: TextView
    private lateinit var getPicture: Button
    private lateinit var chosenImage: Uri
    private lateinit var feedbackWindow: EditText
    private lateinit var seeReviews: Button
    private lateinit var submitReview: Button
    private lateinit var ratingBar: RatingBar
    private lateinit var uploadURL: String
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        restaurantPic = findViewById((R.id.location_image))
        reviewUpload = findViewById(R.id.review_upload)
        restaurantName = findViewById(R.id.restaurant_name)
        restaurantAddress = findViewById(R.id.restaurant_address)
        getPicture = findViewById(R.id.get_picture)
        feedbackWindow = findViewById(R.id.feedback_window)
        seeReviews = findViewById(R.id.see_review_button)
        submitReview = findViewById((R.id.submit_review))
        ratingBar = findViewById(R.id.rating_bar)

        val name = intent.getStringExtra("name")
        val logo = intent.getStringExtra("logo")
        val address = intent.getStringExtra("address")
        val locationID = intent.getStringExtra("locationID")

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Reviews")

        restaurantName.text = name
        restaurantAddress.text = address
        Glide.with(this).load(logo).into(restaurantPic)

        getPicture.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        submitReview.setOnClickListener {
            val feedback = feedbackWindow.text.toString()
            val getRatingValue = ratingBar.rating
            uploadProfilePic(locationID.toString())

            val currentUserUID = auth.currentUser!!.uid
            var username : String = ""
            database = FirebaseDatabase.getInstance().getReference("Users/Customers")
            database.child(currentUserUID).get().addOnSuccessListener {
                if (it.exists()) {
                    username = it.child("uname").value.toString()
                }
            }

            database = FirebaseDatabase.getInstance().getReference("Reviews")
            val newReview = ReviewDC(currentUserUID, locationID, username, feedback, getRatingValue, uploadURL)
            database.child(locationID + currentUserUID).setValue(newReview)
        }

        seeReviews.setOnClickListener {
            val intent = Intent(this, CurrentReviews::class.java)
            intent.putExtra("locationID", locationID)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (data.data != null) {
                chosenImage = data.data!!
                reviewUpload.setImageURI(chosenImage)
            }
        }
    }

    private fun uploadProfilePic(locationID: String) {
        storageReference =
            FirebaseStorage.getInstance().getReference("Reviews/" + auth.currentUser?.uid + locationID)
        storageReference.putFile(chosenImage).addOnSuccessListener {
            uploadURL = storageReference.downloadUrl.toString()
            Toast.makeText(this, "Review picture uploaded", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Review picture failed to upload!", Toast.LENGTH_SHORT).show()
        }
    }
}