package com.example.realtimedatabasereusuablecodedoc

import android.net.Uri

/*
Theodore Yu
This represents Merchants' specific locations of their restaurants
 */

data class LocationDC(val name: String?,
                      val ownerUID: String?,
                      var address: String?,
                      var description: String?,
                      var picture : String?){
}
