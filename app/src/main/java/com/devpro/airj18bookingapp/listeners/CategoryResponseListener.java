package com.devpro.airj18bookingapp.listeners;

import com.devpro.airj18bookingapp.models.Category;
import com.devpro.airj18bookingapp.models.CategoryResponse;

import java.util.List;

public interface CategoryResponseListener {
    void didFetch(CategoryResponse response, String message);
    void didError(String message);
}
