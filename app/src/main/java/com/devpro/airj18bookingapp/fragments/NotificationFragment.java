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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);




        return view;
    }




}