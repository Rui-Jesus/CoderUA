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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

public class SkillsPage extends AppCompatActivity {

    private User user;

    private int auxPtsToSpend;
    private int auxRange;
    private int auxPts;
    private int auxProx;
    private int auxSpawn;
    private CharSequence txt;
    private Button b;

    @Override
    protected void onSaveInstanceState(Bundle onState) {

        super.onSaveInstanceState(onState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills_page);
        ProgressBar proximity = findViewById(R.id.proximityBar);

        /*Next wall of code is to set up all the progress bars*/
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
        rangeBar.setProgress(range-15);
        TextView infoRange = findViewById(R.id.rangeInfo);
        infoRange.setText(range + "/25");
        int rate = user.getRarerate();
        auxSpawn = rate;
        ProgressBar rateBar = findViewById(R.id.spawnBar);
        rateBar.setProgress(rate-5);
        TextView infoRate = findViewById(R.id.rareInfo);
        infoRate.setText(rate + "/25");
        int nmobs = user.getNmobs();
        auxPts = nmobs;
        ProgressBar nmobsBar = findViewById(R.id.pointsBar);
        nmobsBar.setProgress(nmobs-3);
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
        b = findViewById(R.id.saveButton);
        //User hasn´t spend any points yet, he cannot see nor use this button
        b.setVisibility(View.GONE);
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

        //b.setEnabled(true);

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

        /* The user might not have spend all the points we need to check that */
        user.setUpgradeAvailable(auxPtsToSpend);
        user.setRarerate(auxSpawn);
        user.setProximity(auxProx);
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
        checkLevel();
    }

    protected void proximity(View v){
        ProgressBar proximity = findViewById(R.id.proximityBar);
        auxProx +=10;
        auxPtsToSpend--;
        Button b = findViewById(R.id.saveButton);
        b.setVisibility(View.VISIBLE);
        TextView ptsSpend = findViewById(R.id.stringpts);
        String newtxt=txt.toString()+" "+auxPtsToSpend;
        ptsSpend.setText(newtxt);
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
        String newtxt=txt.toString()+" "+auxPtsToSpend;
        ptsSpend.setText(newtxt);
        ProgressBar nmobsBar = findViewById(R.id.pointsBar);
        nmobsBar.setProgress(auxPts-3);
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
        String newtxt=txt.toString()+" "+auxPtsToSpend;
        ptsSpend.setText(newtxt);
        ProgressBar rangeBar = findViewById(R.id.rangeBar);
        rangeBar.setProgress(auxRange-15);
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
                        checkLevel();
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
        b.setVisibility(View.GONE);

    }
}
