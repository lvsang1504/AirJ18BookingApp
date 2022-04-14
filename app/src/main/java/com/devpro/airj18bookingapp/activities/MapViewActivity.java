package com.devpro.airj18bookingapp.activities;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.models.RoomDetail;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.net.ConnectivityReceiver;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.squareup.picasso.Picasso;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback, Callback<DirectionsResponse>, PermissionsListener {

    MapView mapView;
    public MapboxMap mapboxMap;
    AlertDialog dialog;
    private PermissionsManager permissionsManager;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private CarmenFeature home;
    private CarmenFeature work;
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private String symbolIconId = "symbolIconId";
    private static final int REQUEST_CODE = 5678;
    String address;
    Point origin = Point.fromLngLat(90.399452, 23.777176);
    Point destination = Point.fromLngLat(90.399452, 23.777176);
    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";
    private MapboxDirections client;
    int c = 0;
    double distance;
    String st;
    String startLocation = "";
    String endLocation = "";
    RoomDetail roomDetail;

    TextView txtName;
    RoundedImageView imageRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

                Mapbox.getInstance(MapViewActivity.this, getString(R.string.mapbox_access_token));



//        Mapbox.getInstance(this, getResources().getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_map_view);

        txtName = findViewById(R.id.txtName);
        imageRoom = findViewById(R.id.imageRoom);
        dialog = new SpotsDialog.Builder().setContext(MapViewActivity.this).setTheme(R.style.Custom).build();
        dialog.show();
        String json = getIntent().getStringExtra("room_detail");

        Gson gson = new Gson();
        roomDetail = gson.fromJson(json, RoomDetail.class);

        Toast.makeText(MapViewActivity.this, roomDetail.description, Toast.LENGTH_LONG).show();

        txtName.setText(roomDetail.name);
        Picasso.get().load(com.devpro.airj18bookingapp.utils.Constants.BASE_URL + roomDetail.thumbnail).into(imageRoom);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


    }

    @Override
    public void onMapReady(final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;



        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override

            public void onStyleLoaded(@NonNull Style style) {

                enableLocationComponent(style);
//                initSearchFab();

                addUserLocations();
                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_location_on_24, null);
                Bitmap mBitmap = BitmapUtils.getBitmapFromDrawable(drawable);
                // Add the symbol layer icon to map for future use
                style.addImage(symbolIconId, mBitmap);

                // Create an empty GeoJSON source using the empty feature collection
                setUpSource(style);

                // Set up a new symbol layer for displaying the searched location's feature coordinates
                setupLayer(style);


                initSource(style);

                initLayers(style);

                Location lastKnownLocation = mapboxMap.getLocationComponent().getLastKnownLocation();
                origin = Point.fromLngLat(lastKnownLocation.getLongitude(), lastKnownLocation.getLatitude());
                destination = Point.fromLngLat(roomDetail.longitude, roomDetail.latitude);

                getRoute(mapboxMap, origin, destination);


                //  CameraPosition position = new CameraPosition.Builder().target(new LatLng(destination.latitude(), destination.longitude()))
                //      .zoom(13).tilt(13).build();
                //  mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 100);
//                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
//                    LatLng source;
//
//                    @Override
//                    public boolean onMapClick(@NonNull LatLng point) {
//
//
//                        if (c == 0) {
//                            //origin = Point.fromLngLat(point.getLongitude(), point.getLatitude());
//                            source = point;
//                            MarkerOptions markerOptions = new MarkerOptions();
//                            markerOptions.position(point);
//                            markerOptions.title("Source");
//                            mapboxMap.addMarker(markerOptions);
//                            reverseGeocodeFunc(point, c);
//
//
//                        }
//                        if (c == 1) {
//                            //destination = Point.fromLngLat(point.getLongitude(), point.getLatitude());
//                            getRoute(mapboxMap, origin, destination);
//                            MarkerOptions markerOptions2 = new MarkerOptions();
//                            markerOptions2.position(point);
//                            markerOptions2.title("destination");
//                            mapboxMap.addMarker(markerOptions2);
//                            reverseGeocodeFunc(point, c);
//                            getRoute(mapboxMap, origin, destination);
//                            // double d = point.distanceTo(source);
//
//
//                        }
//
//
//
//                      /*  startActivityForResult(
//                                new PlacePicker.IntentBuilder()
//                                        .accessToken("pk.eyJ1IjoiemFoaWQxNiIsImEiOiJja2UxZ3lpaGE0NHFuMnJtcXc5djcxeGVtIn0.V5lnAKqektnfC1pARBQYUQ")
//                                        .placeOptions(PlacePickerOptions.builder()
//                                                .statingCameraPosition(new CameraPosition.Builder()
//                                                        .target(point).zoom(16).build())
//                                                .build())
//                                        .build(this), REQUEST_CODE);
//                        */
//                        if (c > 1) {
//                            c = 0;
//                            recreate();
//                            // mapboxMap.clear();
//                            //   Toast.makeText(MainActivity.this,d+" metres", Toast.LENGTH_LONG).show();
//
//                        }
//
//                        c++;
//                        return true;
//
//
//                    }
//
//                });

            }
        });

        dialog.dismiss();

    }

