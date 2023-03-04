
package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class Review : AppCompatActivity() {
    private lateinit var restaurantPic: ImageView
    private lateinit var reviewUpload: ImageView
    private lateinit var restaurantName: TextView
    private lateinit var restaurantAddress: TextView
    private lateinit var getPicture: Button
    private lateinit var chosenImage: Uri
    private lateinit var feedbackWindow: TextInputEditText
    private lateinit var seeReviews: Button
    private lateinit var submitReview: Button
    private lateinit var ratingBar: RatingBar
//    private lateinit var uploadURL: String
    private lateinit var username: String
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

        auth = FirebaseAuth.getInstance()
        username = ""
        database = FirebaseDatabase.getInstance().getReference("Users/Customers")
        database.child(auth.currentUser!!.uid).get().addOnSuccessListener {
            if(it.exists()) {
                var currentUser: String = it.child("uname").value.toString()
                username = currentUser
            }
        }

        val name = intent.getStringExtra("name")
        val logo = intent.getStringExtra("logo")
        val address = intent.getStringExtra("address")
        val restaurantID = intent.getStringExtra("restaurantID")

        database = FirebaseDatabase.getInstance().getReference("Reviews")

        restaurantName.text = name
        restaurantAddress.text = address
//        var imageUri: Uri = Uri.parse(logo)
//        restaurantPic.setImageURI(imageUri)
//        Glide.with(this).load(imageUri).into(restaurantPic)
        var storageReference: StorageReference = FirebaseStorage.getInstance().getReference().child("Restaurants/"+restaurantID)
        val localFile = File.createTempFile("RestaurantPic", "jpg")
        storageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile((localFile.absolutePath))
            restaurantPic.setImageBitmap(bitmap)
        }.addOnFailureListener {
            Toast.makeText(this, "No restaurant picture available", Toast.LENGTH_SHORT).show()
        }

        getPicture.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        submitReview.setOnClickListener {
            val feedback = feedbackWindow.text.toString()
            val getRatingValue = ratingBar.rating
//            uploadURL = uploadProfilePic(restaurantID.toString())

            uploadProfilePic(restaurantID)

            database = FirebaseDatabase.getInstance().getReference("Reviews")
            val newReview = ReviewDC(auth.currentUser!!.uid, restaurantID, username, feedback, getRatingValue, chosenImage.toString())
            database.child(restaurantID + auth.currentUser!!.uid).setValue(newReview)
        }

        seeReviews.setOnClickListener {
            val intent = Intent(this, CurrentReviews::class.java)
            intent.putExtra("restaurantID", restaurantID)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (data.data != null) {
                chosenImage = data.data!!
                Glide.with(this).load(chosenImage).into(reviewUpload)
//                reviewUpload.setImageURI(chosenImage)
            }
        }
    }

    private fun uploadProfilePic(restaurantID: String?) {
        storageReference =
            FirebaseStorage.getInstance().getReference("Reviews/" + auth.currentUser?.uid + restaurantID)
        storageReference.putFile(chosenImage).addOnSuccessListener {
            //url = storage.downloadUrl.toString()
            Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Profile picture failed to update!", Toast.LENGTH_SHORT).show()
        }
    }
}