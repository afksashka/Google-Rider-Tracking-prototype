package com.example.google_rider_tracking;

import com.google.common.util.concurrent.ListenableFuture;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestProvider {
    @GET("vehicle/{id}")
    ListenableFuture<VehicleModel> getVehicle(@Path("id") String vehicle);

    @POST("vehicle/new")
    ListenableFuture<VehicleModel> createVehicle(@Body VehicleSettings body);
}
