package com.devpro.airj18bookingapp.listeners;

import com.devpro.airj18bookingapp.models.WishlistListIdResponse;

public interface WishlistListResponseListener {
    void didFetch(WishlistListIdResponse response, String message);
    void didError(String message);
}
