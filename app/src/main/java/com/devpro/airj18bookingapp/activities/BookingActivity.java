package com.devpro.airj18bookingapp.activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.listeners.BookingResponseListener;
import com.devpro.airj18bookingapp.listeners.LoginResponseListener;
import com.devpro.airj18bookingapp.listeners.RoomDetailResponseListener;
import com.devpro.airj18bookingapp.models.DTO.BookingDetailDTO;
import com.devpro.airj18bookingapp.models.Response.BookingResponse;
import com.devpro.airj18bookingapp.models.RoomDetail;
import com.devpro.airj18bookingapp.models.RoomDetailResponse;
import com.devpro.airj18bookingapp.models.UserResponse;
import com.devpro.airj18bookingapp.repository.RequestManager;
import com.devpro.airj18bookingapp.utils.Constants;
import com.devpro.airj18bookingapp.utils.PreferenceManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class BookingActivity extends AppCompatActivity {

    static TextView txtCheckin, txtCheckout, btn_proceed;
    Button btnSetCheckin, btnSetCheckout;
    RoomDetail roomDetail;
    RequestManager manager;
    int numberOfDays = 0;
    static Date dateCheckin, dateCheckout;
    private PreferenceManager preferenceManager;
    BookingDetailDTO bookingDetailDTO;

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        int flag = 0;

        public DatePickerFragment(int flag) {
            this.flag = flag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            if (flag == 1) {
                String sDate = day + "-" + month + "-" + year;
                try {
                    dateCheckin = new SimpleDateFormat("dd-MM-yyyy").parse(sDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                txtCheckin.setText(day + "-" + month + "-" + year);
            } else {
                String sDate = day + "-" + month + "-" + year;
                try {
                    dateCheckout = new SimpleDateFormat("dd-MM-yyyy").parse(sDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                txtCheckout.setText(day + "-" + month + "-" + year);

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        String json = getIntent().getStringExtra("room_detail");
        Gson gson = new Gson();
        roomDetail = gson.fromJson(json, RoomDetail.class);
        preferenceManager = new PreferenceManager(BookingActivity.this);
        setControl();
        setEvent();
        getViews();


        //Hide status bar and navigation bar at the bottom
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        this.getWindow().getDecorView().setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void setEvent() {
        btnSetCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(1);
            }
        });
        btnSetCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(2);
            }
        });
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kiem tra
                int daysdiff = 0;
                long diff = dateCheckout.getTime() - dateCheckin.getTime();
                long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
                daysdiff = (int) diffDays;
                numberOfDays = daysdiff;

                String cookie = preferenceManager.getString(Constants.KEY_COOKIE);
                String cookie1 = cookie;
                manager = new RequestManager(BookingActivity.this);
                manager.getBooking(bookingResponseListener, roomDetail.id, txtCheckin.getText().toString(), txtCheckout.getText().toString(), numberOfDays, cookie);


            }
        });
    }

    private void setControl() {
        txtCheckin = findViewById(R.id.txtCheckin);
        txtCheckout = findViewById(R.id.txtCheckout);
        btnSetCheckin = findViewById(R.id.btnSetCheckin);
        btnSetCheckout = findViewById(R.id.btnSetCheckout);
        btn_proceed = findViewById(R.id.btn_proceed);
    }

    public void showDatePickerDialog(int flag) {
        DialogFragment newFragment = new DatePickerFragment(flag);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void getViews() {
    }

    public void backClick(View view) {
        onBackPressed();
    }

    private final BookingResponseListener bookingResponseListener = new BookingResponseListener() {
        @Override
        public void didFetch(BookingResponse response, String message) {
            Toast.makeText(BookingActivity.this, "Booking success", Toast.LENGTH_LONG).show();
            bookingDetailDTO = response.data;
            Intent intent = new Intent(BookingActivity.this, BookingFinishActivity.class);
            Gson gson = new Gson();
            String myJson = gson.toJson(bookingDetailDTO);
            intent.putExtra("bookingDetailDTO", myJson);
            startActivity(intent);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(BookingActivity.this, message + " booking fail", Toast.LENGTH_LONG).show();
        }
    };
}
