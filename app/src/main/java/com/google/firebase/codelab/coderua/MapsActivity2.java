package com.google.firebase.codelab.coderua;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapsActivity2 extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final String TAG = MapsActivity2.class.getSimpleName();
    public static final String ACTION = "LOCATION_UPDATE";
    public static final String ACTION2 = "NEW_LOCATION";
    public static final String ACTION3 = "LIST_CHANGED";

    private static boolean canDraw; //To control the drawMarkers()

    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private Marker userMarker;

    private boolean firstTime;
    private HashMap<Integer,Marker> mapOfMarkers;
    private HashMap<MarkerOptions, String> mapOfOptions;
    private static final int DEFAULT_ZOOM = 15;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mCurrentLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final String MARKERS = "markers";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
            //mapOfMarkers = (HashMap) savedInstanceState.getSerializable(MARKERS);
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        firstTime = true;
        canDraw = false;
        mapOfMarkers = new HashMap<>();
        mapOfOptions = new HashMap<>();

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        /* The user cannot go back to this activity */
        Button pressed = (Button) findViewById(R.id.map);
        pressed.setEnabled(false);
        pressed.setTextColor(Color.parseColor("#000000"));

    }

    /**
     * Receives broadcasts of the type LOCATION_UPDATE that are sent in LocationService upon creating a new location
     */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Double currentLatitude = intent.getDoubleExtra("latitude", 0);
            Double currentLongitude = intent.getDoubleExtra("longitude", 0);

            Location locationReceived = new Location("notImportant");
            locationReceived.setLatitude(currentLatitude);
            locationReceived.setLongitude(currentLongitude);

            Log.i(TAG," onRecieve ACTION 1");
            mCurrentLocation = locationReceived;
            if(userMarker == null){
                firstTime = false;
                MarkerOptions a = new MarkerOptions()
                        .position(new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                userMarker = mMap.addMarker(a);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(mMap.getCameraPosition().zoom));
            }
            else {
                userMarker.setPosition(new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude()));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(mMap.getCameraPosition().zoom));
            }

        }
    };

    /**
     * Receiver that listens for new spawns on the map
     */
    private BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Log.i(TAG," onRecieve ACTION 2");

            Double currentLatitude = intent.getDoubleExtra("latitude", 0);
            Double currentLongitude = intent.getDoubleExtra("longitude", 0);
            Integer internalID = intent.getIntExtra("internalID", 0);
            Bitmap bitmap = intent.getParcelableExtra("bitmap");

            Location locationReceived = new Location("notImportant");
            locationReceived.setLatitude(currentLatitude);
            locationReceived.setLongitude(currentLongitude);

            //We place a marker on the map with a different color, around the user
            //In the future different mobs will have different colors
            MarkerOptions a = new MarkerOptions()
                    .position(new LatLng(locationReceived.getLatitude(),locationReceived.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
            Marker m = mMap.addMarker(a);
            mapOfMarkers.put(internalID, m);
        }
    };

    /**
     * Receiver that listens for changes in the list of markers
     */
    private BroadcastReceiver mMessageReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Log.i(TAG," onRecieve ACTION 3");

            double nToRemove = intent.getDoubleExtra("nToRemove", 0);

            for(Mob m: DataHolder.getInstance().getMobsToRemove()){
                Marker marker = mapOfMarkers.get(m.getInternalId());
                if(marker != null)
                    marker.remove();
                mapOfMarkers.remove(m.getInternalId());
            }
        }
    };

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mCurrentLocation);
            //outState.putSerializable(MARKERS, mapOfMarkers);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.mapFragment), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        //Need to initialize a point first, or else the map will have no place to point at
        Location loc = DataHolder.getInstance().getmCurrentLocation();
        if(loc != null) { //The service might not have produced a location yet
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(loc.getLatitude(),
                            loc.getLongitude()), DEFAULT_ZOOM));
        }
        //Because the service may already created some points to represent, we need to to get them.
        //Once the map is up and ready to go, the other points will be caught by the broadcast receivers
        drawMarkers();
        //Turn on boradcast receivers to receive mobs and location updates
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter(ACTION));

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver2, new IntentFilter(ACTION2));

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver3, new IntentFilter(ACTION3));

    }

    protected void drawMarkers(){
        ArrayList<Mob> lst = DataHolder.getInstance().getListOfMobs();

        //The same code present in broadcast receiver 2
        if(lst != null && canDraw) { //The list might be empty, if the user opened this activity way too fast
            Log.i(TAG, "Mobs to draw: " + lst.size());
            for (Mob mob : lst) {
                MarkerOptions a = new MarkerOptions()
                        .position(new LatLng(mob.getLocation().getLatitude(), mob.getLocation().getLongitude()))
                        .icon(BitmapDescriptorFactory.fromBitmap(mob.getImage()));
                Marker m = mMap.addMarker(a);
                mapOfMarkers.put(mob.getInternalId(), m);
            }
        }
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (DataHolder.getInstance().getPermissionsGranted()) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mCurrentLocation = null;
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /* Métodos para navegação entre as páginas */
    protected void goToCodex(View v){
        Intent intent = new Intent(this, CodexPage.class);
        startActivity(intent);
    }

    protected void goToHome(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void goToSkills(View v){
        Intent intent = new Intent(this, SkillsPage.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        canDraw = true; //Next time this activity gets back it wants to draw
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(canDraw){
            drawMarkers();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        //We want to finish the listeners.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver2);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver3);
    }

}
