package com.devpro.airj18bookingapp.listeners;

public interface BaseResponseListener<T> {
    void didFetch(T response);
    void didError(int code, String message);
}
