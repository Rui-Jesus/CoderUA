package com.google.firebase.codelab.coderua;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MainActivity";
    private GoogleApiClient mGoogleApiClient;

    private boolean first;

    private static boolean serviceLaunched;

    //By the default, the username is anonymous
    public static final String ANONYMOUS = "anonymous";

    //To store username and his google photo
    private String mUsername;
    private String mPhotoUrl;

    //Firebase variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private AdView mAdView;

    //Google api variables
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean canFillLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        first = true;
        canFillLayout = true;

        serviceLaunched = false;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        //If user is loged in, he goes directly into the menu, if not, he is requested the sign in
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        //When the user logs in, we set him up
        DatabaseManager.setUser(this, mFirebaseUser.getEmail(), mUsername);
        DataHolder.getInstance().setmFirebaseAuth(mFirebaseAuth);
        DataHolder.getInstance().setmGoogleApiClient(mGoogleApiClient);
        /* To place the adds */
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //For location
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission();

        /* The user cannot go back to this activity */
        Button pressed = (Button) findViewById(R.id.home);
        //Button test = findViewById(R.id.testButton);
        //test.setVisibility(View.GONE);
        pressed.setEnabled(false);
        pressed.setTextColor(Color.parseColor("#000000"));
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            DataHolder.getInstance().setPermissionsGranted(true);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    DataHolder.getInstance().setPermissionsGranted(true);
                    Intent intent = new Intent(this,LocationService.class);
                    this.startService(intent);
                    serviceLaunched = true;
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //If the user already gave permissions, the start above won't launch
        if(!serviceLaunched) {
            //Intent intent = new Intent(this, LocationService.class);
            //this.startService(intent);
        }

    }

    /* Métodos para navegação entre as páginas */
    protected void goToCodex(View v){
        Intent intent = new Intent(this, CodexPage.class);
        startActivity(intent);
    }

    protected void goToMap(View v){
        Intent intent = new Intent(this, MapsActivity2.class);
        startActivity(intent);
    }

    protected void goToSkills(View v){
        Intent intent = new Intent(this, SkillsPage.class);
        startActivity(intent);
    }

    protected void logoutClick(View v){
        mFirebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        mUsername = ANONYMOUS;
        //We want to stop the running service
        stopService(new Intent(MainActivity.this, LocationService.class));
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    //We're simulating a mob catch, the mob in this case is Ctos
    protected void callCatch(View v){
        Intent intent = new Intent(this, CatchActivity.class);
        intent.putExtra("mobID", 100);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        first = false;
        if (mAdView != null)
            mAdView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null)
            mAdView.resume();
        if (!first){
            //The user level might have changed, we need to update it
            User user = DataHolder.getInstance().getCurrentUser();
            if(user != null) {
                ProgressBar bar = findViewById(R.id.levelBar);
                bar.setProgress(user.getPercentage());
                TextView level = findViewById(R.id.levelText);
                level.setText("Lv: " + user.getLevel());
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    public void fillLayout() {
        if (canFillLayout) {
            User user = DataHolder.getInstance().getCurrentUser();
            ProgressBar bar = findViewById(R.id.levelBar);
            int percentage = user.getPercentage();
            bar.setProgress(percentage);
            TextView level = findViewById(R.id.levelText);
            level.setText("Lv: " + user.getLevel());
            TextView username = findViewById(R.id.username);
            username.setText(user.getUid());
            if(!serviceLaunched) {
                Intent intent = new Intent(this, LocationService.class);
                this.startService(intent);
            }
        }
        canFillLayout = false;
    }

}
