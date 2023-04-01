package com.example.coupongeneration;

public class GenCoupon {

    Double discountedPrice;
    Double quantityNeeded;
    int usesBeforeExpiration;
    int currentUses;
    String expirationDate;

    public GenCoupon(Double discountedPrice, Double quantityNeeded, int usesBeforeExpiration, int currentUses, String expirationDate) {
        this.discountedPrice = discountedPrice;
        this.quantityNeeded = quantityNeeded;
        this.usesBeforeExpiration = usesBeforeExpiration;
        this.currentUses = currentUses;
        this.expirationDate = expirationDate;
    }

    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public Double getQuantityNeeded() {
        return quantityNeeded;
    }

    public void setQuantityNeeded(Double quantityNeeded) {
        this.quantityNeeded = quantityNeeded;
    }

    public int getUsesBeforeExpiration() {
        return usesBeforeExpiration;
    }

    public void setUsesBeforeExpiration(int usesBeforeExpiration) {
        this.usesBeforeExpiration = usesBeforeExpiration;
    }

    public int getCurrentUses() {
        return currentUses;
    }

    public void setCurrentUses(int currentUses) {
        this.currentUses = currentUses;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
