package edu.illinois.cs.cs124.ay2021.mp.models;





import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



//MP2 Part 4: Identify relationships between restaurants
public class RelatedRestaurants {
  private Map<String, Map<String, Integer>> restaurantRelationships = new HashMap<>();
  private final Map<String, Restaurant> restaurantMap = new HashMap<>();
  // constructor
  public RelatedRestaurants(final List<Restaurant> restaurants, final List<Preference> preferences) {
    // populate restaurantMap
    for (int h = 0; h < restaurants.size(); h++) {
      restaurantMap.put(restaurants.get(h).getId(), restaurants.get(h));
    }
    // populate restaurantRelationships
    Set<String> restaurantIDs = new HashSet<>();
    for (int m = 0; m < restaurants.size(); m++) {
      restaurantIDs.add(restaurants.get(m).getId());
    }
    for (int j = 0; j < preferences.size(); j++) {
      List<String> relatedRestaurantIDs = preferences.get(j).getRestaurantIDs();
      for (int k = 0; k < relatedRestaurantIDs.size(); k++) {
        String firstID = relatedRestaurantIDs.get(k);
        if (restaurantIDs.contains(firstID)) {
          for (int i = 0; i < relatedRestaurantIDs.size(); i++) {
            String secondID = relatedRestaurantIDs.get(i);
            if (restaurantIDs.contains(secondID)) {
              if (firstID.equals(secondID) != true && restaurantRelationships.containsKey(firstID)) {
                Map<String, Integer> relatedMap = restaurantRelationships.get(firstID);
                if (relatedMap.containsKey(secondID)) {
                  int val = relatedMap.get(secondID);
                  relatedMap.put(secondID, val + 1);
                  restaurantRelationships.put(firstID, relatedMap);
                } else {
                  relatedMap.put(secondID, 1);
                  restaurantRelationships.put(firstID, relatedMap);
                }
              } else if (firstID.equals(secondID) != true) {
                Map<String, Integer> relatedMap = new HashMap<>();
                relatedMap.put(secondID, 1);
                restaurantRelationships.put(firstID, relatedMap);
              }
            }
          }
        }
      }
    }
  }
  public Map<String, Integer> getRelated(final String restaurantID) {
    Map<String, Integer> returnMap = restaurantRelationships.get(restaurantID);
    if (returnMap != null) {
      return returnMap;
    } else {
      return new HashMap<>();
    }
  }
  public List<Restaurant> getRelatedInOrder(final String restaurantID) {
    // Check the restaurantID
    if (restaurantMap.get(restaurantID) == null) {
      throw new IllegalArgumentException();
    }
    // Retrieve the related restaurants using the getRelated method
    Map<String, Integer> relatedMap = getRelated(restaurantID);
    List<String> restaurantIDs = new ArrayList<>();
    for (String key : relatedMap.keySet()) {
      restaurantIDs.add(key);
    }
    // Convert  list of restaurant IDs to a list of restaurants
    List<Restaurant> restaurants = new ArrayList<>();
    for (int i = 0; i < restaurantIDs.size(); i++) {
      restaurants.add(restaurantMap.get(restaurantIDs.get(i)));
    }
    // Sort it properly (Multiproperty sort)

    //sort by name in ascending order
    Collections.sort(restaurants, (r1, r2) -> {
      return r1.getName().compareTo(r2.getName());
    });
    //sort by most related
    Collections.sort(restaurants, (r1, r2) -> {
      String s1 = r1.getId();
      String s2 = r2.getId();
      int i1 = relatedMap.get(s1);
      int i2 = relatedMap.get(s2);
      return i2 - i1;
    });
    return restaurants;
  }
  public Set<Restaurant> getConnectedTo(final String restaurantID) {
    if (restaurantMap.get(restaurantID) == null) {
      throw new IllegalArgumentException();
    }
    Set<String> seen = new HashSet<>();
    count(restaurantID, seen, 2);
    seen.remove(restaurantID);
    Set<Restaurant> output = new HashSet<>();
    for (String s : seen) {
      Restaurant r = restaurantMap.get(s);
      output.add(r);
    }
    return output;
  }
  private void count(final String s, final Set<String> seen, final int distance) {
    if (distance == 0) {
      return;
    }
    Map<String, Integer> neighbors = getRelated(s);
    for (String neighbor : neighbors.keySet()) {
      if (neighbors.get(neighbor) > 1) {
        seen.add(neighbor);
        count(neighbor, seen, distance - 1);
      }
    }
  }
}
