package com.devpro.airj18bookingapp.listeners;

import com.devpro.airj18bookingapp.models.BookingResponseDetail;

public interface BookingResponseDetailListener {
        void didFetch(BookingResponseDetail response, String message);
        void didError(String message);
}
