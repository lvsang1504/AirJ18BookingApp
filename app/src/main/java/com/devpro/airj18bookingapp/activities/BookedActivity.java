package com.devpro.airj18bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.adapters.HistoryBookingAdapter;
import com.devpro.airj18bookingapp.listeners.BookingResponseDetailListener;
import com.devpro.airj18bookingapp.models.BookingResponseDetail;
import com.devpro.airj18bookingapp.repository.RequestManager;
import com.devpro.airj18bookingapp.utils.Constants;
import com.devpro.airj18bookingapp.utils.PreferenceManager;

public class BookedActivity extends AppCompatActivity {
    RecyclerView rv_history_booking;
    RequestManager manager;

    private PreferenceManager preferenceManager;

    String cookie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked);
        preferenceManager = new PreferenceManager(this);
        cookie = preferenceManager.getString(Constants.KEY_COOKIE);
        manager = new RequestManager(this);

        manager.getBookingResponseDetail(bookingResponseDetailListener, cookie);

        // Inflate the layout for this fragment
        setViews();
        rv_history_booking.setHasFixedSize(true);
        rv_history_booking.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
    private void setViews() {
        rv_history_booking=findViewById(R.id.rv_history_booking);
    }
    private final BookingResponseDetailListener bookingResponseDetailListener = new BookingResponseDetailListener() {
        @Override
        public void didFetch(BookingResponseDetail response, String message) {
            HistoryBookingAdapter adapter=new HistoryBookingAdapter(response.data.bookedRooms);
            rv_history_booking.setAdapter(adapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(BookedActivity.this, message, Toast.LENGTH_LONG).show();
        }
    };
}