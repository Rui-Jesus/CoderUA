package com.google.firebase.codelab.coderua;

import android.content.Context;
import android.graphics.BitmapFactory;

import java.util.HashMap;

/**
 * Created by ruigr on 22/11/2017.
 */

public class MobsHolder {

    private static MobsHolder instance;

    private HashMap<Integer, Mob> appMobs = new HashMap<>();


    /**
     * Creates the base list of mobs existent in the game
     * @param context Context of the activity that summons this service
     */
    private MobsHolder(Context context){
        appMobs.put(100, new Mob(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ctos_mob), 100, "Ctos", "C"));
        appMobs.put(150, new Mob(BitmapFactory.decodeResource(context.getResources(), R.mipmap.katana_launcher), 150, "Katana", "C"));
        appMobs.put(200, new Mob(BitmapFactory.decodeResource(context.getResources(), R.mipmap.beanus_mob), 200, "Beanus", "Java"));
        appMobs.put(250, new Mob(BitmapFactory.decodeResource(context.getResources(), R.mipmap.javlo_mob), 250, "Javlo", "Java"));
        appMobs.put(300, new Mob(BitmapFactory.decodeResource(context.getResources(), R.mipmap.samjo_mob), 300, "SamJo", "Python"));
        appMobs.put(350, new Mob(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ctos_mob), 350, "Diag", "Python"));
    }

    public static synchronized MobsHolder getInstance(Context context) {
        if (instance == null)
            instance = new MobsHolder(context);
        return instance;
    }

    public Mob getMobById(int mobID) { return appMobs.get(mobID); }

    public HashMap<Integer, Mob> getAppMobs() { return appMobs;  }

}
