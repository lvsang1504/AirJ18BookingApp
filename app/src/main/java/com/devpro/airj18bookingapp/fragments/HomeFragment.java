package com.devpro.airj18bookingapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.activities.DetailsBookingActivity;
import com.devpro.airj18bookingapp.activities.ProfileActivity;
import com.devpro.airj18bookingapp.adapters.BookingAdapter;
import com.devpro.airj18bookingapp.adapters.CategoryAdapter;
import com.devpro.airj18bookingapp.listeners.BookingClicksListener;
import com.devpro.airj18bookingapp.listeners.CategoryClicksListener;
import com.devpro.airj18bookingapp.listeners.CategoryResponseListener;
import com.devpro.airj18bookingapp.listeners.RoomResponseListener;
import com.devpro.airj18bookingapp.models.Category;
import com.devpro.airj18bookingapp.models.Room;
import com.devpro.airj18bookingapp.repository.RequestManager;
import com.devpro.airj18bookingapp.utils.Constants;
import com.devpro.airj18bookingapp.utils.PreferenceManager;
import com.google.android.material.tabs.TabLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class HomeFragment extends Fragment {

    TextView textView, textView2, textView3, textView4, textView5;
    RoundedImageView imageProfile;
    SearchView searchView;
    ProgressDialog dialog;
    TabLayout tabLayout;

    Animation anim_from_button, anim_from_top, anim_from_left, from_right;

    RequestManager manager;

    RecyclerView recyclerViewCategory;
    CategoryAdapter categoryAdapter;
//    List<Category> categories=new ArrayList<>();

    RecyclerView recyclerView;
    BookingAdapter adapter;

    private PreferenceManager preferenceManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        getViews(view);

        preferenceManager = new PreferenceManager(getContext());

        loadUserDetails();

        manager = new RequestManager(getContext());
        manager.getCategories(categoryResponseListener);


        //Load Animations
        anim_from_button = AnimationUtils.loadAnimation(getContext(), R.anim.anim_from_bottom);
        anim_from_top = AnimationUtils.loadAnimation(getContext(), R.anim.anim_from_top);
        anim_from_left = AnimationUtils.loadAnimation(getContext(), R.anim.anim_from_left);
        from_right = AnimationUtils.loadAnimation(getContext(), R.anim.anim_from_right);

        textView.setAnimation(anim_from_top);
        textView2.setAnimation(anim_from_top);
        textView3.setAnimation(anim_from_top);
        textView4.setAnimation(anim_from_top);
        textView5.setAnimation(anim_from_top);
        searchView.setAnimation(anim_from_left);
        tabLayout.setAnimation(from_right);
        recyclerView.setAnimation(anim_from_button);


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

    private final CategoryResponseListener categoryResponseListener = new CategoryResponseListener() {
        @Override
        public void didFetch(List<Category> response, String message) {
            for (Category category : response
            ) {
                tabLayout.addTab(tabLayout.newTab().setText(category.name));
            }

            manager.getRoomByCategory(randomRecipeResponseListener, 1);
            dialog.show();

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    manager.getRoomByCategory(randomRecipeResponseListener, tab.getPosition() + 1);
                    dialog.show();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }

        @Override
        public void didError(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    };

    private final CategoryClicksListener categoryClicksListener = new CategoryClicksListener() {
        @Override
        public void onCategoryClicked(String id) {
            Toast.makeText(getContext(), id, Toast.LENGTH_LONG).show();
            manager.getRoomByCategory(randomRecipeResponseListener, Integer.parseInt(id));
            dialog.show();
        }
    };

    private final RoomResponseListener randomRecipeResponseListener = new RoomResponseListener() {
        @Override
        public void didFetch(List<Room> response, String message) {
            dialog.dismiss();
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            adapter = new BookingAdapter(getContext(), response, bookingClicksListener);
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    };

    private void getViews(View view) {
        textView = view.findViewById(R.id.firstText);
        textView2 = view.findViewById(R.id.textView);
        textView3 = view.findViewById(R.id.textView2);
        textView4 = view.findViewById(R.id.textView3);
        textView5 = view.findViewById(R.id.textView4);
        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recycle_booking_list);
        recyclerViewCategory = view.findViewById(R.id.recycle_booking_category);
        imageProfile = view.findViewById(R.id.imageProfile);
        tabLayout = view.findViewById(R.id.tabLayout);

        dialog = new ProgressDialog(view.getContext());
        dialog.setTitle("Loading...");
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