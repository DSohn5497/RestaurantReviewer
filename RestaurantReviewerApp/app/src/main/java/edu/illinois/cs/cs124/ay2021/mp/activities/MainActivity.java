package edu.illinois.cs.cs124.ay2021.mp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.cs.cs124.ay2021.mp.R;
import edu.illinois.cs.cs124.ay2021.mp.adapters.RestaurantListAdapter;
import edu.illinois.cs.cs124.ay2021.mp.application.EatableApplication;
import edu.illinois.cs.cs124.ay2021.mp.databinding.ActivityMainBinding;
import edu.illinois.cs.cs124.ay2021.mp.models.Restaurant;

/*
 * App main activity.
 * Started when the app is launched, based on the configuration in the Android Manifest (AndroidManifest.xml).
 * Should display a sorted list of restaurants based on data retrieved from the server.
 *
 * You will need to understand some of the code here and make changes starting with MP1.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public final class MainActivity extends AppCompatActivity
    implements SearchView.OnQueryTextListener, RestaurantListAdapter.Listener {
  // You may find this useful when adding logging
  private static final String TAG = MainActivity.class.getSimpleName();

  // Binding to the layout defined in activity_main.xml
  private ActivityMainBinding binding;

  // List adapter for displaying the list of restaurants
  private RestaurantListAdapter listAdapter;

  // Reference to the persistent Application instance
  private EatableApplication application;

  private List<Restaurant> restaurantList;
  private boolean isListCreated = false;

  /*
   * onCreate is the first method called when this activity is created.
   * Code here normally does a variety of setup tasks.
   */
  @SuppressWarnings("checkstyle:HiddenField")
  @Override
  protected void onCreate(final Bundle unused) {
    super.onCreate(unused);

    // Initialize our data binding
    // This allows us to more easily access elements of our layout
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    // Create a new list adapter for restaurants and attach it to our app layout
    listAdapter = new RestaurantListAdapter(this, this);
    binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    binding.recyclerView.setAdapter(listAdapter);

    Log.d(TAG, "Hello, world!");
    // Grab a reference to our application instance and use it to fetch the list of restaurants from
    // the server
    application = (EatableApplication) getApplication();
    application
        .getClient()
        /*
         * What is passed to getRestaurants is a callback, which we'll discuss in more detail in the MP lessons.
         * Callbacks allow us to wait for something to complete and run code when it does.
         * In this case, once we retrieve a list of restaurants, we use it to update the contents of our list.
         */
        .getRestaurants(
            (restaurants -> {
              listAdapter.edit().replaceAll(restaurants).commit();
              application.getClient().getPreferences(preferences -> { });
            }));


    // Bind to the search component so that we can receive events when the contents of the search
    // box change
    // the line below registers the callback methods in the Android Platform
    binding.search.setOnQueryTextListener(this);
    // Bind the toolbar that contains our search component
    setSupportActionBar(binding.toolbar);
    // Set the activity title
    setTitle("Find Restaurants");
  }
  public void setRestaurantList() {
    restaurantList = new ArrayList<Restaurant>();
    for (int i = 0; i < listAdapter.getItemCount(); i++) {
      restaurantList.add(listAdapter.getItem(i));
    }
  }
  /*
   * Called when the user changes the text in the search bar.
   * Eventually (MP1) we'll want to update the list of restaurants shown based on their input.
   */
  @Override
  public boolean onQueryTextChange(final String query) {
    if (isListCreated == false) {
      restaurantList = new ArrayList<Restaurant>();
      for (int i = 0; i < listAdapter.getItemCount(); i++) {
        restaurantList.add(listAdapter.getItem(i));
      }
      isListCreated = true;
    }
    List<Restaurant> filteredList = Restaurant.search(restaurantList, query);
    Log.d(TAG, "filtered size: " + filteredList.size());
    Log.d(TAG, "restaurantList size: " + restaurantList.size());
    listAdapter.edit().removeAll().commit();
    for (int j = 0; j < filteredList.size(); j++) {
      listAdapter.edit().add(filteredList.get(j)).commit();
    }
    return true;
  }
  /*
   * Called when the user clicks on one of the restaurants in the list.
   * Eventually (MP2) we'll launch a new activity here so they can see the restaurant details.
   */
  @Override
  public void onClicked(final Restaurant restaurant) {
    Intent startRestaurant = new Intent(this, RestaurantActivity.class);
    //step 1: add ID to intent
    startRestaurant.putExtra("id", restaurant.getId());
    startActivity(startRestaurant);
  }

  /*
   * Called when the user submits their search query.
   * We update the list as the text changes, so don't need to do anything special here.
   */
  @Override
  public boolean onQueryTextSubmit(final String unused) {
    return true;
  }
}
