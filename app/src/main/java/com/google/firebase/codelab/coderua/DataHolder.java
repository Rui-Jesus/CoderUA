package com.google.firebase.codelab.coderua;

import android.location.Location;

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

    /* Structure that stores in memory all mobs of the app
     * In theory this is the most efficient way given the device has the memory for it
      * In a future implementation this could be stored in a local file, although on fast devices IO could become a bottleneck*/


    public static synchronized DataHolder getInstance() {
        if (instance == null)
            instance = new DataHolder();
        return instance;
    }

    private DataHolder() {
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

}
