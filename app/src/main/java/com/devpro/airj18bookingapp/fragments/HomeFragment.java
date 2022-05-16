package com.devpro.airj18bookingapp.fragments;

import android.app.AlertDialog;
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
import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.activities.DetailsBookingActivity;
import com.devpro.airj18bookingapp.activities.ProfileActivity;
import com.devpro.airj18bookingapp.adapters.BookingAdapter;
import com.devpro.airj18bookingapp.listeners.*;
import com.devpro.airj18bookingapp.models.*;
import com.devpro.airj18bookingapp.repository.RequestManager;
import com.devpro.airj18bookingapp.utils.Constants;
import com.devpro.airj18bookingapp.utils.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import dmax.dialog.SpotsDialog;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    TextView textView, textView2, textView3, textView4, textView5;
    RoundedImageView imageProfile;
    SearchView searchView;
    AlertDialog dialog;
    TabLayout tabLayout;

    Animation anim_from_button, anim_from_top, anim_from_left, from_right;

    RequestManager manager;
    List<Integer> listWishlistId = new ArrayList<>();

    BottomNavigationView navBar;


    RecyclerView recyclerView;
    BookingAdapter adapter;

    private PreferenceManager preferenceManager;

    String cookie;
    int idCategory = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home1, container, false);

        navBar  = getActivity().findViewById(R.id.bottom_navigation);


        getViews(view);

        preferenceManager = new PreferenceManager(getContext());
        cookie = preferenceManager.getString(Constants.KEY_COOKIE);
        loadUserDetails();

        manager = new RequestManager(getContext());
        manager.getCategories(categoryResponseListener);
        manager.getWishlists(wishlistListResponseListener, cookie);



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
        setEvents();

        return view;
    }

    private void setEvents() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query!=""){
                    manager.getRoomByQuery(listenerQuery,idCategory,query);
                }
                else{
                    tabLayout.setVisibility(View.VISIBLE);
                    manager.getCategories(categoryResponseListener);
                    manager.getWishlists(wishlistListResponseListener, cookie);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                manager.getRoomByQuery(listenerQuery,idCategory,newText);
                return false;
            }
        });
    }

    private final WishlistListResponseListener wishlistListResponseListener = new WishlistListResponseListener() {
        @Override
        public void didFetch(WishlistListIdResponse response, String message) {
            listWishlistId = response.data;
            navBar.getOrCreateBadge(R.id.favorite)
                    .setNumber(listWishlistId.size());
            manager.getCategories(categoryResponseListener);
        }

        @Override
        public void didError(String message) {
            Log.d("XXX", message);
        }
    };

    private final CategoryResponseListener categoryResponseListener = new CategoryResponseListener() {
        @Override
        public void didFetch(CategoryResponse response, String message) {

            List<Category> categories = response.data;
            for (Category category : categories
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
                    idCategory = tab.getPosition() + 1;
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

    private final RoomResponseListener randomRecipeResponseListener = new RoomResponseListener() {
        @Override
        public void didFetch(RoomResponse response, String message) {
            dialog.dismiss();
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            adapter = new BookingAdapter(getContext(), response.data, listWishlistId, bookingClicksListener);
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    };

    private final RoomResponseListener listenerQuery = new RoomResponseListener() {
        @Override
        public void didFetch(RoomResponse response, String message) {
            //Toast.makeText(getContext(),"listenerQuerySuccess:"+ message, Toast.LENGTH_LONG).show();
            dialog.dismiss();
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            adapter = new BookingAdapter(getContext(), response.data, listWishlistId, bookingClicksListener);
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void didError(String message) {
            //Toast.makeText(getContext(),"listenerQueryError:"+ message, Toast.LENGTH_LONG).show();
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
        imageProfile = view.findViewById(R.id.imageProfile);
        tabLayout = view.findViewById(R.id.tabLayout);

        dialog = new SpotsDialog.Builder().setContext(getContext()).setTheme(R.style.Custom).build();

    }

    private void loadUserDetails() {
        System.out.println(preferenceManager.getString(Constants.KEY_IMAGE));

        if (preferenceManager.getString(Constants.KEY_IMAGE) != null) {
            byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageProfile.setImageBitmap(bitmap);
        }

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

        @Override
        public void onBookingFavoriteClicked(String id, boolean isLiked) {
            System.out.println(isLiked+" "+id);
            if (isLiked) {
                manager.getRemoveWishlist(wishlistEventResponseListener, id, cookie);
                listWishlistId.remove(Integer.valueOf(id));
            } else {
                manager.getAddWishlist(wishlistEventResponseListener, id, cookie);
                listWishlistId.add(Integer.parseInt(id));
            }

            navBar.getOrCreateBadge(R.id.favorite)
                    .setNumber(listWishlistId.size());

            adapter.notifyDataSetChanged();
        }
    };

    private WishlistEventResponseListener wishlistEventResponseListener = new WishlistEventResponseListener() {
        @Override
        public void didFetch(WishlistResponse response, String message) {
            Toast.makeText(getActivity(), "Action " + response.data, Toast.LENGTH_LONG).show();
        }

        @Override
        public void didError(String message) {
            Toast.makeText(getActivity(), "Error " + message, Toast.LENGTH_LONG).show();
        }
    };
}