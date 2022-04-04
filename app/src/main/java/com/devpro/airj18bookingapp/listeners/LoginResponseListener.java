package com.devpro.airj18bookingapp.listeners;

import com.devpro.airj18bookingapp.models.UserResponse;

public interface LoginResponseListener {
    void didFetch(UserResponse userResponse, String message, String cookie);
    void didError(String message);
}
