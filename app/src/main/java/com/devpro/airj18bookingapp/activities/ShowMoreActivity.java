package com.devpro.airj18bookingapp.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.models.RoomDetail;
import com.devpro.airj18bookingapp.utils.Constants;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class ShowMoreActivity extends AppCompatActivity {


    ImageView down_arrow, header_background;
    TextView third_title, third_rating_number,about_text;
    Button roadmap_button;

    LinearLayout btnBooking;

    ScrollView third_scrollview;

    Animation from_bottom;

    RoomDetail roomDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_more);

        String json = getIntent().getStringExtra("room_detail");

        Gson gson = new Gson();
        roomDetail = gson.fromJson(json, RoomDetail.class);

        down_arrow = findViewById(R.id.down_arrow);
        third_scrollview = findViewById(R.id.third_scrillview);
        header_background = findViewById(R.id.header_background);
        third_title = findViewById(R.id.third_title);
        third_rating_number = findViewById(R.id.third_rating_number);
        about_text = findViewById(R.id.about_text);
        roadmap_button = findViewById(R.id.roadmap_button);
        btnBooking = findViewById(R.id.btnBooking);

        Picasso.get().load(Constants.BASE_URL + roomDetail.thumbnail).into(header_background);
        third_title.setText(roomDetail.name);
        third_rating_number.setText(roomDetail.averageRating + "");
        about_text.setText(roomDetail.description);



        from_bottom = AnimationUtils.loadAnimation(this, R.anim.anim_from_bottom);
        down_arrow.setAnimation(from_bottom);
        third_scrollview.setAnimation(from_bottom);
        btnBooking.setAnimation(from_bottom);

        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowMoreActivity.this, BookingActivity.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(roomDetail);
                intent.putExtra("room_detail", myJson);
                startActivity(intent);
            }
        });

        roadmap_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowMoreActivity.this, MapViewActivity.class);

                Gson gson = new Gson();
                String myJson = gson.toJson(roomDetail);
                intent.putExtra("room_detail", myJson);

                startActivity(intent);
            }
        });


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


        down_arrow.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}