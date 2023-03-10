package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityRestaurantAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class RestaurantAdd : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantAddBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var selectedImg: Uri
    private lateinit var dialog: AlertDialog.Builder
    private lateinit var btnAdd: Button
    private lateinit var etName: EditText
    private lateinit var etStreetAddress: EditText
    private lateinit var etCity: EditText
    private lateinit var etState: EditText
    private lateinit var etZip: EditText
    private lateinit var etDescription: EditText
    private var uid: String? = ""
    private var key: String? = ""
    private var TAG: String? = null
    private var url: String? = null
    private var status1: Boolean = false
    private var status2: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_add)
        binding = ActivityRestaurantAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.visibility = View.GONE

        database = FirebaseDatabase.getInstance().getReference("Users/Restaurants")
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser!!.uid.toString()
        //database.push() will give us a random unique "hash", but as of now, it is NOT tied to anything yet
        val pushedRestaurant: DatabaseReference = database.push()
        key = pushedRestaurant.key.toString()

        binding.restaurantImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        //Changes should only be allowed to be finalized and executed when the user decides to Add (presses button)
        binding.btnAdd.setOnClickListener(){
            uploadData()
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Unlike in Merchant Profile, we do NOT upload/edit a Restaurant just by itself
        //An Image is only uploaded if the restaurant is uploaded with it in RTD
        //We only make the btnAdd visible upon selecting an image

        if (data != null) {
            if (data.data != null) {
                selectedImg = data.data!!
                binding.restaurantImage.setImageURI(selectedImg)
                //uploadRestaurantImage()
                binding.btnAdd.visibility = View.VISIBLE
            }
        }
    }

    private fun uploadData(){
        /*
        In this method, we are uploading both the fields and the image
         */
            val name = binding.etName.text.toString()
            val streetAddress = binding.etStreetAddress.text.toString()
            val city = binding.etCity.text.toString()
            val state = binding.etState.text.toString()
            val zip = binding.etZip.text.toString()
            val description = binding.etDescription.text.toString()

            if ((!(name.isNullOrEmpty()))  && (!(streetAddress.isNullOrEmpty())) && (!(city.isNullOrEmpty())) && (!(state.isNullOrEmpty())) && (!(zip.isNullOrEmpty())) && (!(description.isNullOrEmpty())) ){
                //All the fields are filled out with SOME sort of input

                //Perhaps we could have a check via some Google Maps API to see if the address provided is 1) LEGIT and 2) IN THE US

                //Skipping check/validation of physical/postal address for now.../

                database = FirebaseDatabase.getInstance().getReference("Users/Restaurants")
                Log.d (TAG, "key: "+key)
                Log.d (TAG, "url: "+url)

                if ((!(key.isNullOrEmpty())) && (!(uid.isNullOrEmpty()))){
                    //At this point, we should have all fields filled out
                    val newRestaurant: RestaurantDC = RestaurantDC(name, streetAddress, city, state, zip, description, key, uid)
                    //This is the portion that will upload the fields to RTD
                    database.child(key!!).setValue(newRestaurant).addOnSuccessListener {
                        binding.etName.text.clear()
                        binding.etStreetAddress.text.clear()
                        binding.etCity.text.clear()
                        binding.etState.text.clear()
                        binding.etZip.text.clear()
                        binding.etDescription.text.clear()

                        //ONLY UPON SUCCESS OF UPLOADING TO RTD, do we even bother with uploading the Image to Storage

                        //The path will always finish with the restaurant's id (key)
                        storage = FirebaseStorage.getInstance().getReference("Restaurants/" + key)

                        storage.putFile(selectedImg).addOnSuccessListener {
                            //Best case scenario, the image is uploaded to Storage as well!
                            val newPushedRestaurant: DatabaseReference = database.push()
                            key = newPushedRestaurant.key.toString()
                            Toast.makeText(this, "Restaurant Creation SUCCESS!", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            //Image failed to upload
                            //We MUST delete the entry in RTD now
                            database.child(key!!).removeValue().addOnSuccessListener {
                                Toast.makeText(this, "Restaurant Creation Failed!", Toast.LENGTH_SHORT).show()
                                //To Allow subsequent Restaurant Creation without override/write
                            }.addOnFailureListener(){
                                Toast.makeText(this, "CRITICAL ERROR! ROGUE RTD", Toast.LENGTH_SHORT).show()
                            }
                            Toast.makeText(this, "Profile picture failed to update!", Toast.LENGTH_SHORT).show()
                        }
                        Toast.makeText(this, "Restaurant in RTD", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener(){
                        Toast.makeText(this, "Your Restaurant has FAILED to be created and registered.", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this, "Your Restaurant NEEDS an image.", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Please fill out ALL the fields.", Toast.LENGTH_SHORT).show()
            }
    }

}