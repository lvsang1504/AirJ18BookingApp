package com.devpro.airj18bookingapp.listeners;

import com.devpro.airj18bookingapp.models.UserResponse;

public interface RegisterResponseListener {
    void didFetch(UserResponse userResponse, String message);
    void didError(String message);
}
