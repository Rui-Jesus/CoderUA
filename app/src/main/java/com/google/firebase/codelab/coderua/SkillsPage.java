package com.google.firebase.codelab.coderua;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SkillsPage extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills_page);
        ProgressBar proximity = findViewById(R.id.proximityBar);
        user = DataHolder.getInstance().getCurrentUser();
        int prox = 250-user.getProximity();
        proximity.setProgress(prox);
        TextView infoProx = findViewById(R.id.spawnInfo);
        infoProx.setText(prox + "/250");
        ProgressBar bar = findViewById(R.id.levelBar);
        int percentage = user.getPercentage();
        if (percentage == -1){
            bar.setProgress(0);
        } else {
            bar.setProgress(percentage);
        }
        int range = user.getRange();
        ProgressBar rangeBar = findViewById(R.id.rangeBar);
        rangeBar.setProgress(range);
        TextView infoRange = findViewById(R.id.rangeInfo);
        infoRange.setText(range + "/25");
        int rate = user.getRarerate();
        ProgressBar rateBar = findViewById(R.id.spawnBar);
        rateBar.setProgress(rate);
        TextView infoRate = findViewById(R.id.rareInfo);
        infoRate.setText(rate + "/25");
        int nmobs = user.getNmobs();
        ProgressBar nmobsBar = findViewById(R.id.pointsBar);
        nmobsBar.setProgress(nmobs);
        TextView infoMobs = findViewById(R.id.pointsInfo);
        infoMobs.setText(nmobs + "/8");
        TextView level = findViewById(R.id.levelText);
        String text = level.getText() + "" + user.getLevel();
        level.setText(text);
        TextView username = findViewById(R.id.username);
        username.setText(user.getUid());
        Button pressed = findViewById(R.id.skills);
        pressed.setEnabled(false);
        pressed.setTextColor(Color.parseColor("#000000"));
        checkLevel();
    }

    private void checkLevel(){
        if (user.getUpgradeAvailable()==0) {
            Button points = findViewById(R.id.pts);
            points.setEnabled(false);
            Button spawn = findViewById(R.id.spwn);
            spawn.setEnabled(false);
            Button rate = findViewById(R.id.rng);
            rate.setEnabled(false);
            Button range = findViewById(R.id.prox);
            range.setEnabled(false);
        }
        if (user.getNmobs() == 8){
            Button points = findViewById(R.id.pts);
            points.setEnabled(false);
        } if (user.getRarerate() == 25) {
            Button spawn = findViewById(R.id.spwn);
            spawn.setEnabled(false);
        } if (user.getRange() == 25) {
            Button rate = findViewById(R.id.rng);
            rate.setEnabled(false);
        } if (user.getProximity() == 250) {
            Button range = findViewById(R.id.prox);
            range.setEnabled(false);
        }
    }

    protected void goToHome(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void goToCodex(View v){
        Intent intent = new Intent(this, CodexPage.class);
        startActivity(intent);
    }

    protected void goToMap(View v){
        Intent intent = new Intent(this, MapsActivity2.class);
        startActivity(intent);
    }

    protected void spawn(View v){
        ProgressBar rateBar = findViewById(R.id.spawnBar);
        int rare = user.getRarerate()+1;
        rateBar.setProgress(rare);
        user.setRarerate(rare);
        TextView infoRate = findViewById(R.id.rareInfo);
        infoRate.setText(rare + "/25");
        int updatesAvailable = DataHolder.getInstance().getCurrentUser().getUpgradeAvailable();
        user.setUpgradeAvailable(updatesAvailable--);
        DataHolder.getInstance().setCurrentUser(user);
        DatabaseManager.updateBD(DataHolder.getInstance().getCurrentUser());
    }

    protected void proximity(View v){
        ProgressBar proximity = findViewById(R.id.proximityBar);
        int prox = 250-(DataHolder.getInstance().getCurrentUser().getProximity()+10);
        proximity.setProgress(prox);
        TextView infoProx = findViewById(R.id.spawnInfo);
        infoProx.setText(prox + "/250");
        user.setProximity(prox);
        int updatesAvailable = user.getUpgradeAvailable();
        user.setUpgradeAvailable(updatesAvailable--);
        DataHolder.getInstance().setCurrentUser(user);
        DatabaseManager.updateBD(user);
    }

    protected void points(View v){
        int nmobs = DataHolder.getInstance().getCurrentUser().getNmobs()+1;
        ProgressBar nmobsBar = findViewById(R.id.pointsBar);
        nmobsBar.setProgress(nmobs);
        TextView infoMobs = findViewById(R.id.pointsInfo);
        infoMobs.setText(nmobs + "/8");
        DataHolder.getInstance().getCurrentUser().setNmobs(nmobs);
        int updatesAvailable = DataHolder.getInstance().getCurrentUser().getUpgradeAvailable();
        user.setUpgradeAvailable(updatesAvailable--);
        DataHolder.getInstance().setCurrentUser(user);
        DatabaseManager.updateBD(user);
    }

    protected void range(View v){
        int range = user.getRange()+1;
        ProgressBar rangeBar = findViewById(R.id.rangeBar);
        rangeBar.setProgress(range);
        TextView infoRange = findViewById(R.id.rangeInfo);
        infoRange.setText(range + "/25");
        user.setRange(range);
        int updatesAvailable = user.getUpgradeAvailable();
        user.setUpgradeAvailable(updatesAvailable--);
        DataHolder.getInstance().setCurrentUser(user);
        DatabaseManager.updateBD(user);
    }
}
