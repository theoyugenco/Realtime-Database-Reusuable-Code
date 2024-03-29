package com.example.realtimedatabasereusuablecodedoc

import android.net.Uri
/*
Kenneth Valero
Data class to represent reviews for a restaurant
 */
data class ReviewDC(var userID: String? = null,
                    var restaurantID: String? = null,
                    var username: String? = null,
                    var feedback: String? = null,
                    var rating: Float? = null,
                    var picture : String? = null)
