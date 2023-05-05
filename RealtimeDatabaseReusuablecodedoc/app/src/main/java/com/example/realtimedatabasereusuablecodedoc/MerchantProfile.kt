package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityMerchantProfileBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.w3c.dom.Text
import java.io.File

/*
theodore Yu
Where Merchants can custom their profile and retrieve personal information
 */

class MerchantProfile : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMerchantProfileBinding
    private var TAG: String? = null
    private lateinit var tvFName: TextInputLayout
    private lateinit var tvLName: TextInputLayout
    private lateinit var tvUName: TextInputLayout
    private lateinit var tvPWord: TextInputLayout
    private lateinit var tvEmail: TextInputLayout
    private lateinit var tvPhone: TextInputLayout
    private lateinit var tvAddress: TextInputLayout
    private lateinit var tvPayment: TextInputLayout
    private lateinit var storageReference: StorageReference
    private lateinit var chosenImage: Uri
    private lateinit var tvNameLabel: TextView
    private lateinit var tvPWordLabel: TextView
    private lateinit var img: ImageView

    private lateinit var chatButton: ImageButton
    private lateinit var profButton: ImageButton
    private lateinit var homeButton: ImageButton

    private var uid: String? = ""

    //private lateinit var fullName: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merchant_profile)

        binding = ActivityMerchantProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser!!.uid

        tvFName = findViewById(R.id.tvFName)
        tvLName = findViewById(R.id.tvLName)
        tvUName = findViewById(R.id.tvUName)
        tvPWord = findViewById(R.id.tvPWord)
        tvEmail = findViewById(R.id.tvEmail)
        tvPhone = findViewById(R.id.tvPhone)
        tvAddress = findViewById(R.id.tvAddress)
        tvPayment = findViewById(R.id.tvPayment)
        img = findViewById(R.id.user_image)

        //Upon the User's entrance into their profile, we will retrieve their information and profile picture
        readData()
        retrieveImage()

        /*By default, none of the fields will be allowed to be focused/touchable/editable
        and the save button will not even show.
        We will allow them, upon pressing the Edit button, to make whatever changes that they wish
        to the fields that we deem reasonable to be editable within this semester timeframe.
        However, we will only commit to changing their fields (RTD) and profile picture (storage)
        if they press the Save Button, upon which, the fields will return to their unfocusable states
        */
        binding.btnEdit.setOnClickListener() {
            binding.btnSave.isVisible = true
            binding.tfFName.isFocusable = true
            binding.tfFName.isFocusableInTouchMode = true
            binding.tfLName.isFocusable = true
            binding.tfLName.isFocusableInTouchMode = true
            binding.tfUName.isFocusable = true
            binding.tfUName.isFocusableInTouchMode = true
            binding.tfPhone.isFocusable = true
            binding.tfPhone.isFocusableInTouchMode = true
            binding.btnEdit.isVisible = false

            tvAddress.getEditText()!!.setText("You may NOT edit your address.")
            tvPayment.getEditText()!!.setText("Add or Change your Payment outside of Edit with a long click.")
            tvEmail.getEditText()!!.setText("You may NOT edit your email.")
            tvPWord.getEditText()!!.setText("You may NOT edit your password.")


            binding.btnSave.setOnClickListener(){
                binding.btnSave.isVisible = false
                binding.btnEdit.isVisible = true
                binding.tfFName.isFocusable = false
                binding.tfFName.isFocusableInTouchMode = false
                binding.tfLName.isFocusable = false
                binding.tfLName.isFocusableInTouchMode = false
                binding.tfUName.isFocusable = false
                binding.tfUName.isFocusableInTouchMode = false
                binding.tfPhone.isFocusable = false
                binding.tfPhone.isFocusableInTouchMode = false

                editData()
            }
        }

        img = findViewById(R.id.user_image)

        img.setOnClickListener(){
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        binding.btnLogout.setOnClickListener{
            auth.signOut()
            startActivity(
                Intent(this, MainActivity::class.java)
            )
            finish()
        }

        chatButton = findViewById(R.id.message_btn)
        chatButton.setOnClickListener {
            val intent = Intent(this, ChatSearch::class.java)
            startActivity(intent)
        }

        profButton = findViewById(R.id.profile_btn)

        profButton.setOnClickListener {
            val intent = Intent(this, MerchantProfile::class.java)
            startActivity(intent)
        }

        homeButton = findViewById(R.id.home_btn)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeMerchant::class.java)
            startActivity(intent)
        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //This takes care of uploading/editing/saving the Users' profile picture
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (data.data != null) {
                chosenImage = data.data!!
                binding.userImage.setImageURI(chosenImage)
                editUploadImage()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.merchant_item, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //This is the same hamburger menu button in Home.  We are unsure if we want to use this later on
        return when (item.itemId) {
            R.id.menu_location -> {
                val intent = Intent(this, RestaurantManagement::class.java)
                startActivity(intent)
                Toast.makeText(this, "Location Clicked", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.menu_menu -> {
                val intent = Intent(this, ItemMenu::class.java)
                startActivity(intent)
                Toast.makeText(this, "Menu Clicked", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.menu_item -> {
                val intent = Intent(this, MenuItemManagement::class.java)
                startActivity(intent)
                Toast.makeText(this, "Menu Item Clicked", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun retrieveImage() {
        //We have determined that we will save the Merchant's profile picture with their Auth UID
        //Thus we will retrieve it with their UID
        val storage = FirebaseStorage.getInstance().reference.child("Merchants/" + uid)
        val merchantProfileImageFile = File.createTempFile("merchantProfileImage", "jpg")
        if (merchantProfileImageFile.exists()) {
            storage.getFile(merchantProfileImageFile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(merchantProfileImageFile.absolutePath)
                binding.userImage.setImageBitmap(bitmap)
            }.addOnFailureListener {

            }
        }
    }

    private fun readData() {
        database = FirebaseDatabase.getInstance().getReference("Users/Merchants")
        database.child(uid!!).get().addOnSuccessListener {
            //If a node/entry of that specific UserName exists
            if (it.exists()) {
                //We are deciding if we want to facilitate payment via Credit Card VS PayPal
                val fName: String? = it.child("fname").value.toString()
                val lName: String? = it.child("lname").value.toString()
                val uName: String? = it.child("uname").value.toString()
                val pWord: String? = it.child("pword").value.toString()
                val email: String? = it.child("email").value.toString()
                //val paymentPayPal: String? = it.child().value.toString()
                val phone: String? = it.child("phone").value.toString()
                val streetAddress: String? = it.child("streetAddress").value.toString()
                val city: String? = it.child("city").value.toString()
                val state: String? = it.child("state").value.toString()
                val zipcode: String? = it.child("zipcode").value.toString()
                val fullAddress: String? =
                    streetAddress + ", " + city + ", " + state + " " + zipcode


                /*
                Toast.makeText(
                    this,
                    "Successfully retrieved and read User's data!",
                    Toast.LENGTH_SHORT
                ).show()
                 */

                //We just want to ensure that none of the fields are blank.
                //Otherwise, we want to retrieve their data here

                if (fName.isNullOrEmpty()) {
                    tvFName.getEditText()!!.setText("MISSING. PLEASE MAKE THE APPROPRIATE EDIT!")
                } else {
                    tvFName.getEditText()!!.setText(fName)
                }

                if (lName.isNullOrEmpty()) {
                    tvLName.getEditText()!!.setText("MISSING. PLEASE MAKE THE APPROPRIATE EDIT!")
                } else {
                    tvLName.getEditText()!!.setText(lName)
                }

                if (uName.isNullOrEmpty()) {
                    tvUName.getEditText()!!.setText("MISSING. PLEASE MAKE THE APPROPRIATE EDIT!")
                } else {
                    tvUName.getEditText()!!.setText(uName)
                }

                if (pWord.isNullOrEmpty()) {
                    tvPWord.getEditText()!!.setText("MISSING. PLEASE MAKE THE APPROPRIATE EDIT!")
                } else {
                    tvPWord.getEditText()!!.setText(pWord)
                }

                if (phone.isNullOrEmpty()) {
                    tvPhone.getEditText()!!.setText("MISSING. PLEASE MAKE THE APPROPRIATE EDIT!")
                } else {
                    tvPhone.getEditText()!!.setText(phone)
                }

                if (email.isNullOrEmpty()) {
                    tvEmail.getEditText()!!.setText("MISSING. PLEASE MAKE THE APPROPRIATE EDIT!")
                } else {
                    tvEmail.getEditText()!!.setText(email)
                }

                if ((streetAddress.isNullOrEmpty()) || (city.isNullOrEmpty()) || (state.isNullOrEmpty()) || (zipcode.isNullOrEmpty())) {
                    tvAddress.getEditText()!!.setText("MISSING. PLEASE MAKE THE APPROPRIATE EDIT!")
                } else {
                    tvAddress.getEditText()!!.setText(fullAddress)
                }
            }
            //If they provided a Username that does not exist (no such entry or node with that Username)
            else {
                Toast.makeText(
                    this,
                    "A User with that UserName does NOT exist!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }.addOnFailureListener() {
            Toast.makeText(this, "Unable to read the User's data!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun editData(){
        //Saving the previous data because the User may not want to change everything.

        var newFName : String? = binding.tfFName.text.toString()
        var newLName : String? = binding.tfLName.text.toString()
        var newUName : String? = binding.tfUName.text.toString()
        //var newPWord : String? = binding.tfPWord.text.toString()
        var newPhone : String? = binding.tfPhone.text.toString()

        database = FirebaseDatabase.getInstance().getReference("Users/Merchants")
        database.child(uid!!).get().addOnSuccessListener {
            if(it.exists()) {
                //Toast.makeText(this, "edit data ran", Toast.LENGTH_SHORT).show()
                //If a node/entry of that specific UserName exists

                //Many of these fields are difficult to change or have external factors to consider
                //In the interest of time, these fields will not be available to edit.

                val updated_user = mapOf<String, String>(
                    "fname" to newFName!!,
                    "lname" to newLName!!,
                    "uname" to newUName!!,
                    "phone" to newPhone!!
                )
                database.child(uid!!).updateChildren(updated_user).addOnSuccessListener {
                    //Toast.makeText(this, "Successfully updated the User's information\nUpdated Profile showcased below!", Toast.LENGTH_SHORT).show()
                    readData()
                }.addOnFailureListener(){
                    Toast.makeText(this, "Unsuccessfully in updating User's information", Toast.LENGTH_SHORT).show()
                }
            }
            //If they provided a Username that does not exist (no such entry or node with that Username)
            else{
                Toast.makeText(this, "edA User with that UserName does NOT exist!", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener(){
            Toast.makeText(this, "Unable to read the User's data for editting!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun editUploadImage(): Boolean {
        //Image retrieval follows the logic of this code, they are much same, instead of retreiving, we save here
        var status : Boolean = true
        storageReference =
            FirebaseStorage.getInstance().getReference("Merchants/" + auth.currentUser?.uid)
        storageReference.putFile(chosenImage).addOnSuccessListener {
            Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Profile picture failed to update!", Toast.LENGTH_SHORT).show()
            status = false
        }
        return status
    }

}