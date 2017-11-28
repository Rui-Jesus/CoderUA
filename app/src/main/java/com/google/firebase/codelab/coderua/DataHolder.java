package com.google.firebase.codelab.coderua;

import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * A class to hold all the shared data between applications.
 * It has a singleton behaviour, and the method to access this instance is sync
 */
public class DataHolder {

    private static DataHolder instance;

    private boolean permissionsGranted;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private ArrayList<Mob> listOfMobs;
    private ArrayList<Mob> mobsToRemove;
    private User currentUser;
    private FirebaseAuth mFirebaseAuth;
    private GoogleApiClient mGoogleApiClient;

    public static synchronized DataHolder getInstance() {
        if (instance == null)
            instance = new DataHolder();
        return instance;
    }

    private DataHolder() {
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }


    public boolean getPermissionsGranted() {
        return permissionsGranted;
    }

    public void setPermissionsGranted(boolean permissionsGranted) {
        this.permissionsGranted = permissionsGranted;
    }

    public Location getmCurrentLocation() { return mCurrentLocation; }

    public void setmCurrentLocation(Location location){
        this.mCurrentLocation = location;
    }

    public String getmLastUpdateTime() { return mLastUpdateTime; }

    public void setmLastUpdateTime(String date){
        this.mLastUpdateTime = date;
    }

    public ArrayList<Mob> getListOfMobs () { return listOfMobs; }

    public void setListOfMobs(ArrayList<Mob> listOfMobs){ this.listOfMobs = listOfMobs; }

    public User getCurrentUser() { return currentUser; }

    public void setCurrentUser(User currentUser) { this.currentUser = currentUser; }

    public ArrayList<Mob> getMobsToRemove() { return mobsToRemove; }

    public void setMobsToRemove(ArrayList<Mob> lst){ mobsToRemove = lst; }

    public void setmFirebaseAuth(FirebaseAuth auth) { mFirebaseAuth = auth; }

    public FirebaseAuth getmFirebaseAuth() { return mFirebaseAuth; }
}
