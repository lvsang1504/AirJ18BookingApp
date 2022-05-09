package com.devpro.airj18bookingapp.listeners;

import com.devpro.airj18bookingapp.models.WishlistResponse;

public interface WishlistEventResponseListener {
    void didFetch(WishlistResponse response, String message);
    void didError(String message);
}
