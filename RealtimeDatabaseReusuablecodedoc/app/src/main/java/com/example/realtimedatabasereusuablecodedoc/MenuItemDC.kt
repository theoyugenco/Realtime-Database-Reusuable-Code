package com.example.realtimedatabasereusuablecodedoc
//Data class for individual items in menus
data class MenuItemDC(var name: String? = null,
                      var price: String? = null,
                      var description: String? = null,
                      val key: String? = null,
                      val ownerUID: String? = null){

}
