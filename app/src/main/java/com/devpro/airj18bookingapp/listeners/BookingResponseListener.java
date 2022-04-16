package com.devpro.airj18bookingapp.listeners;


import com.devpro.airj18bookingapp.models.Response.BookingResponse;

public interface BookingResponseListener {
    void didFetch(BookingResponse response, String message);
    void didError(String message);
}
