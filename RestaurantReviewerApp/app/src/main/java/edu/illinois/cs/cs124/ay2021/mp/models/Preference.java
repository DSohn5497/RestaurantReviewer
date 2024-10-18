package edu.illinois.cs.cs124.ay2021.mp.models;

import java.util.List;

//MP2 part 2: create a Preference model
public class Preference {
  private String id;
  private List<String> restaurantIDs;
  public String getId() {
    return id;
  }
  public List<String> getRestaurantIDs() {
    return restaurantIDs;
  }
  public Preference() {}
}
