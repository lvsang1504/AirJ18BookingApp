package com.devpro.airj18bookingapp.listeners;

public interface BookingClicksListener {
    void onBookingClicked(String id);
    void onBookingFavoriteClicked(String id, boolean isLiked);
}
