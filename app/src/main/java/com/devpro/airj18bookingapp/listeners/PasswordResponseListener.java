package com.devpro.airj18bookingapp.listeners;

import com.devpro.airj18bookingapp.models.Response.PasswordResponse;

public interface PasswordResponseListener {
    void didFetch(PasswordResponse passwordResponse, String message);
    void didError(String message);
}
