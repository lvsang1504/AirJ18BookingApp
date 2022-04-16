package com.devpro.airj18bookingapp.models.DTO;


public class BookingDetailDTO {
    private float id;
    private String bookingDate;
    private String currencySymbol;
    private float totalFee;
    private float lastUpdated;


    // Getter Methods

    public float getId() {
        return id;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public float getTotalFee() {
        return totalFee;
    }

    public float getLastUpdated() {
        return lastUpdated;
    }

    // Setter Methods

    public void setId(float id) {
        this.id = id;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public void setTotalFee(float totalFee) {
        this.totalFee = totalFee;
    }

    public void setLastUpdated(float lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
