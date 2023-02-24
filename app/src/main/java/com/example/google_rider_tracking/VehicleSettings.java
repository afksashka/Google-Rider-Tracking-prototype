package com.example.google_rider_tracking;

import java.util.List;

public class VehicleSettings {
    private String vehicleId;

    private Boolean backToBackEnabled;

    private int maximumCapacity;

    private List<String> supportedTripTypes;

    public VehicleSettings(
            String vehicleId,
            Boolean backToBackEnabled,
            int maximumCapacity,
            List<String> supportedTripTypes) {
        this.vehicleId = vehicleId;
        this.backToBackEnabled = backToBackEnabled;
        this.maximumCapacity = maximumCapacity;
        this.supportedTripTypes = supportedTripTypes;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public Boolean getBackToBackEnabled() {
        return backToBackEnabled;
    }

    public List<String> getSupportedTripTypes() {
        return supportedTripTypes;
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setBackToBackEnabled(Boolean backToBackEnabled) {
        this.backToBackEnabled = backToBackEnabled;
    }

    public void setSupportedTripTypes(List<String> supportedTripTypes) {
        this.supportedTripTypes = supportedTripTypes;
    }

    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }
}
