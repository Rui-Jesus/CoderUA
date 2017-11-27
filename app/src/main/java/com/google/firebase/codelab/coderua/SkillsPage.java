package com.google.firebase.codelab.coderua;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SkillsPage extends AppCompatActivity {

    private User user;

    private int auxPtsToSpend;
    private int auxRange;
    private int auxPts;
    private int auxProx;
    private int auxSpawn;
    private CharSequence txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills_page);
        ProgressBar proximity = findViewById(R.id.proximityBar);
        user = DataHolder.getInstance().getCurrentUser();
        int prox = 250-user.getProximity();
        auxProx = prox;
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
        auxRange = range;
        ProgressBar rangeBar = findViewById(R.id.rangeBar);
        rangeBar.setProgress(range);
        TextView infoRange = findViewById(R.id.rangeInfo);
        infoRange.setText(range + "/25");
        int rate = user.getRarerate();
        auxSpawn = rate;
        ProgressBar rateBar = findViewById(R.id.spawnBar);
        rateBar.setProgress(rate);
        TextView infoRate = findViewById(R.id.rareInfo);
        infoRate.setText(rate + "/25");
        int nmobs = user.getNmobs();
        auxPts = nmobs;
        ProgressBar nmobsBar = findViewById(R.id.pointsBar);
        nmobsBar.setProgress(nmobs);
        TextView infoMobs = findViewById(R.id.pointsInfo);
        infoMobs.setText(nmobs + "/8");
        TextView level = findViewById(R.id.levelText);
        String text = level.getText() + "" + DataHolder.getInstance().getCurrentUser().getLevel();
        level.setText(text);
        TextView username = findViewById(R.id.username);
        username.setText(DataHolder.getInstance().getCurrentUser().getUid());
        Button pressed = findViewById(R.id.skills);
        pressed.setEnabled(false);
        pressed.setTextColor(Color.parseColor("#000000"));
        auxPtsToSpend = DataHolder.getInstance().getCurrentUser().getUpgradeAvailable();
        TextView ptsSpend = findViewById(R.id.stringpts);
        txt=ptsSpend.getText();
        String newtxt=txt.toString()+" "+auxPtsToSpend;
        ptsSpend.setText(newtxt);
        checkLevel();
        Button b = findViewById(R.id.saveButton);
        b.setVisibility(View.VISIBLE);
        b.setEnabled(true);
    }

    private void checkLevel(){
        if (auxPtsToSpend==0) {
            Button points = findViewById(R.id.pts);
            points.setEnabled(false);
            Button spawn = findViewById(R.id.spwn);
            spawn.setEnabled(false);
            Button rate = findViewById(R.id.rng);
            rate.setEnabled(false);
            Button range = findViewById(R.id.prox);
            range.setEnabled(false);
        }
        if (auxPts == 8){
            Button points = findViewById(R.id.pts);
            points.setEnabled(false);
        } if (auxSpawn == 25) {
            Button spawn = findViewById(R.id.spwn);
            spawn.setEnabled(false);
        } if (auxRange == 25) {
            Button rate = findViewById(R.id.rng);
            rate.setEnabled(false);
        } if (auxProx == 250) {
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

    private void toDB(){
        User user = DataHolder.getInstance().getCurrentUser();
        user.setRarerate(auxSpawn);
        user.setProximity(auxProx);
        user.setNmobs(auxPts);
        user.setUpgradeAvailable(auxPtsToSpend);
        user.setRange(auxRange);
        DataHolder.getInstance().setCurrentUser(user);
        DatabaseManager.updateBD(user);
        Button b = findViewById(R.id.saveButton);
        b.setVisibility(View.GONE);
    }

    protected void spawn(View v){
        auxSpawn++;
        auxPtsToSpend--;
        Button b = findViewById(R.id.saveButton);
        b.setVisibility(View.VISIBLE);
        TextView ptsSpend = findViewById(R.id.stringpts);
        ptsSpend.setText(txt+" "+auxPtsToSpend);
        ProgressBar rateBar = findViewById(R.id.spawnBar);
        rateBar.setProgress(auxSpawn);
        TextView infoRate = findViewById(R.id.rareInfo);
        infoRate.setText(auxSpawn + "/25");
        checkLevel();
    }

    protected void proximity(View v){
        ProgressBar proximity = findViewById(R.id.proximityBar);
        auxProx +=10;
        auxPtsToSpend--;
        Button b = findViewById(R.id.saveButton);
        b.setVisibility(View.VISIBLE);
        TextView ptsSpend = findViewById(R.id.stringpts);
        ptsSpend.setText(txt+" "+auxPtsToSpend);
        proximity.setProgress(auxProx);
        TextView infoProx = findViewById(R.id.spawnInfo);
        infoProx.setText(auxProx + "/250");
        checkLevel();
    }

    protected void points(View v){
        auxPts++;
        auxPtsToSpend--;
        Button b = findViewById(R.id.saveButton);
        b.setVisibility(View.VISIBLE);
        TextView ptsSpend = findViewById(R.id.stringpts);
        ptsSpend.setText(txt+" "+auxPtsToSpend);
        ProgressBar nmobsBar = findViewById(R.id.pointsBar);
        nmobsBar.setProgress(auxPts);
        TextView infoMobs = findViewById(R.id.pointsInfo);
        infoMobs.setText(auxPts + "/8");
        checkLevel();
    }

    protected void range(View v){
        auxRange++;
        auxPtsToSpend--;
        Button b = findViewById(R.id.saveButton);
        b.setVisibility(View.VISIBLE);
        TextView ptsSpend = findViewById(R.id.stringpts);
        ptsSpend.setText(txt+" "+auxPtsToSpend);
        ProgressBar rangeBar = findViewById(R.id.rangeBar);
        rangeBar.setProgress(auxRange);
        TextView infoRange = findViewById(R.id.rangeInfo);
        infoRange.setText(auxRange + "/25");
        checkLevel();
    }

    protected void saveData(View v){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.question);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        toDB();
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        restoreData();
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        Button b = findViewById(R.id.saveButton);
        b.setVisibility(View.GONE);
    }

    private void restoreData() {
        User user = DataHolder.getInstance().getCurrentUser();
        auxPts = user.getNmobs();
        ProgressBar nmobsBar = findViewById(R.id.pointsBar);
        nmobsBar.setProgress(auxPts);
        auxPtsToSpend = user.getUpgradeAvailable();
        TextView ptsSpend = findViewById(R.id.stringpts);
        ptsSpend.setText(txt+" "+auxPtsToSpend);
        auxRange = user.getRange();
        ProgressBar rangeBar = findViewById(R.id.rangeBar);
        rangeBar.setProgress(auxRange);
        auxProx = user.getProximity();
        ProgressBar proximity = findViewById(R.id.proximityBar);
        proximity.setProgress(auxProx);
        auxSpawn = user.getRarerate();
        ProgressBar rateBar = findViewById(R.id.spawnBar);
        rateBar.setProgress(auxSpawn);

    }
}
