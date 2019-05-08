package com.example.googlemapactivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import java.lang.Object;
import android.os.Parcelable;

import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnPoiClickListener, View.OnClickListener {

    public GoogleMap mMap;
    double currentLat, currentLng;
    LatLng carLocation;
    int carLocationIndex = 1; // we'll use this in our demo... to cycle through three different parking spots
    boolean navigationOn = false;
    Button currentLocation, navigateBtn;
    LocationManager locationManager;
    LocationListener locationListener;
    LocationHandler locationHandler;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        currentLocation = (Button) findViewById(R.id.locationButton);
        navigateBtn = (Button) findViewById(R.id.navigateButton);
        currentLocation.setOnClickListener(this);
        navigateBtn.setOnClickListener(this);

        // DAVID CHANGESET
        locationHandler = new LocationHandler(this);  /// AAAAHHHH HHHHHHAAAAA
        // END CHANGESET

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
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Marker markercarLocation, markeruserLocation;
                mMap.clear(); // clears all the markers

                // get the user's location updated
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16f));
                markeruserLocation = mMap.addMarker(new MarkerOptions().position(userLocation).title("Moving Me!"));
                //LatLng eastLot = new LatLng(33.296195, -111.795448);
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                currentLat = location.getLatitude();
                currentLng = location.getLongitude();
                // some leftover debug code from trying to figure out the database linkage problem
                // currentLat = Double.valueOf(location.getLatitude());   // tried for debug
                // currentLng = Double.valueOf(location.getLongitude());
                //boolean tossaway;  insertLocation returns true if successful
                //tossaway = locationHandler.insertLocation(currentLat, currentLng);


                // okay, here's the conditional code... if we're in Navigate mode, let's show more
                if (navigationOn){

                   // make a marker for the car
                    markercarLocation = mMap.addMarker(new MarkerOptions().position(carLocation).title("YOUR CAR").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).visible(true));


                    // below is all debug... making a bunch of markers
                    //add two more markers onto the map
                    // LatLng CGCCNorthLot = new LatLng(locationHandler.getLat(2), locationHandler.getLng(2));
                    //markercarLocation = mMap.addMarker(new MarkerOptions().position(CGCCNorthLot).title("North Lot"));
                    //LatLng westLot = new LatLng(locationHandler.getLat(4), locationHandler.getLng(4));
                    //markercarLocation = mMap.addMarker(new MarkerOptions().position(westLot).title("West lot"));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CGCCNorthLot, 16f));


                    // okay, now for the fun... see if we can autosize the map
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    //for (Marker marker : markers) {
                    //    builder.include(marker.getPosition());
                    //}
                    //I will just jam in the two markers that we're using
                    builder.include(markeruserLocation.getPosition());
                    builder.include(markercarLocation.getPosition());
                    LatLngBounds bounds = builder.build();

                    // now that we have a bounding box, let's tell the map to zoom in
                    // first, create a movement description

                    // this first attempt put the markers at the absolute edge of the display
                    // int padding = 0; // offset from edges of the map in pixels
                    //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                    // so, a better approach is to calculate a pad (thanks Stack Overflow)

                    int width = getResources().getDisplayMetrics().widthPixels;
                    int height = getResources().getDisplayMetrics().heightPixels;
                    int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen
                    // someone suggested using height instead... so I tried it, but didn't like it
                    // int padding = (int) (height * 0.20); // offset from vertical edges of the map 20% of screen

                    // let's try making it a % of whichever is greater
                    int verticalpadding = (int) (height * 0.10);
                    if (padding < verticalpadding){
                        padding = verticalpadding;
                    }

                    // Now, I'm not sure I like the extra parameters on this call... compare and contrast
                    //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

                    // here is the old call...
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);



                    //now, move the camera... you can do it one of two ways...
                    // mMap.moveCamera(cu);  //this one moves the camera

                    mMap.animateCamera(cu); // this one animates the camera

                    // now... time to add the routing information

                } else{
                    // we are back to just watching current position and centering around that
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18f));

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
        };

        if (Build.VERSION.SDK_INT < 23)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0 , 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                mMap.clear(); // clear off all the markers
                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Moving Me API<23"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16f));

                currentLat = lastKnownLocation.getLatitude();
                currentLng = lastKnownLocation.getLongitude();

                //add two more markers onto the map
                LatLng CGCCNorthLot = new LatLng(locationHandler.getLat(2), locationHandler.getLng(2));
                mMap.addMarker(new MarkerOptions().position(CGCCNorthLot).title("North Lot"));
                LatLng westLot = new LatLng(locationHandler.getLat(4), locationHandler.getLng(4));
                mMap.addMarker(new MarkerOptions().position(westLot).title("West lot"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CGCCNorthLot, 16f));
            }
        }
    }

    @Override
    public void onPoiClick(PointOfInterest pointOfInterest)
    {
        Toast.makeText(getApplicationContext(), "Clicked: " + pointOfInterest.name + "\nPlace ID: "
                + pointOfInterest.placeId
                + "\nLatitude: " + pointOfInterest.latLng.latitude
                + "Longitude: " + pointOfInterest.latLng.longitude, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.locationButton:
                // pops the camera over to the current location, and stores in the database
                LatLng userLocation = new LatLng(currentLat, currentLng);
                //mMap.addMarker(new MarkerOptions().position(userLocation).title("Me!"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18f));



                // DAVID CHANGESET BELOW
                Toast.makeText(getApplicationContext(), "CURRENT LOCATION: " +
                        "\nLatitude: " + currentLat
                        + "  Longitude: " + currentLng, Toast.LENGTH_LONG).show();
                boolean tossresult;  // insertLocation returns a boolean
                tossresult = locationHandler.insertLocation(currentLat, currentLng);

                // END OF CHANGESET
                break;

            case R.id.navigateButton:
                // At this moment, I"m going to try to auto-rezoom the display to show current location
                // and a parking lot marker... in this case, the West Lot, and see if I can get
                // a navigate line to it

                if (navigationOn){
                    navigationOn = false;
                    Toast.makeText(getApplicationContext(), "Navigate OFF", Toast.LENGTH_LONG).show();

                } else {
                    navigationOn = true;
                    //Toast.makeText(getApplicationContext(), "Navigate ON", Toast.LENGTH_LONG).show();
                    double lastCarLat = locationHandler.getLat(); // pull the latest stored location
                    double lastCarLng = locationHandler.getLng();


                    // point to the car location we want to navigate to
                    // In the REAL application, we'd pick the last location in the database
                    //carLocation = new LatLng(lastCarLat, lastCarLng);

                    // In the DEMO version of the app, we're going to jam in one of three locations
                    if (carLocationIndex < 4){  // want to cycle through 2, 3 and 4
                        carLocationIndex = carLocationIndex + 1;
                    } else{
                        carLocationIndex =2;
                    }

                    //Toast.makeText(getApplicationContext(), "Index= " + carLocationIndex, Toast.LENGTH_LONG).show();
                    carLocation = new LatLng(locationHandler.getLat(carLocationIndex), locationHandler.getLng(carLocationIndex));
                    //


                    // Okay... all is set up... the carLocation is known, and navigation is on
                    // let the onLocationChanged method hand all of the update work
                    // of resizing the map, and drawing the route
                } // end of if/else







                //Toast.makeText(getApplicationContext(), "Navigate Clicked.  West and North. West = " +
                //        "\nLatitude: " + lastCarLat
                //        + "  Longitude: " + lastCarLng, Toast.LENGTH_LONG).show();
                //LatLng CGCCNorthLot = new LatLng(lastCarLat, lastCarLng);
                //mMap.addMarker(new MarkerOptions().position(CGCCNorthLot).title("North Lot"));
                //LatLng eastLot = new LatLng(33.296195, -111.795448);
                //mMap.addMarker(new MarkerOptions().position(eastLot).title("Fake for me"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CGCCNorthLot, 16f));

                break;
        }
    }
}
