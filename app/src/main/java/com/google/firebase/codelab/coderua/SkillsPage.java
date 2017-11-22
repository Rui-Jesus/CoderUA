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
        proximity.setProgress(250-DataHolder.getInstance().getCurrentUser().getProximity());
        ProgressBar bar = findViewById(R.id.levelBar);
        bar.setProgress(DataHolder.getInstance().getCurrentUser().getPercentage());
        ProgressBar range = findViewById(R.id.rangeBar);
        range.setProgress(DataHolder.getInstance().getCurrentUser().getRange());
        ProgressBar rate = findViewById(R.id.spawnBar);
        rate.setProgress(DataHolder.getInstance().getCurrentUser().getRarerate());
        ProgressBar nmobsBar = findViewById(R.id.pointsBar);
        nmobsBar.setProgress(DataHolder.getInstance().getCurrentUser().getNmobs());
        TextView level = findViewById(R.id.level);
        level.setText("Lvl. "+DataHolder.getInstance().getCurrentUser().getLevel());
        TextView username = findViewById(R.id.username);
        username.setText(DataHolder.getInstance().getCurrentUser().getUid());
        Button pressed = findViewById(R.id.skills);
        pressed.setEnabled(false);
        pressed.setTextColor(Color.parseColor("#000000"));
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

}
