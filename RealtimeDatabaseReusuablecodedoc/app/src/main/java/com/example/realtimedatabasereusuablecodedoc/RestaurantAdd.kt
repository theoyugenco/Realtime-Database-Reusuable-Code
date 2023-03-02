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
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
    private var key: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_add)
        binding = ActivityRestaurantAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

       binding.restaurantImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
        uploadData()
        /*
        dialog = AlertDialog(this)
            .setMessage("Updating Profile...")
            .setCancelable(false)
        */







    }

    private fun uploadData(){
        //val reference



        binding.btnAdd.setOnClickListener(){
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

                //val newRestaurant: RestaurantDC = RestaurantDC(name, streetAddress, city, state, zip, description)
                database = FirebaseDatabase.getInstance().getReference("Users/Restaurants")

                val pushedRestaurant: DatabaseReference = database.push()
                key = pushedRestaurant.key
                val url :String?  = uploadRestaurantImage()


                val newRestaurant: RestaurantDC = RestaurantDC(name, streetAddress, city, state, zip, description, key, url)

                database.child(key!!).setValue(newRestaurant).addOnSuccessListener {
                    binding.etName.text.clear()
                    binding.etStreetAddress.text.clear()
                    binding.etCity.text.clear()
                    binding.etState.text.clear()
                    binding.etZip.text.clear()
                    binding.etDescription.text.clear()
                    //uploadRestaurantImage()
                    //database.child(key!!).updateChildren()


                    Toast.makeText(this, "Your Restaurant has been successfully created and registered.", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener(){
                    Toast.makeText(this, "Your Restaurant has FAILED to be created and registered.", Toast.LENGTH_SHORT).show()
                }


            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (data.data != null) {
                selectedImg = data.data!!
                binding.restaurantImage.setImageURI(selectedImg)
                //uploadRestaurantImage()
            }
        }
    }

    private fun uploadRestaurantImage(): String? {
        storage =
            FirebaseStorage.getInstance().getReference("Restaurants/" + key)
        var url : String?  = null
        url = storage.downloadUrl.toString()
        storage.putFile(selectedImg).addOnSuccessListener {
            //url = storage.downloadUrl.toString()
            Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Profile picture failed to update!", Toast.LENGTH_SHORT).show()
        }
        return url
    }

}