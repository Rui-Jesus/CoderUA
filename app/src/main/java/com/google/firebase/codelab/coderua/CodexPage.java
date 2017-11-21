package com.google.firebase.codelab.coderua;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CodexPage extends AppCompatActivity {


        ListView list;
        String[] web = {
                "Python",
                "Java",
                "HTML",
                "C"
        } ;
        Integer[] imageId = {
                R.drawable.gear,
                R.drawable.logout,
                R.drawable.home,
                R.drawable.list
        };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_codex_page);
            CustomList adapter = new CustomList(CodexPage.this, web, imageId);
            list=(ListView)findViewById(R.id.list);
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
            ProgressBar bar = (ProgressBar) findViewById(R.id.levelBar);
            bar.setProgress(90);
            TextView level = (TextView) findViewById(R.id.level);
            level.setText("Level 10");
            TextView username = (TextView) findViewById(R.id.username);
            username.setText("SnowCharizard:HowToBeAutist101");
            Button pressed = (Button) findViewById(R.id.codex);
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
