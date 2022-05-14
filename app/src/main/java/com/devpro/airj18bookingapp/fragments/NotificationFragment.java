package com.devpro.airj18bookingapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.devpro.airj18bookingapp.R;

import com.devpro.airj18bookingapp.adapters.HistoryBookingAdapter;
import com.devpro.airj18bookingapp.adapters.PostAdapter;
import com.devpro.airj18bookingapp.listeners.BookingResponseDetailListener;
import com.devpro.airj18bookingapp.models.BookingResponseDetail;


import com.devpro.airj18bookingapp.listeners.BookingResponseDetailListener;
import com.devpro.airj18bookingapp.models.BookingResponseDetail;
import com.devpro.airj18bookingapp.models.Notification;
import com.devpro.airj18bookingapp.repository.RequestManager;
import com.devpro.airj18bookingapp.repository.notifications.ApiClient;
import com.devpro.airj18bookingapp.repository.notifications.ApiService;
import com.devpro.airj18bookingapp.utils.Constants;
import com.devpro.airj18bookingapp.utils.PreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {

    private PostAdapter _mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private PreferenceManager preferenceManager;
    RecyclerView recyclerView;
    LinearLayout lvl_not_found_notification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseMessaging.subscribeToTopic("");

        preferenceManager = new PreferenceManager(getContext());
        layoutManager = new LinearLayoutManager(getContext());
        getViews(view);

        recyclerView.setFocusable(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        getToken();

        getDataHistoryNotification();

        return view;
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

                Collections.reverse(notifications);

                if (notifications.isEmpty()) {
                    lvl_not_found_notification.setVisibility(View.VISIBLE);
                } else {
                    lvl_not_found_notification.setVisibility(View.GONE);
                }

                _mAdapter = new PostAdapter(notifications);
                recyclerView.setAdapter(_mAdapter);

                Log.d("token", preferenceManager.getString(Constants.KEY_FCM_TOKEN));

                try {
                    JSONArray tokens = new JSONArray();
                    tokens.put(preferenceManager.getString(Constants.KEY_FCM_TOKEN));

                    JSONObject data = new JSONObject();
                    data.put(Constants.KEY_MESSAGE, notifications.get(0).content);

                    JSONObject body = new JSONObject();
                    body.put(Constants.REMOTE_MSG_DATA, data);
                    body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

                    System.out.println(body.toString());

                    sendNotification(body.toString());

                } catch (Exception e) {
                    showToast(e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String messageBody) {
        ApiClient.getClient().create(ApiService.class).sendMessage(
                Constants.getRemoteMsgHeaders(),
                messageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            JSONObject responseJson = new JSONObject(response.body());
                            JSONArray results = responseJson.getJSONArray("results");
                            if (responseJson.getInt("failure") == 1) {
                                JSONObject error = (JSONObject) results.get(0);
                                showToast(error.getString("error"));
                                return;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showToast("Notification sent successfully!");
                } else {
                    showToast("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                showToast(t.getMessage());
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
    }

    private void getViews(View view) {
        recyclerView = view.findViewById(R.id.recycle_notification_list);
        lvl_not_found_notification = view.findViewById(R.id.lvl_notfound_notification);
    }


}