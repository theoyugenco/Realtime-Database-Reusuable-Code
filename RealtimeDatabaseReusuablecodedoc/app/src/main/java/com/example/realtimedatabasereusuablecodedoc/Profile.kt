package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.widget.*
import com.example.realtimedatabasereusuablecodedoc.databinding.ActivityProfileBinding
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
 */
class Profile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private lateinit var profilePic: ImageView
    private lateinit var chosenImage: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

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
                uploadProfilePic()
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
}