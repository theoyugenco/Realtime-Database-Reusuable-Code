package com.example.realtimedatabasereusuablecodedoc;

public class SpecificCoupon implements ListItem {
    String couponFor;
    Double discountedPrice;
    int quantityNeeded;
    int usesBeforeExpiration;
    int currentUses;
    String expirationDate;

    public SpecificCoupon(String couponFor, Double discountedPrice, int quantityNeeded, int usesBeforeExpiration, int currentUses, String expirationDate) {
        this.couponFor = couponFor;
        this.discountedPrice = discountedPrice;
        this.quantityNeeded = quantityNeeded;
        this.usesBeforeExpiration = usesBeforeExpiration;
        this.currentUses = currentUses;
        this.expirationDate = expirationDate;
    }

    public String getCouponFor() {
        return couponFor;
    }

    public void setCouponFor(String couponFor) {
        this.couponFor = couponFor;
    }

    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public int getQuantityNeeded() {
        return quantityNeeded;
    }

    public void setQuantityNeeded(int quantityNeeded) {
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

    @Override
    public int getListItemType() {
        return ListItem.Type.SpecificCoupon.ordinal();
    }
}
