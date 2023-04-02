package com.example.realtimedatabasereusuablecodedoc

interface ListItem {
    enum class Type(value: Int) {GeneralCoupon(0), SpecificCoupon(1) }
    fun getListItemType(): Int
}