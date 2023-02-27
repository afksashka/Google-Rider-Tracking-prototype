package com.example.google_rider_tracking;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.google_rider_tracking.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.libraries.mapsplatform.transportation.driver.api.base.data.DriverContext;
import com.google.android.libraries.mapsplatform.transportation.driver.api.ridesharing.RidesharingDriverApi;
import com.google.android.libraries.mapsplatform.transportation.driver.api.ridesharing.vehiclereporter.RidesharingVehicleReporter;
import com.google.android.libraries.navigation.NavigationApi;
import com.google.android.libraries.navigation.Navigator;
import com.google.android.libraries.navigation.SupportNavigationFragment;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private SupportMapFragment mapFragment;
    private Navigator navigator;
    private SupportNavigationFragment navFragment;
    private RestProvider restProvider;

    public static final String EXCLUSIVE_TRIP_TYPE = "EXCLUSIVE";
    public static final List<String> DEFAULT_SUPPORTED_TRIP_TYPES =
            ImmutableList.of(EXCLUSIVE_TRIP_TYPE);
    private final ExecutorService executor = Executors.newCachedThreadPool();
    boolean isCameraTilted = true;
    private static boolean isNotFoundHttpException(HttpException httpException) {
        return httpException.code() == 404;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restProvider = createRestProvider("https://xpress-366609.uc.r.appspot.com");
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        initializeSDKs();

        updateCameraPerspective(isCameraTilted);
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
                        initDriverContext();
                    }
                    @Override
                    public void onError(int i) {
                        Log.d("rider-tag", "Error: " + i);
                    }
                }
        );
    }
    private void initDriverContext(){
        Application application = this.getApplication();
        JsonAuthTokenFactory authTokenFactory = new JsonAuthTokenFactory();
        DriverContext driverContext = DriverContext.builder(application)
                .setProviderId("xpress-366609")
                .setVehicleId("v-001-sb")
                .setAuthTokenFactory(authTokenFactory)
                .setNavigator(navigator)
                .setRoadSnappedLocationProvider(
                        NavigationApi.getRoadSnappedLocationProvider(application))
                .build();
        RidesharingDriverApi ridesharingDriverApi = RidesharingDriverApi.createInstance(driverContext);
        RidesharingVehicleReporter vehicleReporter = ridesharingDriverApi.getRidesharingVehicleReporter();
        vehicleReporter.enableLocationTracking();
        vehicleReporter.setVehicleState(RidesharingVehicleReporter.VehicleState.ONLINE);
        Log.d("rider-tag", "Vehicle Reporter enabled? " + vehicleReporter.isLocationTrackingEnabled());
        registerVehicle("v-001-sb");
    }

    public ListenableFuture<VehicleModel> registerVehicle(String vehicleId) {
        return Futures.catchingAsync(
                restProvider.getVehicle(vehicleId),
                HttpException.class,
                (exception) -> {
                    if (isNotFoundHttpException(exception)) {
                        return restProvider.createVehicle(
                                new VehicleSettings(
                                        vehicleId,
                                        /* backToBackEnabled= */ false,
                                        4,
                                        DEFAULT_SUPPORTED_TRIP_TYPES));

                    } else {
                        return Futures.immediateFailedFuture(exception);
                    }
                },
                executor);
    }

    public static RestProvider createRestProvider(String baseUrl) {
        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addCallAdapterFactory(GuavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

        return retrofit.create(RestProvider.class);
    }
    // Permissions are managed by the 'SplashScreenActivity'
    @SuppressLint("MissingPermission")
    private void updateCameraPerspective(Boolean isTilted) {
        mapFragment.getMapAsync(
                googleMap ->
                        googleMap.followMyLocation(
                                isTilted ? GoogleMap.CameraPerspective.TILTED : GoogleMap.CameraPerspective.TOP_DOWN_NORTH_UP));
    }
}