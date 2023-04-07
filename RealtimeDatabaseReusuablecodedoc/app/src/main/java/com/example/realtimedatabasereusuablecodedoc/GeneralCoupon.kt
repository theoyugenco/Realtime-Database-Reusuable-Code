package com.example.realtimedatabasereusuablecodedoc

data class GeneralCoupon(var discountedPrice: Double? = null,
                         var quantityNeeded: Double? = null,
                         var usesBeforeExpiration: Int = 0,
                         var currentUses: Int = 0,
                         var expirationDate: String? = null
) : ListItem {
    @JvmName("getDiscountedPrice1")
    fun getDiscountedPrice(): Double? {
        return discountedPrice
    }
    @JvmName("setDiscountedPrice1")
    fun setDiscountedPrice(discountedPrice: Double?) {
        this.discountedPrice = discountedPrice
    }

    @JvmName("getQuantityNeeded1")
    fun getQuantityNeeded(): Double? {
        return quantityNeeded
    }

    @JvmName("setQuantityNeeded1")
    fun setQuantityNeeded(quantityNeeded: Double?) {
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
        return ListItem.Type.GeneralCoupon.ordinal
    }
}

