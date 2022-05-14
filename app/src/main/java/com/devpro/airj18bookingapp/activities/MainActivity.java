package com.devpro.airj18bookingapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.adapters.PostAdapter;
import com.devpro.airj18bookingapp.fragments.FavoriteFragment;
import com.devpro.airj18bookingapp.fragments.HomeFragment;
import com.devpro.airj18bookingapp.fragments.NotificationFragment;
import com.devpro.airj18bookingapp.fragments.Settings2Fragment;
import com.devpro.airj18bookingapp.fragments.SettingsFragment;
import com.devpro.airj18bookingapp.models.Notification;
import com.devpro.airj18bookingapp.utils.Constants;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    Settings2Fragment settingsFragment = new Settings2Fragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    FavoriteFragment favoriteFragment = new FavoriteFragment();
    Locale mLocale;
    BadgeDrawable badgeDrawable;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(LocaleList.getDefault().get(0).getLanguage().equals("en")){
            mLocale=new Locale("en");
        }else {
            mLocale=new Locale("vi");
        }

        if(this.getResources().getConfiguration().locale.equals("en")){

        }else {

        }

        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        this.getResources().updateConfiguration(config,
                this.getResources().getDisplayMetrics());
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.notification);



        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;
                    case R.id.notification:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, notificationFragment).commit();
                        return true;
                    case R.id.favorite:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, favoriteFragment).commit();
                        return true;
                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, settingsFragment).commit();
                        return true;
                }

                return false;
            }
        });

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

    private void getDataHistoryNotification() {

        DatabaseReference fallDetector = FirebaseDatabase
                .getInstance("https://airj18-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Notifications");
        Query fallDetectQuery = fallDetector.orderByKey();
        fallDetectQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Notification> notifications = new ArrayList<>();


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    notifications.add(postSnapshot.getValue(Notification.class));
                }

                badgeDrawable.setVisible(true);
                badgeDrawable.setNumber(notifications.size());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}