package com.google.firebase.codelab.coderua;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {

    private String uid; //username
    private String email;
    private int level;
    private int nmobs; //numero de mobs que aparecem no mapa
    private ArrayList<Integer> mobsCaught;
    private int proximity; //proximidade com que os mobs começam a aparecer no mapa
    private int rarerate; //probabilidade de nascer um mob raro
    private int range; //distancia a que tem de estar do mob para o apanhar
    private int percentage; //percentagem a que esta o nivel
    private int upgradeAvailable; //numero de vezes que o user ainda pode evoluir skills

    //se o user ainda nao existir é criado com os dados essenciais, tendo os restantes com o seu default value
    public User(String uid, String email){
        this.uid = uid;
        this.email = email;
        level = 1;
        nmobs = 3; //minimo
        mobsCaught = new ArrayList<Integer>(); //ainda nao apanhou nenhum
        proximity = 250;
        rarerate = 5;
        range = 15; //range to catch a mob. Because google is somewhat inprecise, we give this a wide margin
        percentage = 0;
        upgradeAvailable = 0;
    }

    //se o user já existir é criado com todos os dados
    public User(String uid, String email, int level, int nmobs, ArrayList<Integer> mobsCaught, int proximity, int rarerate, int range, int percentage, int upgradeAvailable){
        this.uid = uid;
        this.email = email;
        this.level = level;
        this.nmobs = nmobs;
        this.mobsCaught = mobsCaught;
        this.proximity = proximity;
        this.rarerate = rarerate;
        this.range = range;
        this.percentage = percentage;
        this.upgradeAvailable = upgradeAvailable;
    }

    //Mapa a ser usado para fazer upload dos dados para a base de dados
    public Map<String,Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", uid);
        map.put("email", email);
        map.put("level", level);
        map.put("nmobs", nmobs);
        map.put("mobsCaught", mobsCaught);
        map.put("proximity", proximity);
        map.put("rarerate", rarerate);
        map.put("range", range);
        map.put("percentage", percentage);
        map.put("updateAvailable", upgradeAvailable);
        return map;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", level=" + level +
                ", nmobs=" + nmobs +
                ", mobsCaught=" + mobsCaught +
                ", proximity=" + proximity +
                ", rarerate=" + rarerate +
                ", range=" + range +
                ", level percentage=" + percentage +
                ", isUpgradeAvailable=" + upgradeAvailable +
                '}';
    }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public int getLevel() { return level; }

    public void setLevel(int level) { this.level = level; }

    public String getEmail() { return email; }

    public int getNmobs() { return nmobs; }

    public void setNmobs(int nmobs) { this.nmobs = nmobs; }

    public ArrayList<Integer> getMobsCaught() { return mobsCaught; }

    public void setMobsCaught(ArrayList<Integer> mobsCaught) { this.mobsCaught = mobsCaught; }

    public void addModCaught(int mobID) {
        if(mobsCaught == null)
            mobsCaught = new ArrayList<>();
        mobsCaught.add(mobID);
    }

    public int getProximity() { return proximity; }

    public void setProximity(int proximity) { this.proximity = proximity; }

    public int getRarerate() { return rarerate; }

    public void setRarerate(int rarerate) { this.rarerate = rarerate; }

    public int getRange() { return range; }

    public void setRange(int range) { this.range = range; }

    public int getPercentage() { return percentage; }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getUpgradeAvailable() {
        return upgradeAvailable;
    }

    public void setUpgradeAvailable(int upgradeAvailable) {
        this.upgradeAvailable = upgradeAvailable;
    }
}
