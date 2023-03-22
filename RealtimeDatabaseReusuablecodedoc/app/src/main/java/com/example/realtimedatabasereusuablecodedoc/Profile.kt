package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityProfileBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File


/*
theodore Yu
Where Customer can custom their profile and retrieve personal information
Much of the code from MerchantProfile.kt is being used and recycled here.
 */
class Profile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private lateinit var profilePic: ImageView
    private lateinit var chosenImage: Uri
    private var TAG: String? = null
    private lateinit var tvFName: TextInputLayout
    private lateinit var tvLName: TextInputLayout
    private lateinit var tvUName: TextInputLayout
    private lateinit var tvPWord: TextInputLayout
    private lateinit var tvEmail: TextInputLayout
    private lateinit var tvPhone: TextInputLayout
    private lateinit var tvAddress: TextInputLayout
    private lateinit var tvPayment: TextInputLayout
    private lateinit var tvNameLabel: TextView
    private lateinit var tvPWordLabel: TextView
    private lateinit var img: ImageView

    private var uid: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        binding = ActivityProfileBinding.inflate(layoutInflater)
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

        binding.btnSignout.setOnClickListener{
            auth.signOut()
            startActivity(
                Intent(this, MainActivity::class.java)
            )
            finish()
        }


        /*
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profilePic = findViewById((R.id.user_image))

        auth = FirebaseAuth.getInstance()
        retrieveProfilePicture()

        /*
        Kenneth Valero: Image Upload Use Case
        User can click on the profile picture imageview
        to access their gallery to upload an image
         */
        profilePic.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        //val test = auth.currentUser.
        database = FirebaseDatabase.getInstance().getReference("Users")
        val currentUserUID = auth.currentUser!!.uid

        database.child(currentUserUID).get().addOnSuccessListener {
            //If a node/entry of that specific Email exists
            if (it.exists()) {
                binding.tvEmail.editText!!.setText(auth.currentUser!!.email)

                val fName = it.child("fname").value
                val lName = it.child("lname").value

                val name = "$fName $lName"
                val name2 = "" + fName + " " + lName

                binding.tvName.editText!!.setText("test me")

            }
        }
        */

        */
    }
    
    /*
    Kenneth Valero: Image Upload Use case
    Function to set the current displayed imageview to the chosen image
    initiates uploading image to storage
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (data.data != null) {
                chosenImage = data.data!!
                binding.userImage.setImageURI(chosenImage)
                editUploadImage()
            }
        }
    }

    /*
    Kenneth Valero: Image Upload Use Case
    Creates a storage reference to the Customers folder then adds the image to the storage
    under the user ID
     */
    private fun uploadProfilePic() {
        storageReference =
            FirebaseStorage.getInstance().getReference("Customers/" + auth.currentUser?.uid)
        storageReference.putFile(chosenImage).addOnSuccessListener {
            Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Profile picture failed to update!", Toast.LENGTH_SHORT).show()
        }
    }

    /*
    Kenneth Valero: Image Upload Use Case
    Retrieves the profile picture from the storage and assigns the bitmap data to the profile
    picture image view.
     */
    private fun retrieveProfilePicture() {
        storageReference = FirebaseStorage.getInstance().getReference().child("Customers/"+auth.currentUser?.uid)
        val localFile = File.createTempFile("ProfilePic", "jpg")
        storageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile((localFile.absolutePath))
            profilePic.setImageBitmap(bitmap)
        }.addOnFailureListener{
            Toast.makeText(this, "No profile picture available...", Toast.LENGTH_SHORT).show()
        }
    }


    private fun readData() {
        database = FirebaseDatabase.getInstance().getReference("Users/Customers")
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



                Toast.makeText(
                    this,
                    "Successfully retrieved and read User's data!",
                    Toast.LENGTH_SHORT
                ).show()

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

        database = FirebaseDatabase.getInstance().getReference("Users/Customers")
        database.child(uid!!).get().addOnSuccessListener {
            if(it.exists()) {
                Toast.makeText(this, "edit data ran", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this, "Successfully updated the User's information\nUpdated Profile showcased below!", Toast.LENGTH_SHORT).show()
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
            FirebaseStorage.getInstance().getReference("Customers/" + auth.currentUser?.uid)
        storageReference.putFile(chosenImage).addOnSuccessListener {
            Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Profile picture failed to update!", Toast.LENGTH_SHORT).show()
            status = false
        }
        return status
    }

    private fun retrieveImage() {
        //We have determined that we will save the Merchant's profile picture with their Auth UID
        //Thus we will retrieve it with their UID
        val storage = FirebaseStorage.getInstance().reference.child("Customers/" + uid)
        val customerProfileImageFile = File.createTempFile("customerProfileImage", "jpg")
        if (customerProfileImageFile.exists()) {
            storage.getFile(customerProfileImageFile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(customerProfileImageFile.absolutePath)
                binding.userImage.setImageBitmap(bitmap)
            }.addOnFailureListener {

            }
        }
    }
}