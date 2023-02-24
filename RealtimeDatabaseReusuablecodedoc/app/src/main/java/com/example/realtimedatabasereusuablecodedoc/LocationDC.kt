package com.example.realtimedatabasereusuablecodedoc
/*
Theodore Yu
This represents Merchants' specific locations of their restaurants
 */

data class LocationDC(val brand: String?,
                      val ownerUID: String?,
                      var address: String?,
                      var description: String?,
                      var menu : MenuDC?){

}
