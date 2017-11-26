package com.google.firebase.codelab.coderua;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class CodexPage extends AppCompatActivity {


    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codex_page);
        HashMap<Integer, Mob> map = MobsHolder.getInstance(this).getAppMobs();
        Set<Integer> mobs = map.keySet();
        ArrayList<String> web = new ArrayList<>();
        ArrayList<Bitmap> images = new ArrayList<>();
        for (Iterator<Integer> it = mobs.iterator(); it.hasNext(); ) {
            int f = it.next();
            Mob b = map.get(f);
            web.add(b.getName());
            images.add(b.getImage());
        }
        CustomList adapter = new CustomList(CodexPage.this, web, images);
        list = findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PopUp();
            }

            private void PopUp() {
                AlertDialog alertDialog = new AlertDialog.Builder(CodexPage.this).create();
                alertDialog.setTitle("Name of the mob");
                alertDialog.setMessage("info to be shown");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

        });
        ProgressBar bar = findViewById(R.id.levelBar);
        TextView level = findViewById(R.id.levelText);
        String text = level.getText() + "" + DataHolder.getInstance().getCurrentUser().getLevel();
        level.setText(text);
        TextView username = findViewById(R.id.username);
        username.setText(DataHolder.getInstance().getCurrentUser().getUid());
        Button pressed = findViewById(R.id.codex);
        pressed.setEnabled(false);
        pressed.setTextColor(Color.parseColor("#000000"));

    }

    protected void goToHome(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void goToSkills(View v){
        Intent intent = new Intent(this, SkillsPage.class);
        startActivity(intent);
    }

    protected void goToMap(View v){
        Intent intent = new Intent(this, MapsActivity2.class);
        startActivity(intent);
    }

}
