package com.google.firebase.codelab.coderua;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;


/**
 * Created by ruigr on 16/11/2017.
 */

public class LocationService extends Service {

    public static final String TAG = LocationService.class.getSimpleName();
    public static final String ACTION = "LOCATION_UPDATE";
    public static final String ACTION2 = "NEW_LOCATION";
    public static final String ACTION3 = "LIST_CHANGED";

    private String mLastUpdateTime;
    private Random rand;

    /*Variables for location updates*/
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mCurrentLocation;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private ArrayList<Mob> listOfMobs;
    private int mobCount; //To give unique internal ids to the mobs created

    /* Support tools */
    private Thread locationThread;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;


    @Override
    public void onCreate (){
        super.onCreate();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        listOfMobs = new ArrayList<>();

        mLastUpdateTime = "";
        rand = new Random();
        mobCount = 0;
        //Let's start a thread to create some heavy job
        locationThread = new Thread(locationJob);

        Log.i(TAG, "Finished On Create");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "On StartCommand");
        createLocationRequest();

        Log.i(TAG, "Starting location callback");
        createLocationCallback();
        Log.i(TAG, "Starting location updates");
        startLocationUpdates();

        //We only want to start the thread if it wasn´t started already
        if(!locationThread.isAlive())
            locationThread.start();

