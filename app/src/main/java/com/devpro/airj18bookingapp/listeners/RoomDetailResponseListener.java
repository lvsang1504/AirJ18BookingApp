package com.devpro.airj18bookingapp.listeners;

import com.devpro.airj18bookingapp.models.Room;
import com.devpro.airj18bookingapp.models.RoomDetail;

import java.util.List;

public interface RoomDetailResponseListener {
    void didFetch(RoomDetail response, String message);
    void didError(String message);
}
