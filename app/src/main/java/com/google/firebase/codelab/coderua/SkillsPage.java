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
        ProgressBar range = (ProgressBar) findViewById(R.id.rangeBar);
        range.setProgress(100);
        ProgressBar bar = (ProgressBar) findViewById(R.id.levelBar);
        bar.setProgress(90);
        TextView level = (TextView) findViewById(R.id.level);
        level.setText("Level 10");
        TextView username = (TextView) findViewById(R.id.username);
        username.setText("SnowCharizard:HowToBeAutist101");
        Button pressed = (Button) findViewById(R.id.skills);
        pressed.setEnabled(false);
        pressed.setTextColor(Color.parseColor("#000000"));
    }


    protected void goToHome(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void goToCodex(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void goToMap(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
