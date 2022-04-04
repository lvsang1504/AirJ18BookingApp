package com.devpro.airj18bookingapp.listeners;

import com.devpro.airj18bookingapp.models.Room;
import com.devpro.airj18bookingapp.models.RoomResponse;

import java.util.List;

public interface RoomResponseListener {
    void didFetch(RoomResponse response, String message);
    void didError(String message);
}
