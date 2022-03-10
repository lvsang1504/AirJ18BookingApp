package com.devpro.airj18bookingapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.activities.DetailsBookingActivity;
import com.devpro.airj18bookingapp.activities.ProfileActivity;
import com.devpro.airj18bookingapp.adapters.BookingAdapter;
import com.devpro.airj18bookingapp.listeners.BookingClicksListener;
import com.devpro.airj18bookingapp.models.Hotel;
import com.devpro.airj18bookingapp.utils.Constants;
import com.devpro.airj18bookingapp.utils.PreferenceManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    TextView textView, textView2, textView3, textView4, textView5;
    RoundedImageView imageProfile;
    SearchView searchView;

    Animation anim_from_button, anim_from_top, anim_from_left;
    RecyclerView recyclerView;
    BookingAdapter adapter;
    List<Hotel> list = new ArrayList<>();

    private PreferenceManager preferenceManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        list.add(new Hotel(1, "RedDoorz Chau Thien Tu 3 Hotel ", "https://dichvuthuexe.vn/wp-content/uploads/2020/06/dat-phong-khach-san.jpg", 4.5, 100, "demo mo mo"));
        list.add(new Hotel(1, "RedDoorz Chau Thien Tu 3 Hotel ", "https://cdnimg.vietnamplus.vn/t460/Uploaded/izhsa/2020_01_01/booking.jpg", 4.5, 100, "demo mo mo"));
        list.add(new Hotel(1, "RedDoorz Chau Thien Tu 3 Hotel ", "https://dichvuthuexe.vn/wp-content/uploads/2020/06/dat-phong-khach-san.jpg", 4.5, 100, "demo mo mo"));
        list.add(new Hotel(1, "RedDoorz Chau Thien Tu 3 Hotel ", "https://dichvuthuexe.vn/wp-content/uploads/2020/06/dat-phong-khach-san.jpg", 4.5, 100, "demo mo mo"));
        list.add(new Hotel(1, "RedDoorz Chau Thien Tu 3 Hotel ", "https://dichvuthuexe.vn/wp-content/uploads/2020/06/dat-phong-khach-san.jpg", 4.5, 100, "demo mo mo"));
        list.add(new Hotel(1, "RedDoorz Chau Thien Tu 3 Hotel ", "https://dichvuthuexe.vn/wp-content/uploads/2020/06/dat-phong-khach-san.jpg", 4.5, 100, "demo mo mo"));
        list.add(new Hotel(1, "RedDoorz Chau Thien Tu 3 Hotel ", "https://dichvuthuexe.vn/wp-content/uploads/2020/06/dat-phong-khach-san.jpg", 4.5, 100, "demo mo mo"));
        list.add(new Hotel(1, "RedDoorz Chau Thien Tu 3 Hotel ", "https://dichvuthuexe.vn/wp-content/uploads/2020/06/dat-phong-khach-san.jpg", 4.5, 100, "demo mo mo"));
        list.add(new Hotel(1, "RedDoorz Chau Thien Tu 3 Hotel ", "https://dichvuthuexe.vn/wp-content/uploads/2020/06/dat-phong-khach-san.jpg", 4.5, 100, "demo mo mo"));
        list.add(new Hotel(1, "RedDoorz Chau Thien Tu 3 Hotel ", "https://dichvuthuexe.vn/wp-content/uploads/2020/06/dat-phong-khach-san.jpg", 4.5, 100, "demo mo mo"));
        list.add(new Hotel(1, "RedDoorz Chau Thien Tu 3 Hotel ", "https://dichvuthuexe.vn/wp-content/uploads/2020/06/dat-phong-khach-san.jpg", 4.5, 100, "demo mo mo"));

        getViews(view);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));

        adapter = new BookingAdapter(getContext(), list, bookingClicksListener);
        recyclerView.setAdapter(adapter);
        /////////////////

        preferenceManager = new PreferenceManager(getContext());

        loadUserDetails();



        //Load Animations
        anim_from_button = AnimationUtils.loadAnimation(getContext(), R.anim.anim_from_bottom);
        anim_from_top = AnimationUtils.loadAnimation(getContext(), R.anim.anim_from_top);
        anim_from_left = AnimationUtils.loadAnimation(getContext(), R.anim.anim_from_left);

        textView.setAnimation(anim_from_top);
        textView2.setAnimation(anim_from_top);
        textView3.setAnimation(anim_from_top);
        textView4.setAnimation(anim_from_top);
        textView5.setAnimation(anim_from_top);
        searchView.setAnimation(anim_from_left);


        //Hide status bar and navigation bar at the bottom
        getActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        getActivity().getWindow().getDecorView().setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        );

        return view;
    }

    private void getViews(View view) {
        textView = view.findViewById(R.id.firstText);
        textView2 = view.findViewById(R.id.textView);
        textView3 = view.findViewById(R.id.textView2);
        textView4 = view.findViewById(R.id.textView3);
        textView5 = view.findViewById(R.id.textView4);
        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recycle_booking_list);
        imageProfile = view.findViewById(R.id.imageProfile);
    }

    private void loadUserDetails() {
        //binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageProfile.setImageBitmap(bitmap);

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });
    }

    private final BookingClicksListener bookingClicksListener = new BookingClicksListener() {

        @Override
        public void onBookingClicked(String id) {
            startActivity(new Intent(getActivity(), DetailsBookingActivity.class)
                    .putExtra("id", id));
        }
    };
}