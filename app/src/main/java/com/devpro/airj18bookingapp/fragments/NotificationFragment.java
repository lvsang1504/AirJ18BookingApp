package com.devpro.airj18bookingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.adapters.HistoryBookingAdapter;

public class NotificationFragment extends Fragment {

    RecyclerView rv_history_booking;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_notification, container, false);
        setViews(view);
        rv_history_booking.setHasFixedSize(true);
        rv_history_booking.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        HistoryBookingAdapter adapter=new HistoryBookingAdapter();
        rv_history_booking.setAdapter(adapter);
        return view;
    }

    private void setViews(View view) {
        rv_history_booking=view.findViewById(R.id.rv_history_booking);
    }
}