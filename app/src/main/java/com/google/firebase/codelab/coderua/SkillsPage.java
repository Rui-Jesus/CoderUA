package com.google.firebase.codelab.coderua;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SkillsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills_page);
        ProgressBar proximity = findViewById(R.id.proximityBar);
        int prox = 250-DataHolder.getInstance().getCurrentUser().getProximity();
        proximity.setProgress(prox);
        TextView infoProx = findViewById(R.id.spawnInfo);
        infoProx.setText(prox + "/250");
        ProgressBar bar = findViewById(R.id.levelBar);
        int percentage  =DataHolder.getInstance().getCurrentUser().getPercentage();
        if (percentage == -1){
            bar.setProgress(0);
        } else {
            bar.setProgress(percentage);
        }
        int range = DataHolder.getInstance().getCurrentUser().getRange();
        ProgressBar rangeBar = findViewById(R.id.rangeBar);
        rangeBar.setProgress(range);
        TextView infoRange = findViewById(R.id.rangeInfo);
        infoRange.setText(range + "/25");
        int rate = DataHolder.getInstance().getCurrentUser().getRarerate();
        ProgressBar rateBar = findViewById(R.id.spawnBar);
        rateBar.setProgress(rate);
        TextView infoRate = findViewById(R.id.rareInfo);
        infoRate.setText(rate + "/25");
        int nmobs = DataHolder.getInstance().getCurrentUser().getNmobs();
        ProgressBar nmobsBar = findViewById(R.id.pointsBar);
        nmobsBar.setProgress(nmobs);
        TextView infoMobs = findViewById(R.id.pointsInfo);
        infoMobs.setText(nmobs + "/8");
        TextView level = findViewById(R.id.level);
        level.setText(R.string.level+DataHolder.getInstance().getCurrentUser().getLevel());
        TextView username = findViewById(R.id.username);
        username.setText(DataHolder.getInstance().getCurrentUser().getUid());
        Button pressed = findViewById(R.id.skills);
        pressed.setEnabled(false);
        pressed.setTextColor(Color.parseColor("#000000"));
        checkLevel();
    }

    private void checkLevel(){
        if (DataHolder.getInstance().getCurrentUser().getUpgradeAvailable()==0) {
            Button points = findViewById(R.id.pts);
            points.setEnabled(false);
            Button spawn = findViewById(R.id.spwn);
            spawn.setEnabled(false);
            Button rate = findViewById(R.id.rng);
            rate.setEnabled(false);
            Button range = findViewById(R.id.prox);
            range.setEnabled(false);
        }
        if (DataHolder.getInstance().getCurrentUser().getNmobs() == 8){
            Button points = findViewById(R.id.pts);
            points.setEnabled(false);
        } if (DataHolder.getInstance().getCurrentUser().getRarerate() == 25) {
            Button spawn = findViewById(R.id.spwn);
            spawn.setEnabled(false);
        } if (DataHolder.getInstance().getCurrentUser().getRange() == 25) {
            Button rate = findViewById(R.id.rng);
            rate.setEnabled(false);
        } if (DataHolder.getInstance().getCurrentUser().getProximity() == 250) {
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
        int rare = DataHolder.getInstance().getCurrentUser().getRarerate()+1;
        rateBar.setProgress(rare);
        DataHolder.getInstance().getCurrentUser().setRarerate(rare);
        TextView infoRate = findViewById(R.id.rareInfo);
        infoRate.setText(rare + "/25");
        int updatesAvailable = DataHolder.getInstance().getCurrentUser().getUpgradeAvailable();
        DataHolder.getInstance().getCurrentUser().setUpgradeAvailable(updatesAvailable--);
    }

    protected void proximity(View v){
        ProgressBar proximity = findViewById(R.id.proximityBar);
        int prox = 250-(DataHolder.getInstance().getCurrentUser().getProximity()+10);
        proximity.setProgress(prox);
        TextView infoProx = findViewById(R.id.spawnInfo);
        infoProx.setText(prox + "/250");
        DataHolder.getInstance().getCurrentUser().setProximity(prox);
        int updatesAvailable = DataHolder.getInstance().getCurrentUser().getUpgradeAvailable();
        DataHolder.getInstance().getCurrentUser().setUpgradeAvailable(updatesAvailable--);
    }

    protected void points(View v){
        int nmobs = DataHolder.getInstance().getCurrentUser().getNmobs()+1;
        ProgressBar nmobsBar = findViewById(R.id.pointsBar);
        nmobsBar.setProgress(nmobs);
        TextView infoMobs = findViewById(R.id.pointsInfo);
        infoMobs.setText(nmobs + "/8");
        DataHolder.getInstance().getCurrentUser().setNmobs(nmobs);
        int updatesAvailable = DataHolder.getInstance().getCurrentUser().getUpgradeAvailable();
        DataHolder.getInstance().getCurrentUser().setUpgradeAvailable(updatesAvailable--);
    }

    protected void range(View v){
        int range = DataHolder.getInstance().getCurrentUser().getRange()+1;
        ProgressBar rangeBar = findViewById(R.id.rangeBar);
        rangeBar.setProgress(range);
        TextView infoRange = findViewById(R.id.rangeInfo);
        infoRange.setText(range + "/25");
        DataHolder.getInstance().getCurrentUser().setRange(range);
        int updatesAvailable = DataHolder.getInstance().getCurrentUser().getUpgradeAvailable();
        DataHolder.getInstance().getCurrentUser().setUpgradeAvailable(updatesAvailable--);
    }
}
