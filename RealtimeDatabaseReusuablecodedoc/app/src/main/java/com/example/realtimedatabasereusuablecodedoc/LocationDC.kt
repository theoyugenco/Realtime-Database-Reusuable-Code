package com.example.realtimedatabasereusuablecodedoc

import android.net.Uri

/*
Kenneth Valero
This data class represents locations that are recorded based on distance.
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

