package com.example.realtimedatabasereusuablecodedoc

/*
Kenneth Valero
Interface to separate coupon types
 */
interface ListItem {
    enum class Type(value: Int) {GeneralCoupon(0), SpecificCoupon(1) }
    fun getListItemType(): Int
}