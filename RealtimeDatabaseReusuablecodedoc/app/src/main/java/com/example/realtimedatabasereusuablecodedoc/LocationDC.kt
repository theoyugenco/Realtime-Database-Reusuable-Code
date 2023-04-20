package com.example.realtimedatabasereusuablecodedoc

import android.net.Uri

/*
Theodore Yu
This represents Merchants' specific locations of their restaurants
 */

data class LocationDC(var name: String? = "",
                      var streetAddress: String? = "",
                      var city: String? = "",
                      var state: String? = "",
                      var zipcode: String? = "",
                      var description: String? = "",
                      var restaurantID: String? = "",
                      var distanceFromUser: Float? = null
)