        return START_STICKY;
        //return START_NOT_STICKY;
    }

    /**
     * Runnable object to implement some heavy code that could block the UI otherwise
     */
    private Runnable locationJob = new Runnable() {
        public void run(){

            //We want to make sure that there already exists a location from the api client
            //And since we are generating the points from time to time, there will be no issues
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while(true){

                checkDistance(0.3, 0.050, 0.020); //300 meters | 50 meters | 20 meters
                //Running this thread non-stop is quite heavy, and once checkDisntance is ran, we don´t need to immediately start again
                try {
                    Thread.sleep(3500); //wait 3.5 seconds at the end of each turn
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    /**
     * Creates a location request to the fused location client
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                DataHolder.getInstance().setmCurrentLocation(mCurrentLocation);
                DataHolder.getInstance().setmLastUpdateTime(mLastUpdateTime);

                Intent intent = new Intent(ACTION);
                sendLocationBroadcast(intent, mCurrentLocation, null, 0);

                //We got a new point, we want to generate a new position around it
                //The double will be an argument but for now is static. It's represented in KM
                generateCoords(mCurrentLocation, 275); //175 meters around the location

            }
        };
    }

    /**
     * Method to send warning to the system about multiple events. It only broadcasts them internally
     * @param intent The type of action
     * @param location The location to transmit to the system
     * @param bitmap In case we're creating a mob, this will be it's representation
     */
    private void sendLocationBroadcast(Intent intent, Location location, Bitmap bitmap, int internalID){
        intent.putExtra("latitude", location.getLatitude());
        intent.putExtra("longitude", location.getLongitude());
        if(bitmap != null){
            intent.putExtra("bitmap", bitmap);
            intent.putExtra("internalID", internalID);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Log.i(TAG, "Sending broadcast");
    }

    private void sendListChangedBroadcast(Intent intent, double nToRemove){
        intent.putExtra("nToRemove", nToRemove);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Log.i(TAG, "Sending list changed broadcast");
    }

    /**
     * Method to receive periodically location updates from the fused location client
     */
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        if(DataHolder.getInstance().getPermissionsGranted()){
            Log.i(TAG, "All location settings are satisfied.");

            //noinspection MissingPermission
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    /**
     * Method to generate random coords around a location
     * @param location User last known location, provided by google api
     * @param radius Distance from the user location, in Kilometers
     */
    public void generateCoords(Location location, double radius){

        //We only want to have 3 locations being displayed at a time
        //The number will be an argument in the future
        if(listOfMobs.size() < 5){

            Random random = new Random();
            double radiusInDegrees = radius / 111000f;

            double u = random.nextDouble();
            double v = random.nextDouble();
            double w = radiusInDegrees * Math.sqrt(u);
            double t = 2 * Math.PI * v;
            double x = w * Math.cos(t);
            double y = w * Math.sin(t);

            //Adjust the x coords around the earth curvature
            double new_x = x / Math.cos(Math.toRadians(location.getLatitude()));

            double finalLongitude = new_x + location.getLongitude();
            double finalLatitude = y + location.getLatitude();

            Location finalLocation = new Location("not important");
            finalLocation.setLongitude(finalLongitude);
            finalLocation.setLatitude(finalLatitude);
            Mob mob = generateMob();
            mob.setLocation(finalLocation);
            listOfMobs.add(mob);
            //Update the list in the data holder
            DataHolder.getInstance().setListOfMobs(listOfMobs);

            //Lets warn the maps activity that a new point was created and needs to be represented on the map
            //we need a different message to let the map know how to handle the points given
            Intent intent = new Intent(ACTION2);
            Log.i(TAG, "New Spawn, sending broadcast, it's id is: " + mob.getInternalId());
            sendLocationBroadcast(intent, finalLocation, mob.getImage(), mob.getInternalId());

        }
        else{
            Log.i(TAG, "Tried to create more coords but we already reached max number");
            return;
        }

    }

    private Mob generateMob(){

        HashMap<Integer, Mob> mobDict = MobsHolder.getInstance(this).getAppMobs();
        int aux = mobDict.size();

        int randomNum = rand.nextInt(aux);
        int id = (int) mobDict.keySet().toArray()[randomNum];

        Mob mob = mobDict.get(id);
        mob.setInternalId(mobCount++);

        return mobDict.get(id);

    }

    /**
     * A function that will check if our current calculated points need to be deleted due to being too far away from the user already
     * @param dist The distance at which the points will be deleted
     * @param closeDist The distance at which the user is close enough to receive a notification about a mob
     * @param catchDist The distance at which the user can catch the mob
     * @return Wether or not we need to update our points on the map
     */
    public boolean checkDistance(double dist, double closeDist, double catchDist){
        if(listOfMobs.size() <= 0){
            return false; //List is empty, no points to be deleted
        }

        int earthRadiusKm = 6371;
        boolean needsRemoval = false;
        double nRemoved = 0; //To help in the process
        ArrayList<Mob> mobsToRemove = new ArrayList<>(); //To help in the process

        //We will be removing objects from the list in this proccess, we have to iterate backwards.
        for(int i = listOfMobs.size()-1; i>=0; i--){
            Mob mob = listOfMobs.get(i);
            Location loc = mob.getLocation();
            double dLat = degreesToRadians(mCurrentLocation.getLatitude() - loc.getLatitude());
            double dLon = degreesToRadians(mCurrentLocation.getLongitude() - loc.getLongitude());

            double lat1 = degreesToRadians(loc.getLatitude());
            double lat2 = degreesToRadians(mCurrentLocation.getLatitude());

            double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

            double distanceInBetween = earthRadiusKm * c;
            Log.i(TAG, "OUR DISTANCE IS: " + distanceInBetween * 1000);

            //Both dist and distanceInBetween come in KM
            if(dist < distanceInBetween){
                needsRemoval = true;
                //removeMobFromList(mob);
                mobsToRemove.add(mob);
                listOfMobs.remove(i);
                nRemoved++;
                Log.i(TAG, "Id of mob to be removed: " + mob.getInternalId());
            }
            //Is close enough to send a notification
            //However, if this specific mob was already notified, we don´t want to notify it again, or else the phone won't stop ringing
            else if ((catchDist < distanceInBetween && distanceInBetween <= closeDist) && !mob.getWasFocused()){
                //Get the default sound
                Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.app_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                                R.mipmap.app_launcher))
                        .setContentTitle("Monster near by !!!")
                        .setContentText("A wild programmer has appeared")
                        .setSound(uri)
                        .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
                        //.setContentIntent(pendingIntent);

                android.app.NotificationManager notificationManager =
                        (android.app.NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);

                /* ID of notification */
                int mId = 010;
                notificationManager.notify(mId, notificationBuilder.build());
                mob.setWasFocused(true); //The mob got the focus, no longer needs notifications
            }
            //Is close enough for the user to catch it
            else if( distanceInBetween < catchDist ){
                Log.i("TAG", "I catched something");
                //If this is active is not active, we want to start it.
                //It might happen that the user has 2 mobs really close by and he can´t catch them both at the same time
                if(CatchActivity.notActive) {
                    Intent intent = new Intent(LocationService.this, CatchActivity.class);
                    intent.putExtra("mobID", mob.getMobID());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplication().startActivity(intent);
                    //The mob was caught, we want to remove it from the list
                    needsRemoval = true;
                    nRemoved++;
                    //removeMobFromList(mob);
                    listOfMobs.remove(i);
                    mobsToRemove.add(mob);
                }
            }

        }

        //Update the list in the data holder
        DataHolder.getInstance().setListOfMobs(listOfMobs);
        DataHolder.getInstance().setMobsToRemove(mobsToRemove);

        //Items to remove were detected, we have to warn the system
        if(needsRemoval){
            Intent intent = new Intent(ACTION3);
            sendListChangedBroadcast(intent, nRemoved);
        }

        return needsRemoval;

    }

    private double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    private void removeMobFromList(Mob mob){
        for(int i = 0; i<listOfMobs.size(); i++){
            if(listOfMobs.get(i).equals(mob))
                listOfMobs.remove(i);
        }
    }

}
