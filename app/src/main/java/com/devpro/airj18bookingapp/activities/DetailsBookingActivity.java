package com.devpro.airj18bookingapp.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.listeners.RoomDetailResponseListener;
import com.devpro.airj18bookingapp.models.RoomDetail;
import com.devpro.airj18bookingapp.repository.RequestManager;
import com.devpro.airj18bookingapp.utils.Constants;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class DetailsBookingActivity extends AppCompatActivity {

    ImageView second_back_arrow, second_arrow_up, image_background;
    TextView second_title, second_subtitle, second_rating_number, second_rating_number2, more_details;
    RatingBar second_ratingbar;

    Animation from_left, from_right, from_bottom;

    RequestManager manager;

    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_booking);

        getViews();

        id = getIntent().getStringExtra("id");


        manager = new RequestManager(DetailsBookingActivity.this);
        manager.getRoomDetail(roomDetailResponseListener, Integer.parseInt(id));


        second_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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


        //Load Animations
        from_left = AnimationUtils.loadAnimation(this, R.anim.anim_from_left);
        from_right = AnimationUtils.loadAnimation(this, R.anim.anim_from_right);
        from_bottom = AnimationUtils.loadAnimation(this, R.anim.anim_from_bottom);


        //Set Animations
        second_back_arrow.setAnimation(from_left);
        second_title.setAnimation(from_right);
        second_subtitle.setAnimation(from_right);
        second_ratingbar.setAnimation(from_left);
        second_rating_number.setAnimation(from_right);
        second_rating_number2.setAnimation(from_right);
        second_arrow_up.setAnimation(from_bottom);
        more_details.setAnimation(from_bottom);



    }

    private final RoomDetailResponseListener roomDetailResponseListener = new RoomDetailResponseListener() {
        @Override
        public void didFetch(RoomDetail response, String message) {
            second_title.setText(response.name);
            second_subtitle.setText(response.description);
            second_rating_number2.setText(response.reviews.size() + "");
            Picasso.get().load(Constants.BASE_URL + response.thumbnail).into(image_background);

            second_arrow_up.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailsBookingActivity.this, ShowMoreActivity.class);

                    Pair[] pairs = new Pair[1];
                    pairs[0] = new Pair<View, String>(second_arrow_up, "background_image_transition");


                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(DetailsBookingActivity.this, pairs);

                    Gson gson = new Gson();
                    String myJson = gson.toJson(response);
                    intent.putExtra("room_detail", myJson);

                    startActivity(intent, options.toBundle());
                }
            });
        }

        @Override
        public void didError(String message) {
            Toast.makeText(DetailsBookingActivity.this, message, Toast.LENGTH_LONG).show();
        }
    };

    void getViews() {
        second_back_arrow = findViewById(R.id.second_back_arrow);
        second_arrow_up = findViewById(R.id.seconf_arrow_up);
        second_title = findViewById(R.id.second_title);
        second_subtitle = findViewById(R.id.second_subtitle);
        second_rating_number = findViewById(R.id.second_rating_number);
        second_rating_number2 = findViewById(R.id.second_rating_number2);
        more_details = findViewById(R.id.more_details);
        second_ratingbar = findViewById(R.id.second_ratingbar);
        image_background = findViewById(R.id.image_background);
    }
}