package com.google.firebase.codelab.coderua;

import android.graphics.Bitmap;
import android.location.Location;

/**
 * Created by ruigr on 20/11/2017.
 * A class that will represent our mobs in the game
 */
public class Mob {

    /**
     * The location of the mob
     */
    private Location location;

    /**
     * Flag to identify if this monster was already notified to the user, in case he was near by
     */
    private boolean wasFocused;

    /**
     * The image thaat represents the mob
     */
    private Bitmap image;

    //Will be used in the future
    private int mobID;
    private String name;
    private String type;
    private static int count = 0;
    private int internalId;

    public Mob(Bitmap image, int mobID, String name, String type, Location location){
        this.image = image;
        this.mobID = mobID;
        this.name = name;
        this.type = type;
        this.location = location;
        wasFocused = false;
        internalId = count++;
    }

    public Mob(Bitmap image, int mobID, String name, String type){
        this.image = image;
        this.mobID = mobID;
        this.name = name;
        this.type = type;
        wasFocused = false;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean getWasFocused() {
        return wasFocused;
    }

    public void setWasFocused(boolean wasFocused) {
        this.wasFocused = wasFocused;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getMobID() {
        return mobID;
    }

    public void setMobID(int mobID) {
        this.mobID = mobID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInternalId() { return internalId; }

    public void setInternalId(int id) { internalId = id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mob)) return false;

        Mob mob = (Mob) o;

        if (getWasFocused() != mob.getWasFocused()) return false;
        if (getMobID() != mob.getMobID()) return false;
        if (!getLocation().equals(mob.getLocation())) return false;
        if (!getImage().equals(mob.getImage())) return false;
        if (!getName().equals(mob.getName())) return false;
        return type.equals(mob.type);

    }

    @Override
    public int hashCode() {
        int result = getLocation().hashCode();
        result = 31 * result + (getWasFocused() ? 1 : 0);
        result = 31 * result + getImage().hashCode();
        result = 31 * result + getMobID();
        result = 31 * result + getName().hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