//    private void reverseGeocodeFunc(LatLng point, int c) {
//        MapboxGeocoding reverseGeocode = MapboxGeocoding.builder()
//                .accessToken(getResources().getString(R.string.mapbox_access_token))
//                .query(Point.fromLngLat(point.getLongitude(), point.getLatitude()))
//                .geocodingTypes(GeocodingCriteria.TYPE_POI)
//                .build();
//        reverseGeocode.enqueueCall(new Callback<GeocodingResponse>() {
//            @Override
//            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
//
//                List<CarmenFeature> results = response.body().features();
//
//                if (results.size() > 0) {
//                    // CarmenFeature feature =results.get(0);
//                    CarmenFeature feature;
//                    // Log the first results Point.
//                    Point firstResultPoint = results.get(0).center();
//                    //   for (int i = 0; i < results.size(); i++) {
//                    //  feature = results.get(i);
//                    feature = results.get(0);
//                    if (c == 0) {
//                        startLocation += feature.placeName();
//                        startLocation = startLocation.replace(", Dhaka, Bangladesh", ".");
//                        TextView tv = findViewById(R.id.s);
//                        tv.setText(startLocation);
//
//                    }
//                    if (c == 1) {
//                        endLocation += feature.placeName();
//                        endLocation = endLocation.replace(", Dhaka, Bangladesh", ".");
//                        TextView tv2 = findViewById(R.id.d);
//                        tv2.setText(endLocation);
//                    }
//
//                    // endLocation = endLocation.replace(",Dhaka,Bangladesh", " ");
//                    // Toast.makeText(MapsActivity.this, endLocation, Toast.LENGTH_LONG).show();
//
//
//                    // startLocation=feature.placeName()+"";
//
//                    //   Toast.makeText(MainActivity.this, "" + results.get(i), Toast.LENGTH_LONG).show();
//                    Toast.makeText(MapViewActivity.this, "" + feature.placeName(), Toast.LENGTH_LONG).show();
//
//                    //  }
//                    Log.d("MyActivity", "onResponse: " + firstResultPoint.toString());
//
//                } else {
//
//                    // No result for your request were found.
//                    Toast.makeText(MapViewActivity.this, "Not found", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
//                throwable.printStackTrace();
//            }
//        });
//
//
//    }

    private void initLayers(@NonNull Style loadedMapStyle) {
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

// Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(Color.parseColor("#009688"))
        );
        loadedMapStyle.addLayer(routeLayer);

// Add the red marker icon image to the map
        loadedMapStyle.addImage(RED_PIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.marker)));


// Add the red marker icon SymbolLayer to the map
        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                PropertyFactory.iconImage(RED_PIN_ICON_ID),
                PropertyFactory.iconIgnorePlacement(true),
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.iconOffset(new Float[]{0f, -9f})));
    }

    private void initSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID));

        GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{
                Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))}));
        loadedMapStyle.addSource(iconGeoJsonSource);
    }

    private void getRoute(final MapboxMap mapboxMap, Point origin, final Point destination) {
        client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(getResources().getString(R.string.mapbox_access_token))
                .build();

        client.enqueueCall(this);
    }


    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
// You can get the generic HTTP info about the response
        if (response.body() == null) {
            Toast.makeText(MapViewActivity.this, "NO routes found make sure to set right user and access token", Toast.LENGTH_LONG).show();
            return;
        } else if (response.body().routes().size() < 1) {
            Toast.makeText(MapViewActivity.this, "NO routes found", Toast.LENGTH_LONG).show();
        }


