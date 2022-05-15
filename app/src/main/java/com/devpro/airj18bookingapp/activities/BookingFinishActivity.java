package com.devpro.airj18bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.models.DTO.BookingDetailDTO;
import com.devpro.airj18bookingapp.models.RoomDetail;
import com.google.gson.Gson;

public class BookingFinishActivity extends AppCompatActivity {

    BookingDetailDTO bookingDetailDTO;
    TextView txt_orderdate,txt_orderid,txt_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_finish);
        setControl();
        String json = getIntent().getStringExtra("bookingDetailDTO");
        Gson gson = new Gson();
        bookingDetailDTO = gson.fromJson(json, BookingDetailDTO.class);
        setData();
        setEvent();
    }

    private void setEvent() {
        txt_home.setOnClickListener(view -> {
            startActivity(new Intent(this,MainActivity.class));
        });
    }

    private void setData() {
        txt_orderid.setText(bookingDetailDTO.getId()+"");
        txt_orderdate.setText(bookingDetailDTO.getBookingDate());
    }

    private void setControl() {
        txt_orderid=findViewById(R.id.txt_orderid);
        txt_orderdate=findViewById(R.id.txt_orderdate);
        txt_home=findViewById(R.id.txt_home);
    }

    public void backClick(View view) {
        onBackPressed();
    }
}