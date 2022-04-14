package com.devpro.airj18bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.utils.Constants;
import com.devpro.airj18bookingapp.utils.PreferenceManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    ImageView profile_back;
    RoundedImageView profile_image;
    ImageButton profile_signout;
    TextView profile_username, profile_name, profile_email;
    FrameLayout linear_info;
    LinearLayout linear;

    private PreferenceManager preferenceManager;
    Animation anim_from_button, anim_from_top, anim_from_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        preferenceManager = new PreferenceManager(ProfileActivity.this);
        getViews();

        loadUserDetails();

        setListeners();


        //Load Animations
        anim_from_button = AnimationUtils.loadAnimation(this, R.anim.anim_from_bottom);
        anim_from_top = AnimationUtils.loadAnimation(this, R.anim.anim_from_top);
        anim_from_left = AnimationUtils.loadAnimation(this, R.anim.anim_from_left);

        profile_image.setAnimation(anim_from_top);
        profile_back.setAnimation(anim_from_top);
        profile_username.setAnimation(anim_from_top);
        linear_info.setAnimation(anim_from_left);
        profile_signout.setAnimation(anim_from_button);
        linear.setAnimation(anim_from_button);

        //Hide status bar and navigation bar at the bottom
        this.getWindow().setFlags(
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

    private void setListeners() {
        profile_signout.setOnClickListener(v -> signOut());
        profile_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadUserDetails() {
        profile_email.setText(preferenceManager.getString(Constants.KEY_EMAIL));
        profile_username.setText(preferenceManager.getString(Constants.KEY_NAME));
        profile_name.setText(preferenceManager.getString(Constants.KEY_NAME));
        if(preferenceManager.getString(Constants.KEY_IMAGE)!=null){
            profile_name.setText(preferenceManager.getString(Constants.KEY_NAME));
            byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            profile_image.setImageBitmap(bitmap);
        }
    }

    private void signOut() {
        showToast("Signing out ...");
        preferenceManager.clear();
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USER).document(preferenceManager.getString(Constants.KEY_USER_ID));
//        HashMap<String, Object> updates = new HashMap<>();
//        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
//        documentReference.update(updates)
//                .addOnSuccessListener(unused -> {
//                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
//                })
//                .addOnFailureListener(e -> showToast("Unable to sign out"));
    }

    private void getViews() {
        profile_back = findViewById(R.id.profile_back);
        profile_image = findViewById(R.id.profile_image);
        profile_signout = findViewById(R.id.profile_signout);
        profile_username = findViewById(R.id.profile_username);
        profile_name = findViewById(R.id.profile_name);
        profile_email = findViewById(R.id.profile_email);
        linear_info = findViewById(R.id.linear_info);
        linear = findViewById(R.id.linear);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void backClick(View view) {
        onBackPressed();
    }
}