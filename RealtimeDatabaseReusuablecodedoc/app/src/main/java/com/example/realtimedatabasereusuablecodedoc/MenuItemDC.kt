package com.example.realtimedatabasereusuablecodedoc
//Data class for individual items in menus
data class MenuItemDC(var name: String,
                      var description: String?,
                      var price: Float,
                      val ownerUID: String?){

}
