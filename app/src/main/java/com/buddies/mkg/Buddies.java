package com.buddies.mkg;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trendit.mkg.trendit.R;

import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Buddies extends AppCompatActivity implements OnMapReadyCallback {

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
    private String[] SPINNERLIST = {"Android Material Design", "Material Design Spinner", "Spinner Using Material Library", "Material Spinner Example", "ds", "yre", "gfdffd", "jjfjj", "fdgfdf", "fdgg"};
    private GoogleMap mapObj;


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

        FloatingActionButton filterSearch = (FloatingActionButton) findViewById(R.id.filterSearch);
        filterSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getBaseContext(), SearchFilter.class);
                startActivity(intent);

            }
        });


      /*  CardView card_view = (CardView)findViewById(R.id.card_view);
       // card_view.setVisibility(View.GONE);

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeId);
        LayoutParams layoutParams2 = new LayoutParams(MATCH_PARENT, LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(layoutParams2);
*/




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
        map.animateCamera(CameraUpdateFactory.zoomTo(5));

        List<Person> personList = JsonProcess.getPersonResultByString();
        for (Person person : personList) {


            double distance = Util.calculationByDistance(latLng, new LatLng(person.getLatitude(), person.getLongitude()));
            // MarkerOptions marker = new MarkerOptions().position(new LatLng(person.getLatitude(), person.getLongitude())).title(person.getPersonId()+" is "+distance+" far");
            MarkerOptions marker = new MarkerOptions().position(new LatLng(person.getLatitude(), person.getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(Util.getRoundedCroppedBitmap(BitmapFactory.decodeResource(getResources(),
                    R.mipmap.ic_launcher),60)))
                    // Specifies the anchor to be at a particular point in the marker image.
                    .anchor(0.5f, 1);
            map.addMarker(marker);
        }

    }



    /*private void initiateSearchFilterPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) Buddies.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Inflate the view from a predefined XML layout
        View layout = inflater.inflate(R.layout.filter_search_popup_layout,
                (ViewGroup) findViewById(R.id.popup_element));


        final PopupWindow pw = new PopupWindow(layout, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

        Spinner popupSpinner = (Spinner)layout.findViewById(R.id.popupspinner);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(Buddies.this,
                        android.R.layout.simple_spinner_item, SPINNERLIST);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        popupSpinner.setAdapter(adapter);







        // GridView castLst = (GridView) findViewById(R.id.castLst);
      //  RecyclerView posterImgLst = (RecyclerView) layout.findViewById(R.id.posterLst);
        Button backButton = (Button) layout.findViewById(R.id.popup_close);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
            }
        });

     //   posterImgLst.setHasFixedSize(true);

        // RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        // cast.addItemDecoration(itemDecoration);

        *//*final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        posterImgLst.setItemAnimator(new DefaultItemAnimator());
        posterImgLst.setLayoutManager(layoutManager);

        imagesUrl = CommonUtils.getIdString(getResources(), R.string.base_url)
                + "/" + CommonUtils.getIdString(getResources(), R.string.person)
                + "/" + id
                + "/" + CommonUtils.getIdString(getResources(), R.string.images)
                + "?api_key="
                + CommonUtils.getIdString(getResources(), R.string.api_data)
                + "&include_adult=" + CommonUtils.getFamilyFilter(this) + "&language=" + CommonUtils.getLanguage(this) + "&include_image_language=" + CommonUtils.getLanguage(this);
        posterImgLst.setAdapter(new ProfileImagesRecyclerViewAdapter(this, new JSONArray()));

        ProfileImagesDtlsProcess posterImageProcess = new ProfileImagesDtlsProcess();
        // posterImageProcess.setProgressBar(progressBar);
        posterImageProcess.execute(posterImgLst, this, imagesUrl);*//*
    }*/


}