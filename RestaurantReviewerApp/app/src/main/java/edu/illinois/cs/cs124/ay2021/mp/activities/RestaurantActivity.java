package edu.illinois.cs.cs124.ay2021.mp.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import edu.illinois.cs.cs124.ay2021.mp.R;
import edu.illinois.cs.cs124.ay2021.mp.application.EatableApplication;
import edu.illinois.cs.cs124.ay2021.mp.databinding.ActivityRestaurantBinding;
import edu.illinois.cs.cs124.ay2021.mp.models.RelatedRestaurants;
import edu.illinois.cs.cs124.ay2021.mp.models.Restaurant;

// MP2: Part 3, new activity to display restaurant details
public class RestaurantActivity extends AppCompatActivity {
  // Binding to the layout defined in activity_restaurant.xml
  private ActivityRestaurantBinding binding;
  @Override
  protected void onCreate(@Nullable final Bundle unused) {
    super.onCreate(unused);
    //step 2: retrieve ID from the intent
    //step 3: convert ID to restaurant
    Intent startedIntent = getIntent(); // returns the intent that started this activity
    String restaurantID = startedIntent.getStringExtra("id"); // use .getExtras to get info about restaurant
    EatableApplication app = (EatableApplication) getApplication();
    Restaurant restaurant = app.getClient().getRestaurantForID(restaurantID);
    RelatedRestaurants r = app.getClient().getRelatedRestaurants();
    int size = r.getConnectedTo(restaurantID).size();
    String mostRelated = "";
    if (r.getRelatedInOrder(restaurantID).size() != 0) {
      mostRelated = r.getRelatedInOrder(restaurantID).get(0).getName();
    }
    binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant);
    binding.name.setText(restaurant.getName());
    binding.cuisine.setText(restaurant.getCuisine());
    binding.topRelated.setText(mostRelated);
    binding.connectedCount.setText(String.valueOf(size));
  }
}
