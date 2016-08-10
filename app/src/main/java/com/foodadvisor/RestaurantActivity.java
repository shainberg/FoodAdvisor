package com.foodadvisor;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.foodadvisor.models.Restaurant;

public class RestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Restaurant restaurant = (Restaurant) getIntent().getSerializableExtra("Restaurant");

        TextView restaurantNameText = (TextView) findViewById(R.id.restaurant_name);
        restaurantNameText.setText(restaurant.getName());
        TextView restaurantTimeText = (TextView) findViewById(R.id.restaurant_time);
        restaurantTimeText.setText(restaurant.getTime());
        TextView restaurantAddressText = (TextView) findViewById(R.id.restaurant_address);
        restaurantAddressText.setText(restaurant.getAddress());
        TextView restaurantTypeText = (TextView) findViewById(R.id.restaurant_type);
        restaurantTypeText.setText(restaurant.getType());
        TextView restaurantPhoneText = (TextView) findViewById(R.id.restaurant_phone);
        restaurantPhoneText.setText(restaurant.getPhone());
        CheckBox restaurantParkingBox = (CheckBox) findViewById(R.id.restaurant_parking);
        restaurantParkingBox.setClickable(false);
        restaurantParkingBox.setChecked(restaurant.getParking());
        CheckBox restaurantKosherBox = (CheckBox) findViewById(R.id.restaurant_kosher);
        restaurantKosherBox.setClickable(false);
        restaurantKosherBox.setChecked(restaurant.getKosher());
        CheckBox restaurantAccessibleBox = (CheckBox) findViewById(R.id.restaurant_accessible);
        restaurantAccessibleBox.setClickable(false);
        restaurantAccessibleBox.setChecked(restaurant.getAccessible());



        FloatingActionButton callButton = (FloatingActionButton) findViewById(R.id.call_button);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + ((Restaurant)getIntent().getSerializableExtra("Restaurant")).getPhone()));
                startActivity(intent);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

            }
        });

        FloatingActionButton navigateButton = (FloatingActionButton) findViewById(R.id.navigate_button);
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    Restaurant restaurant = (Restaurant)getIntent().getSerializableExtra("Restaurant");
                    String url = "waze://?q= " + restaurant.getLatitude() + "," + restaurant.getLongitude();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
                catch (ActivityNotFoundException ex)
                {
                    Snackbar.make(view, "Don't have Waze? :(", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                    /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://details?id=com.waze"));
                    startActivity(intent);*/
                }
            }
        });
    }

}