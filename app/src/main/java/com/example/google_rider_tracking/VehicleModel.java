
package com.example.google_rider_tracking;

import com.google.android.libraries.navigation.Waypoint;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/** Non-extensive POJO representation of a Vehicle object. */
public class VehicleModel {

  @SerializedName("name")
  private String name;

  @SerializedName("vehicleState")
  private String vehicleState;

  @SerializedName("currentTripsIds")
  private List<String> currentTripsIds;

  @SerializedName("backToBackEnabled")
  private boolean backToBackEnabled;

  @SerializedName("supportedTripTypes")
  private List<String> supportedTripTypes;

  @SerializedName("maximumCapacity")
  private int maximumCapacity;

  @SerializedName("waypoints")
  private List<Waypoint> waypoints;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getVehicleState() {
    return vehicleState;
  }

  public void setVehicleState(String vehicleState) {
    this.vehicleState = vehicleState;
  }

  public List<String> getCurrentTripsIds() {
    return currentTripsIds;
  }

  public void setCurrentTripsIds(List<String> currentTripsIds) {
    this.currentTripsIds = currentTripsIds;
  }

  public boolean getBackToBackEnabled() {
    return backToBackEnabled;
  }

  public void setBackToBackEnabled(boolean backToBackEnabled) {
    backToBackEnabled = backToBackEnabled;
  }

  public List<String> getSupportedTripTypes() {
    return supportedTripTypes;
  }

  public void setSupportedTripTypes(List<String> supportedTripTypes) {
    this.supportedTripTypes = supportedTripTypes;
  }

  public int getMaximumCapacity() {
    return maximumCapacity;
  }

  public void setMaximumCapacity(int maximumCapacity) {
    this.maximumCapacity = maximumCapacity;
  }

  public List<Waypoint> getWaypoints() {
    return waypoints;
  }

  public void setWaypoints(List<Waypoint> waypoints) {
    this.waypoints = waypoints;
  }
}
