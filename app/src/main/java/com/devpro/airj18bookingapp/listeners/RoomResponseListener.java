package com.devpro.airj18bookingapp.listeners;

import com.devpro.airj18bookingapp.models.Room;

import java.util.List;

public interface RoomResponseListener {
    void didFetch(List<Room> response, String message);
    void didError(String message);
}