// Get the directions route
        final DirectionsRoute currentRoute = response.body().routes().get(0);
        // Toast.makeText(MainActivity.this,currentRoute.distance()+" metres ",Toast.LENGTH_SHORT).show();
        distance = currentRoute.distance() / 1000;
        st = String.format("%.2f K.M", distance);
        TextView dv = findViewById(R.id.distanceView);
        dv.setText(st);
        //String f=startLocation+endLocation+st;
        //TextView tv=findViewById(R.id.s);
        // TextView tv2 = findViewById(R.id.d);
        // startLocation=startLocation.replace("Bangladesh",".");
        //  endLocation=endLocation.replace("Bangladesh",".");

        // tv.setText(startLocation);
        // tv2.setText(endLocation);


        // tv.setText(distance+" K.M");
        //Toast.makeText(MainActivity.this,st,Toast.LENGTH_LONG).show();


        if (mapboxMap != null) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {

// Retrieve and update the source designated for showing the directions route
                    GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

// Create a LineString with the directions route's geometry and
// reset the GeoJSON source for the route LineLayer source
                    if (source != null) {
                        source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), Constants.PRECISION_6));
                    }
                }

            });

        }

    }

    @Override
    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {

    }


    public void confirmed(View view) {
        navigationRoute();

    }

    private void navigationRoute() {
        NavigationRoute.builder(this)
                .accessToken(getResources().getString(R.string.mapbox_access_token))
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null) {
                            Toast.makeText(MapViewActivity.this, "No route found make sure get right user and  access token!", Toast.LENGTH_LONG).show();
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Toast.makeText(MapViewActivity.this, "No route found!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        DirectionsRoute route = response.body().routes().get(0);
                        boolean simulateRoute = true;


                        NavigationLauncherOptions options =
                                NavigationLauncherOptions.builder()
                                        .directionsRoute(route)
                                        .shouldSimulateRoute(true)
                                        .build();

                        NavigationLauncher.startNavigation(MapViewActivity.this, options);

                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                    }
                });
    }

//    private void initSearchFab() {
//        findViewById(R.id.fab_location_search).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new PlaceAutocomplete.IntentBuilder()
//                        .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getResources().getString(R.string.mapbox_access_token))
//                        .placeOptions(PlaceOptions.builder()
//                                .backgroundColor(Color.parseColor("#EEEEEE"))
//                                .limit(10)
//                                .addInjectedFeature(home)
//                                .addInjectedFeature(work)
//                                .build(PlaceOptions.MODE_CARDS))
//                        .build(MapViewActivity.this);
//                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
//            }
//        });
//    }

    private void addUserLocations() {
        home = CarmenFeature.builder().text("Mapbox SF Office")
                .geometry(Point.fromLngLat(-122.3964485, 37.7912561))
                .placeName("50 Beale St, San Francisco, CA")
                .id("mapbox-sf")
                .properties(new JsonObject())
                .build();

        work = CarmenFeature.builder().text("Mapbox DC Office")
                .placeName("740 15th Street NW, Washington DC")
                .geometry(Point.fromLngLat(-77.0338348, 38.899750))
                .id("mapbox-dc")
                .properties(new JsonObject())
                .build();
    }

    private void setUpSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(geojsonSourceLayerId));
    }

    private void setupLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                iconImage(symbolIconId),
                iconOffset(new Float[]{0f, -8f})
        ));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            // Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            // Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
            // Then retrieve and update the source designated for showing a selected location's symbol layer icon

            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[]{Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }

                    // Move map camera to the selected location
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(14)
                                    .build()), 4000);
                }
            }
        }
        /*if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
// Retrieve the information from the selected location's CarmenFeature
            CarmenFeature carmenFeature = PlacePicker.getPlace(data);

// Set the TextView text to the entire CarmenFeature. The CarmenFeature
// also be parsed through to grab and display certain information such as
// its placeName, text, or coordinates.
            if (carmenFeature != null) {
                Toast.makeText(MapsActivity.this, String.format(address
                        , carmenFeature.toJson()), Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(MapViewActivity.this)) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(MapViewActivity.this, loadedMapStyle).build());

            // Enable to make component visible
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        //  Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            //Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public void backClick(View view) {
        onBackPressed();
    }
}