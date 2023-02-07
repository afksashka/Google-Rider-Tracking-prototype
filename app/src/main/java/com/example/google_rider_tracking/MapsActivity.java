package com.example.google_rider_tracking;

import androidx.fragment.app.FragmentActivity;

import android.app.Application;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.google_rider_tracking.databinding.ActivityMapsBinding;
import com.google.android.libraries.mapsplatform.transportation.driver.api.base.data.DriverContext;
import com.google.android.libraries.mapsplatform.transportation.driver.api.ridesharing.RidesharingDriverApi;
import com.google.android.libraries.mapsplatform.transportation.driver.api.ridesharing.vehiclereporter.RidesharingVehicleReporter;
import com.google.android.libraries.navigation.NavigationApi;
import com.google.android.libraries.navigation.Navigator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private SupportMapFragment mapFragment;
    private Navigator navigator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        initializeSDKs();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void initializeSDKs() {
        NavigationApi.getNavigator(
                this, // Activity
                new NavigationApi.NavigatorListener() {
                    @Override
                    public void onNavigatorReady(Navigator navigatorSdk) {
                        // Keep a reference to the Navigator (used to configure and start nav)
                        navigator = navigatorSdk;
                    }
                    @Override
                    public void onError(int i) {

                    }
                }
        );
        Application application = this.getApplication();
        JsonAuthTokenFactory authTokenFactory = new JsonAuthTokenFactory();
        DriverContext driverContext = DriverContext.builder(application)
                .setProviderId("xpress-366609")
                .setVehicleId("v-001")
                .setAuthTokenFactory(authTokenFactory)
                .setNavigator(navigator)
                .setRoadSnappedLocationProvider(
                        NavigationApi.getRoadSnappedLocationProvider(application))
                .build();
        RidesharingDriverApi ridesharingDriverApi = RidesharingDriverApi.createInstance(driverContext);
        RidesharingVehicleReporter vehicleReporter = ridesharingDriverApi.getRidesharingVehicleReporter();



    }
}