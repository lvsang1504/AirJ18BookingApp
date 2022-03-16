package com.devpro.airj18bookingapp.listeners;

import com.devpro.airj18bookingapp.models.Category;

import java.util.List;

public interface CategoryResponseListener {
    void didFetch(List<Category> response, String message);
    void didError(String message);
}
