package com.devpro.airj18bookingapp.listeners;

import com.devpro.airj18bookingapp.models.WishlistResponseData;

public interface WishlistListAllResponseListener {
    void didFetch(WishlistResponseData response, String message);
    void didError(String message);
}
