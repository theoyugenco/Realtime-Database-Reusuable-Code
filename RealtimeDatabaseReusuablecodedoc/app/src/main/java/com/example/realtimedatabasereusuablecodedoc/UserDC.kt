package com.example.realtimedatabasereusuablecodedoc

import com.google.firebase.database.PropertyName
//Data class to represent each individual user
data class  UserDC(
    @get:PropertyName("fname") @set:PropertyName("fname") var fName : String? = null,
    @get:PropertyName("lname") @set:PropertyName("lname") var lName : String? = null,
    @get:PropertyName("pword") @set:PropertyName("pword") var pWord : String? = null,
    @get:PropertyName("uname") @set:PropertyName("uname") var uName : String? = null,
    val email : String? = null,
    val uid : String? = null,
    @get:PropertyName("streetAddress") @set:PropertyName("streetAddress") var streetAddress: String? = null,
    @get:PropertyName("city") @set:PropertyName("city") var city: String? = null,
    @get:PropertyName("state") @set:PropertyName("state") var state: String? = null,
    @get:PropertyName("zipcode") @set:PropertyName("zipcode") var zipcode: String? = null,
    @get:PropertyName("phone") @set:PropertyName("phone") var phone: String? = null
    )
    {

}
