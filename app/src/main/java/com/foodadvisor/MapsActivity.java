package com.foodadvisor;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.foodadvisor.DAL.DBHandler;
import com.foodadvisor.models.Model;
import com.foodadvisor.models.Restaurant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient = null;
    private Location mLastLocation;
    GPSTracker gps;
    private double latitude;
    private double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /* = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity *//*,
                        this /* OnConnectionFailedListener *//*)*/
                /*.addApi(LocationServices.API)
                //.addScope(LocationServices.)
                .build();*/
        /*if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }*/
        gps = new GPSTracker(MapsActivity.this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
        else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }




        /*.getR .getService().GetRecommendedParking(numberOfParkings, latitude, longitude,
                new Callback<List<Parking>>() {
                    @Override
                    public void success(List<Parking> parkings, Response response) {
                        parkings_list = parkings;
                        SetDataOnMap(parkings);
                    }

                    @Override
                    public void failure(RetrofitError error) { }
                });*/


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
//       LatLng x = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        LatLng y = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(y));
        mMap.addMarker(new MarkerOptions()
                .position(y)
                .title("Ori's house"));


        Model.instance().getRestaurants(new Model.GetRestaurantsListener() {
            @Override
            public void done(List<Restaurant> stList) {
                for (Restaurant restaurant : stList){
                    mMap.addMarker(new MarkerOptions().title(restaurant.getName()).position(new LatLng(restaurant.getLatitude(), restaurant.getLongitude()))).setTag(restaurant);
                }
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(getBaseContext(), RestaurantActivity.class);
                intent.putExtra("Restaurant", (Restaurant)marker.getTag());
                startActivity(intent);
                return false;
            }
        });

    }

    private void drawMarker(Location location){
        mMap.clear();

        //  convert the location object to a LatLng object that can be used by the map API
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

        // zoom to the current location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,16));

        // add a marker to the map indicating our current position
        mMap.addMarker(new MarkerOptions()
                .position(currentPosition)
                .snippet("Lat:" + location.getLatitude() + "Lng:"+ location.getLongitude()));
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mMap != null)
        {drawMarker(location);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
