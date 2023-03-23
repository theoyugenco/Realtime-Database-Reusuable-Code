package com.example.realtimedatabasereusuablecodedoc

data class CustomerCheckoutDC(var name: String? = "",
                              var streetAddress: String? = "",
                              var city: String? = "",
                              var state: String? = "",
                              var zipcode: String? = "",
                              var description: String? = "",
                              var restaurantID: String? = "",
                              var merchantID: String? = "")
