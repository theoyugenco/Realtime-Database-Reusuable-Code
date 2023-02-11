package com.example.realtimedatabasereusuablecodedoc

import com.google.firebase.database.PropertyName

data class UserDC(
    @get:PropertyName("fname") @set:PropertyName("fname") var fName : String? = null,
    @get:PropertyName("lname") @set:PropertyName("lname") var lName : String? = null,
    @get:PropertyName("pword") @set:PropertyName("pword") var pWord : String? = null,
    @get:PropertyName("uname") @set:PropertyName("uname") var uName : String? = null,
    val email : String? = null,
    val uid : String? = null)
    {

}
