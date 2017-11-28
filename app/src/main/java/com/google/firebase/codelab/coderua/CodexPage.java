package com.google.firebase.codelab.coderua;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class CodexPage extends AppCompatActivity {


    private ListView list;
    private Mob popUpMob;
    private ArrayList<Mob> mobs1;
    private boolean isCaught;
    private HashMap<Integer, Integer> nmobscaught = new HashMap<>();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCaught = true;
        setContentView(R.layout.activity_codex_page);
        HashMap<Integer, Mob> map = MobsHolder.getInstance(this).getAppMobs();
        user = DataHolder.getInstance().getCurrentUser();
        Set<Integer> mobs = map.keySet();
        ArrayList<String> web = new ArrayList<>();
        ArrayList<Integer> mobsCaught = user.getMobsCaught();
        if(mobsCaught == null)
            mobsCaught = new ArrayList<>();
        mobs1 = new ArrayList<>();
        boolean inList = false;
        for (Integer i: mobsCaught) {
            if(i != null){
                if (nmobscaught.containsKey(i)){
                    int n=nmobscaught.get(i);
                    Log.i("N da porra:", String.valueOf(n));
                    nmobscaught.put(i, n++);
                } else {
                    nmobscaught.put(i, 1);
                }
            }

        }
        for (Iterator<Integer> it = mobs.iterator(); it.hasNext(); ) {
            int f = it.next();
            for (Integer i: mobsCaught) {
                if (i==f) {
                    inList = true;
                    break;
                }
            }
            if (inList){
                Mob b = map.get(f);
                web.add(b.getName());
                mobs1.add(b);
                inList = false;
            } else {
                Mob b = map.get(f);
                web.add("??????");
                mobs1.add(b);
            }

        }
        final CustomList adapter = new CustomList(CodexPage.this, web, mobs1);
        list = findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                popUpMob = mobs1.get(i);
                String a = adapter.getItem(i);
                if (!a.equals("??????")){
                    PopUp();
                }
            }

            private void PopUp() {
                AlertDialog alertDialog = new AlertDialog.Builder(CodexPage.this).create();
                alertDialog.setTitle(popUpMob.getName());
                alertDialog.setMessage("Type: " + popUpMob.getType() + "\nNumber of mobs caught: " + nmobscaught.get(popUpMob.getMobID()));
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
        String text = level.getText() + "" + user.getLevel();
        level.setText(text);
        TextView username = findViewById(R.id.username);
        username.setText(user.getUid());
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

    protected void logoutClick(View v){
        FirebaseAuth mFirebaseAuth = DataHolder.getInstance().getmFirebaseAuth();
        GoogleApiClient mGoogleApiClient = DataHolder.getInstance().getmGoogleApiClient();
        mFirebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        //We want to stop the running service
        stopService(new Intent(CodexPage.this, LocationService.class));
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

}
