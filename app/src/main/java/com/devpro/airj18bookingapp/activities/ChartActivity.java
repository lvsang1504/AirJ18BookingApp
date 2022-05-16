package com.devpro.airj18bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.devpro.airj18bookingapp.adapters.HistoryBookingAdapter;
import com.devpro.airj18bookingapp.listeners.BookingResponseDetailListener;
import com.devpro.airj18bookingapp.models.BookedRoom;
import com.devpro.airj18bookingapp.models.BookingResponseDetail;
import com.devpro.airj18bookingapp.repository.RequestManager;
import com.devpro.airj18bookingapp.utils.Constants;
import com.devpro.airj18bookingapp.utils.PreferenceManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import com.devpro.airj18bookingapp.R;

public class ChartActivity extends AppCompatActivity {

    // variable for our bar chart
    BarChart barChart;

    // variable for our bar data.
    BarData barData;

    // variable for our bar data set.
    BarDataSet barDataSet;

    // array list for storing entries.
    ArrayList barEntriesArrayList;

    RequestManager manager;

    private PreferenceManager preferenceManager;

    String cookie;

    ArrayList<String> labels=new ArrayList<>();

    ImageView btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);


        preferenceManager = new PreferenceManager(this);
        cookie = preferenceManager.getString(Constants.KEY_COOKIE);
        manager = new RequestManager(this);
        barEntriesArrayList = new ArrayList<>();


        btn_back=findViewById(R.id.iv_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // initializing variable for bar chart.
        barChart = findViewById(R.id.idBarChart);


        // calling method to get bar entries.

        manager.getBookingResponseDetail(bookingResponseDetailListener, cookie);



        getBarEntries();




    }

    private final BookingResponseDetailListener bookingResponseDetailListener = new BookingResponseDetailListener() {
        @Override
        public void didFetch(BookingResponseDetail response, String message) {
            float i=1f;
            for (BookedRoom a:response.data.bookedRooms) {
                barEntriesArrayList.add(new BarEntry(i, (float) (a.pricePerDay*a.numberOfDays)));
                i++;
            }

            Log.d("OOOO", barEntriesArrayList.size()+"");
            getBarEntries();

            barChart.invalidate();
        }

        @Override
        public void didError(String message) {
            Toast.makeText(ChartActivity.this, message, Toast.LENGTH_LONG).show();
        }
    };



    public void getBarEntries() {
        // creating a new array list

        barDataSet = new BarDataSet(barEntriesArrayList, "");

        // creating a new bar data and
        // passing our bar data set.
        barData = new BarData(barDataSet);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        // below line is to set data
        // to our bar chart.
        barChart.setData(barData);

        // adding color to our bar data set.
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        // setting text color.
        barDataSet.setValueTextColor(Color.BLACK);

        // setting text size
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(false);

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
//        barEntriesArrayList.add(new BarEntry(1f, 4));
//        barEntriesArrayList.add(new BarEntry(2f, 6));
//        barEntriesArrayList.add(new BarEntry(3f, 8));
//        barEntriesArrayList.add(new BarEntry(4f, 2));
//        barEntriesArrayList.add(new BarEntry(5f, 4));
//        barEntriesArrayList.add(new BarEntry(6f, 1));


    }
}