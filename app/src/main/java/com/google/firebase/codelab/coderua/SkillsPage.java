package com.google.firebase.codelab.coderua;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.RotateDrawable;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

public class SkillsPage extends AppCompatActivity {

    private User user;
    private boolean canSave;
    private int auxPtsToSpend;
    private int auxRange;
    private int auxPts;
    private int auxProx;
    private int auxSpawn;
    private int percentage;
    private CharSequence txt;
    private Button b;
    private static final String SPAWN_DATA = "spawn";
    private static final String POINTS_DATA = "points";
    private static final String RANGE_DATA = "range";
    private static final String PROX_DATA = "prox";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        canSave = false;
        setContentView(R.layout.activity_skills_page);
        user = DataHolder.getInstance().getCurrentUser();
        auxProx  = 250-user.getProximity();
        percentage = user.getPercentage();
        auxRange = user.getRange();
        auxSpawn = user.getRarerate();
        auxPts = user.getNmobs();
        auxPtsToSpend = user.getUpgradeAvailable();

    }

    @Override
    protected void onSaveInstanceState(Bundle onState) {
        onState.putInt(SPAWN_DATA, auxSpawn);
        onState.putInt(POINTS_DATA, auxPts);
        onState.putInt(RANGE_DATA, auxRange);
        onState.putInt(PROX_DATA, auxProx);
        super.onSaveInstanceState(onState);
    }

    protected void onRestoreInstanceState(Bundle onState) {
        super.onRestoreInstanceState(onState);
        auxPts = onState.getInt(POINTS_DATA);
        auxProx = onState.getInt(PROX_DATA);
        auxSpawn = onState.getInt(SPAWN_DATA);
        auxRange = onState.getInt(RANGE_DATA);
    }

    @Override
    protected void onResume(){
        super.onResume();
        setContentView(R.layout.activity_skills_page);
        fillLayout();
    }

    private void fillLayout() {
        ProgressBar proximity = findViewById(R.id.proximityBar);
        /*Next wall of code is to set up all the progress bars*/
        proximity.setProgress(auxProx);
        TextView infoProx = findViewById(R.id.spawnInfo);
        infoProx.setText(auxProx + "/250");
        ProgressBar bar = findViewById(R.id.levelBar);
        bar.setProgress(percentage);
        ProgressBar rangeBar = findViewById(R.id.rangeBar);
        rangeBar.setProgress(auxRange-15);
        TextView infoRange = findViewById(R.id.rangeInfo);
        infoRange.setText(auxRange + "/25");
        ProgressBar rateBar = findViewById(R.id.spawnBar);
        rateBar.setProgress(auxSpawn-5);
        TextView infoRate = findViewById(R.id.rareInfo);
        infoRate.setText(auxSpawn + "/25");
        ProgressBar nmobsBar = findViewById(R.id.pointsBar);
        nmobsBar.setProgress(auxPts-3);
        TextView infoMobs = findViewById(R.id.pointsInfo);
        infoMobs.setText(auxPts + "/8");
        TextView level = findViewById(R.id.levelText);
        String text = level.getText() + "" + user.getLevel();
        level.setText(text);
        TextView username = findViewById(R.id.username);
        username.setText(user.getUid());
        Button pressed = findViewById(R.id.skills);
        pressed.setEnabled(false);
        pressed.setTextColor(Color.parseColor("#000000"));
        TextView ptsSpend = findViewById(R.id.stringpts);
        txt=ptsSpend.getText();
        String newtxt=txt.toString()+" "+auxPtsToSpend;
        ptsSpend.setText(newtxt);
        checkLevel();
        b = findViewById(R.id.saveButton);
        //User hasn´t spend any points yet, he cannot see nor use this button
        if (!canSave) {
            b.setVisibility(View.GONE);
        }
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

    protected void logoutClick(View v){
        FirebaseAuth mFirebaseAuth = DataHolder.getInstance().getmFirebaseAuth();
        GoogleApiClient mGoogleApiClient = DataHolder.getInstance().getmGoogleApiClient();
        mFirebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        //We want to stop the running service
        stopService(new Intent(SkillsPage.this, LocationService.class));
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    private void toDB(){

        /* The user might not have spent all the points we need to check that */
        user.setUpgradeAvailable(auxPtsToSpend);
        user.setRarerate(auxSpawn);
        user.setProximity(user.getProximity() - auxProx);
        user.setNmobs(auxPts);
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
        String newtxt=txt.toString()+" "+auxPtsToSpend;
        ptsSpend.setText(newtxt);
        ProgressBar rateBar = findViewById(R.id.spawnBar);
        rateBar.setProgress(auxSpawn-5);
        TextView infoRate = findViewById(R.id.rareInfo);
        infoRate.setText(auxSpawn + "/25");
        canSave = true;
        checkLevel();
    }

    protected void proximity(View v){
        ProgressBar proximity = findViewById(R.id.proximityBar);
        auxProx +=10; //The mobs spawn closer the more points the user spends here
        auxPtsToSpend--;
        Button b = findViewById(R.id.saveButton);
        b.setVisibility(View.VISIBLE);
        TextView ptsSpend = findViewById(R.id.stringpts);
        String newtxt=txt.toString()+" "+auxPtsToSpend;
        ptsSpend.setText(newtxt);
        proximity.setProgress(auxProx);
        TextView infoProx = findViewById(R.id.spawnInfo);
        infoProx.setText(auxProx + "/250");
        canSave = true;
        checkLevel();
    }

    protected void points(View v){
        auxPts++;
        auxPtsToSpend--;
        Button b = findViewById(R.id.saveButton);
        b.setVisibility(View.VISIBLE);
        TextView ptsSpend = findViewById(R.id.stringpts);
        String newtxt=txt.toString()+" "+auxPtsToSpend;
        ptsSpend.setText(newtxt);
        ProgressBar nmobsBar = findViewById(R.id.pointsBar);
        nmobsBar.setProgress(auxPts-3);
        TextView infoMobs = findViewById(R.id.pointsInfo);
        infoMobs.setText(auxPts + "/8");
        canSave = true;
        checkLevel();
    }

    protected void range(View v){
        auxRange+=5;
        auxPtsToSpend--;
        Button b = findViewById(R.id.saveButton);
        b.setVisibility(View.VISIBLE);
        TextView ptsSpend = findViewById(R.id.stringpts);
        String newtxt=txt.toString()+" "+auxPtsToSpend;
        ptsSpend.setText(newtxt);
        ProgressBar rangeBar = findViewById(R.id.rangeBar);
        rangeBar.setProgress(auxRange-15);
        TextView infoRange = findViewById(R.id.rangeInfo);
        infoRange.setText(auxRange + "/25");
        canSave = true;
        checkLevel();
    }

    protected void saveData(View v){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.question);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        toDB();
                        checkLevel();
                        canSave = false;
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
        auxPts = user.getNmobs();
        ProgressBar nmobsBar = findViewById(R.id.pointsBar);
        nmobsBar.setProgress(auxPts-3);
        auxPtsToSpend = user.getUpgradeAvailable();
        TextView ptsSpend = findViewById(R.id.stringpts);
        String newtxt=txt.toString()+" "+auxPtsToSpend;
        ptsSpend.setText(newtxt);
        auxRange = user.getRange();
        ProgressBar rangeBar = findViewById(R.id.rangeBar);
        rangeBar.setProgress(auxRange-15);
        auxProx = user.getProximity();
        ProgressBar proximity = findViewById(R.id.proximityBar);
        proximity.setProgress(auxProx);
        auxSpawn = user.getRarerate();
        ProgressBar rateBar = findViewById(R.id.spawnBar);
        rateBar.setProgress(auxSpawn-5);
        Button b = findViewById(R.id.saveButton);
        canSave = false;
        b.setVisibility(View.GONE);

    }
}
