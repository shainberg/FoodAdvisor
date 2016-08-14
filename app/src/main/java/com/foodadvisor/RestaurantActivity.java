package com.foodadvisor;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.foodadvisor.models.Restaurant;

public class RestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Restaurant restaurant = (Restaurant) getIntent().getSerializableExtra("Restaurant");
        getSupportActionBar().setTitle(restaurant.getName());

        TextView restaurantAddressText = (TextView) findViewById(R.id.restaurant_address);
        restaurantAddressText.setText(restaurant.getAddress());
        TextView restaurantTypeText = (TextView) findViewById(R.id.restaurant_type);
        restaurantTypeText.setText(restaurant.getType());
        TextView restaurantPhoneText = (TextView) findViewById(R.id.restaurant_phone);
        restaurantPhoneText.setText(restaurant.getPhone());
        CheckBox restaurantParkingBox = (CheckBox) findViewById(R.id.restaurant_parking);
        restaurantParkingBox.setClickable(false);
        restaurantParkingBox.setEnabled(false);
        restaurantParkingBox.setChecked(restaurant.getParking());
        CheckBox restaurantKosherBox = (CheckBox) findViewById(R.id.restaurant_kosher);
        restaurantKosherBox.setClickable(false);
        restaurantKosherBox.setEnabled(false);
        restaurantKosherBox.setChecked(restaurant.getKosher());
        CheckBox restaurantAccessibleBox = (CheckBox) findViewById(R.id.restaurant_accessible);
        restaurantAccessibleBox.setClickable(false);
        restaurantAccessibleBox.setEnabled(false);
        restaurantAccessibleBox.setChecked(restaurant.getAccessible());

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new CommentListFragment();

        // Comments fragment
        Bundle bundle = new Bundle();
        bundle.putString("restaurantId", restaurant.getId().toString());
        fragmentTransaction.replace(R.id.comments_frag_container, fragment);
        fragment.setArguments(bundle);
        fragmentTransaction.commit();

        ImageButton callButton = (ImageButton) findViewById(R.id.call_button);

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

        Button addReview = (Button) findViewById(R.id.add_review);

        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddCommentActivity.class);
                intent.putExtra("Restaurant", restaurant);
                startActivity(intent);
            }
        });

        ImageButton navigateButton = (ImageButton) findViewById(R.id.navigate_button);
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
                }
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        unbindDrawables(findViewById(R.id.content_restaurant));
        System.gc();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void unbindDrawables(View view)
    {
        System.out.println("unbinding...");
        if (view.getBackground() != null)
        {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView))
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
                System.out.println("unbinding... " + i);
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
//    @Override
//    public void unbindDrawables(View view) {
//        if (view.getBackground() != null) {
//            view.getBackground().setCallback(null);
//        }
//        if (view instanceof ViewGroup) {
//            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//                unbindDrawables(((ViewGroup) view).getChildAt(i));
//            }
//            ((ViewGroup) view).removeAllViews();
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        unbindDrawables(mGridView);
//        mGridView = null;
//        gridAdapter = null;
//        System.gc();
//        Runtime.getRuntime().gc();
//    }
}
