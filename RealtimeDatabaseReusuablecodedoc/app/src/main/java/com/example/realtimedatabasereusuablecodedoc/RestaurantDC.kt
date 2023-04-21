package com.example.realtimedatabasereusuablecodedoc

import android.os.Parcel
import android.os.Parcelable
import java.io.FileDescriptor

data class RestaurantDC( var name: String? = "",
                         var streetAddress: String? = "",
                         var city: String? = "",
                         var state: String? = "",
                         var zipcode: String? = "",
                         var description: String? = "",
                         var restaurantID: String? = "",
                         var merchantID: String? = "",
                         var activeMenu: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(streetAddress)
        parcel.writeString(city)
        parcel.writeString(state)
        parcel.writeString(zipcode)
        parcel.writeString(description)
        parcel.writeString(restaurantID)
        parcel.writeString(merchantID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RestaurantDC> {
        override fun createFromParcel(parcel: Parcel): RestaurantDC {
            return RestaurantDC(parcel)
        }

        override fun newArray(size: Int): Array<RestaurantDC?> {
            return arrayOfNulls(size)
        }
    }
}
