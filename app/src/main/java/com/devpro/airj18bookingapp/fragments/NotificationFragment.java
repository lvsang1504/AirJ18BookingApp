package com.devpro.airj18bookingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devpro.airj18bookingapp.R;

import com.devpro.airj18bookingapp.adapters.HistoryBookingAdapter;
import com.devpro.airj18bookingapp.listeners.BookingResponseDetailListener;
import com.devpro.airj18bookingapp.models.BookingResponseDetail;



import com.devpro.airj18bookingapp.listeners.BookingResponseDetailListener;
import com.devpro.airj18bookingapp.models.BookingResponseDetail;
import com.devpro.airj18bookingapp.repository.RequestManager;
import com.devpro.airj18bookingapp.utils.Constants;
import com.devpro.airj18bookingapp.utils.PreferenceManager;

public class NotificationFragment extends Fragment {
    RecyclerView rv_history_booking;
    RequestManager manager;

    private PreferenceManager preferenceManager;

    String cookie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        preferenceManager = new PreferenceManager(getContext());
        cookie = preferenceManager.getString(Constants.KEY_COOKIE);
        manager = new RequestManager(getContext());

        manager.getBookingResponseDetail(bookingResponseDetailListener, cookie);

        // Inflate the layout for this fragment
        setViews(view);
        rv_history_booking.setHasFixedSize(true);
        rv_history_booking.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        return view;
    }

    private void setViews(View view) {
        rv_history_booking=view.findViewById(R.id.rv_history_booking);
    }

    private final BookingResponseDetailListener bookingResponseDetailListener = new BookingResponseDetailListener() {
        @Override
        public void didFetch(BookingResponseDetail response, String message) {
            HistoryBookingAdapter adapter=new HistoryBookingAdapter(response.data.bookedRooms);
            rv_history_booking.setAdapter(adapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    };
}