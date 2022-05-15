package com.devpro.airj18bookingapp.listeners;

import com.devpro.airj18bookingapp.models.BookedRoom;

public interface HistoryBookingListener {
    void onBookingDetailClicked(String id);
    void onBookingGetInvoiceClicked(BookedRoom bookedRoom);
}
