package com.trendit.mkg.trendit;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Buddies extends AppCompatActivity implements OnMapReadyCallback ,PlaceSelectionListener {


    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] CAMERA_PERMS = {
            Manifest.permission.CAMERA
    };
    private static final String[] CONTACTS_PERMS = {
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST = 1337;
    private static final int CAMERA_REQUEST = INITIAL_REQUEST + 1;
    private static final int CONTACTS_REQUEST = INITIAL_REQUEST + 2;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;
    private GoogleMap mapObj;

    /**
     * Helper method to format information about a place nicely.
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        /*Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));*/
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddies);

        final SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toast toast = Toast.makeText(this, "onCreate...", Toast.LENGTH_SHORT);
        toast.show();

        FloatingActionButton mapChange = (FloatingActionButton) findViewById(R.id.mapChange);
        mapChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mapObj.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                    mapObj.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                } else {
                    mapObj.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }
        });





    }

    /**
     * Callback invoked when a place has been selected from the PlaceAutocompleteFragment.
     */
    @Override
    public void onPlaceSelected(Place place) {

    }

    @Override
    public void onError(Status status) {
        //Log.e(TAG, "onError: Status = " + status.toString());

        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we
     * just add a marker near Africa.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mapObj = map;
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
            }
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);


        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        //Location location = locationManager.getLastKnownLocation(provider);
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


        if (locationGPS != null) {
            Toast toast = Toast.makeText(this, "locationGPS...", Toast.LENGTH_SHORT);
            toast.show();
            onLocationChanged(locationGPS, map);
        } else if (locationNet != null) {
            Toast toast = Toast.makeText(this, "locationNet...", Toast.LENGTH_SHORT);
            toast.show();
            onLocationChanged(locationNet, map);
        } else {
            Toast toast = Toast.makeText(this, "location not fund...", Toast.LENGTH_SHORT);
            toast.show();
        }



        /*// latitude and longitude
        double latitude = 1;
        double longitude = 2;

    // create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps ");
        map.addMarker(marker);*/




    }

    public void onLocationChanged(Location location, GoogleMap map) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
       map.animateCamera(CameraUpdateFactory.zoomTo(15));

        List<Person> personList = JsonProcess.getPersonResultByString();
        for (Person person  :personList) {


            double distance = Util.calculationByDistance(latLng,new LatLng(person.getLatitude(),person.getLongitude()));
            MarkerOptions marker = new MarkerOptions().position(new LatLng(person.getLatitude(), person.getLongitude())).title(person.getPersonId()+" is "+distance+" far");
            map.addMarker(marker);
        }

    }


}