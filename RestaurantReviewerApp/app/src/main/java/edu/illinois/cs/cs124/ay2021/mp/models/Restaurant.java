package edu.illinois.cs.cs124.ay2021.mp.models;




import androidx.annotation.NonNull;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;



/*
 * Model storing information about a restaurant retrieved from the restaurant server.
 *
 * You will need to understand some of the code in this file and make changes starting with MP1.
 *
 * If your project builds successfully, you can safely ignore the warning about "Related problems" here.
 * It seems to be a bug in Android studio.
 */
@SuppressWarnings("unused")
public final class Restaurant implements SortedListAdapter.ViewModel {
  // Name of the restaurant
  private String name;
  private String cuisine;
  private String id;

  public String getCuisine() {
    return cuisine;
  }
  public String getName() {
    return name;
  }
  public String getNameandCuisine() {
    return name + " " + cuisine;
  }
  public String getId() {
    return id;
  }

  @SuppressWarnings("checkstyle:RegexpSingleline")
  public static List<Restaurant> search(final List<Restaurant> restaurants, final String search) {
    if (restaurants == null || search == null) {
      throw new IllegalArgumentException();
    }
    List<Restaurant> output = new ArrayList<>();
    Set<String> cuisines = new HashSet<>();
    for (int i = 0; i < restaurants.size(); i++) {
      cuisines.add(restaurants.get(i).cuisine);
    }
    String s = search;
    s = s.trim();
    s = s.toLowerCase();
    if (s.length() == 0 || s.equals("")) {
      for (int i = 0; i < restaurants.size(); i++) {
        output.add(restaurants.get(i));
      }
    } else if (cuisines.contains(s)) {
      for (int i = 0; i < restaurants.size(); i++) {
        String compare = restaurants.get(i).cuisine.toLowerCase();
        if (compare.equals(s)) {
          output.add(restaurants.get(i));
        }
      }
    } else {
      for (int i = 0; i < restaurants.size(); i++) {
        String compareCuisine = restaurants.get(i).cuisine.toLowerCase();
        String compareName = restaurants.get(i).name.toLowerCase();
        if (compareCuisine.contains(s) || compareName.contains(s)) {
          output.add(restaurants.get(i));
        }
      }
    }
    return output;
  }

    // Getter for the name


  // You will need to add more fields here...

  /*
   * The Jackson JSON serialization library we are using requires an empty constructor.
   * So don't remove this!
   */
  public Restaurant() {}

  /*
   * Function to compare Restaurant instances by name.
   * Currently this does not work, but you will need to implement it correctly for MP1.
   * Comparator is like Comparable, except it defines one possible ordering, not a canonical ordering for a class,
   * and so is implemented as a separate method rather than directly by the class as is done with Comparable.
   */
  public static final Comparator<Restaurant> SORT_BY_NAME = ((restaurant1, restaurant2) -> {
    return restaurant1.name.compareTo(restaurant2.name);
  });
  // You should not need to modify this code, which is used by the list adapter component
  @Override
  public <T> boolean isSameModelAs(@NonNull final T model) {
    return equals(model);
  }

  // You should not need to modify this code, which is used by the list adapter component
  @Override
  public <T> boolean isContentTheSameAs(@NonNull final T model) {
    return equals(model);
  }
  @Override
  public int hashCode() {
    return id.hashCode();
  }
  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof Restaurant)) {
      return false;
    }
    Restaurant other = (Restaurant) o;
    return id.equals(other.id);
  }
}
