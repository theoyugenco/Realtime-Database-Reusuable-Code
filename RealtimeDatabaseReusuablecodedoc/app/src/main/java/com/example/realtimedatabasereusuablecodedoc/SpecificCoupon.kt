package com.example.realtimedatabasereusuablecodedoc

data class SpecificCoupon(var couponFor: String? = null,
                          var discountedPrice: Double? = null,
                          var quantityNeeded: Int = 0,
                          var usesBeforeExpiration: Int = 0,
                          var currentUses: Int = 0,
                          var expirationDate: String? = null
) : ListItem {
    @JvmName("getCouponFor1")
    fun getCouponFor(): String? {
        return couponFor
    }

    @JvmName("setCouponFor1")
    fun setCouponFor(couponFor: String?) {
        this.couponFor = couponFor
    }

    @JvmName("getDiscountedPrice1")
    fun getDiscountedPrice(): Double? {
        return discountedPrice
    }

    @JvmName("setDiscountedPrice1")
    fun setDiscountedPrice(discountedPrice: Double?) {
        this.discountedPrice = discountedPrice
    }

    @JvmName("getQuantityNeeded1")
    fun getQuantityNeeded(): Int {
        return quantityNeeded
    }

    @JvmName("setQuantityNeeded1")
    fun setQuantityNeeded(quantityNeeded: Int) {
        this.quantityNeeded = quantityNeeded
    }

    @JvmName("getUsesBeforeExpiration1")
    fun getUsesBeforeExpiration(): Int {
        return usesBeforeExpiration
    }

    @JvmName("setUsesBeforeExpiration1")
    fun setUsesBeforeExpiration(usesBeforeExpiration: Int) {
        this.usesBeforeExpiration = usesBeforeExpiration
    }

    @JvmName("getCurrentUses1")
    fun getCurrentUses(): Int {
        return currentUses
    }

    @JvmName("setCurrentUses1")
    fun setCurrentUses(currentUses: Int) {
        this.currentUses = currentUses
    }

    @JvmName("getExpirationDate1")
    fun getExpirationDate(): String? {
        return expirationDate
    }

    @JvmName("setExpirationDate1")
    fun setExpirationDate(expirationDate: String?) {
        this.expirationDate = expirationDate
    }

    override fun getListItemType(): Int {
        return ListItem.Type.SpecificCoupon.ordinal
    }
}

