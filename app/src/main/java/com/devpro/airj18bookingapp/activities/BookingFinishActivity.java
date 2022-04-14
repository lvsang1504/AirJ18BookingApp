package com.devpro.airj18bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.devpro.airj18bookingapp.R;

public class BookingFinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_finish);
    }

    public void backClick(View view) {
        onBackPressed();
    }
}