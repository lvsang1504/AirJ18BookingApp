package com.devpro.airj18bookingapp.models.Response;

import com.devpro.airj18bookingapp.models.DTO.BookingDetailDTO;
import com.devpro.airj18bookingapp.models.User;

public class BookingResponse {
    public boolean success;
    public BookingDetailDTO data;
    public Object error;
}
