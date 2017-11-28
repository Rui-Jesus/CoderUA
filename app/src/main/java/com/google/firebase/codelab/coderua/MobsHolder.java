package com.google.firebase.codelab.coderua;

import android.content.Context;
import android.graphics.BitmapFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

/**Structure that stores in memory all mobs of the app
 * In theory this is the most efficient way given the device has the memory for it
 * In a future implementation this could be stored in a local file, although on fast devices IO could become a bottleneck
 */
public class MobsHolder {

    private static MobsHolder instance;

    private HashMap<Integer, Mob> appMobs = new HashMap<>();

    private TreeSet<Integer> rareMobsIds = new TreeSet<>();

    private TreeSet<Integer> commonMobsIds = new TreeSet<>();

    public static MobsHolder getInstance() {
        return instance;
    }

    /**
     * Creates the base list of mobs existent in the game
     * @param context Context of the activity that summons this service
     */
    private MobsHolder(Context context){

        //The rare mobs are fixed .. decided by us
        rareMobsIds.add(350);
        rareMobsIds.add(150);

        commonMobsIds.add(100);
        commonMobsIds.add(200);
        commonMobsIds.add(250);
        commonMobsIds.add(300);



        appMobs.put(100, new Mob(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ctos_mob), 100, "Ctos", "C"));
        appMobs.put(150, new Mob(BitmapFactory.decodeResource(context.getResources(), R.mipmap.katana_launcher), 150, "Katana", "C"));
        appMobs.put(200, new Mob(BitmapFactory.decodeResource(context.getResources(), R.mipmap.beanus_mob), 200, "Beanus", "Java"));
        appMobs.put(250, new Mob(BitmapFactory.decodeResource(context.getResources(), R.mipmap.javlo_mob), 250, "Javlo", "Java"));
        appMobs.put(300, new Mob(BitmapFactory.decodeResource(context.getResources(), R.mipmap.samjo_mob), 300, "SamJo", "Python"));
        appMobs.put(350, new Mob(BitmapFactory.decodeResource(context.getResources(), R.mipmap.diag_mob), 350, "Diag", "Python"));
    }

    public static synchronized MobsHolder getInstance(Context context) {
        if (instance == null)
            instance = new MobsHolder(context);
        return instance;
    }

    public Mob getMobById(int mobID) { return appMobs.get(mobID); }

    public HashMap<Integer, Mob> getAppMobs() { return appMobs;  }

    public TreeSet<Integer> getRareMobsIds() { return rareMobsIds; }

    public void setRareMobsIds(TreeSet<Integer> rareMobsIds) { this.rareMobsIds = rareMobsIds; }

    public TreeSet<Integer> getCommonMobsIds() { return commonMobsIds; }

    public void setCommonMobsIds(TreeSet<Integer> commonMobsIds) { this.commonMobsIds = commonMobsIds; }


}
