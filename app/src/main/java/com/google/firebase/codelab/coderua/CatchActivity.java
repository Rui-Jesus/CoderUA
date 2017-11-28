package com.google.firebase.codelab.coderua;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CatchActivity extends AppCompatActivity {

    private static final String TAG = "CatchActivity";

    private static final int CLICK_DURATION = 1000; //This comes in miliseconds.

    //To mark if this activity is still running
    static boolean notActive = true;

    private ImageView iv;
    private TextView tv;
    private int action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //App booted, it's active
        notActive = false;

        setContentView(R.layout.activity_catch);

        final int mobID = getIntent().getIntExtra("mobID", 0);
        iv = (ImageView)findViewById(R.id.myView);
        tv = (TextView)findViewById(R.id.action);

        iv.setImageBitmap(MobsHolder.getInstance(this).getMobById(mobID).getImage());

        double r = Math.random();

        //Depending on the action, we tell the user which activity to do, and set the action flag to select behaviour below.
        if(r > 0.5) { action = 1; tv.setText(getResources().getString(R.string.hold));}
        else{action = 0;}

        /* We create an alert dialog here to be used below  */
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage( getResources().getString(R.string.you_caught_a_monster) +
                "\n" +
                "\t " +  getResources().getString(R.string.mob_is) + " " +
                MobsHolder.getInstance(this).getMobById(mobID).getName());
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                R.string.OK,
                new DialogInterface.OnClickListener() {
                    User user = DataHolder.getInstance().getCurrentUser();
                    public void onClick(DialogInterface dialog, int id) {
                        int percentage = user.getPercentage();

                        //If the user is low leveled, he levels up fast
                        if(user.getLevel()<=5)
                            percentage += 50;

                        //If the user is between 5 and 10, receives 100 - n * 30
                        //Where n is in % 100 - level * 3
                        if(5 < user.getLevel() && user.getLevel() <= 10)
                            percentage += Math.abs(1- 3*user.getLevel() * 30);

                        if(user.getLevel() > 10)
                            percentage += Math.abs(1- 2*user.getLevel() * 20);

                        user.addModCaught(mobID);
                        if(percentage>=100){ //He leveled up
                            percentage = percentage - 100; //Percentage that remains
                            user.setLevel(user.getLevel() + 1); //Got a level
                            user.setUpgradeAvailable(user.getUpgradeAvailable() + 1); //Got a new point to spend
                        }
                        user.setPercentage(percentage);
                        //We update the DataHolder
                        DataHolder.getInstance().setCurrentUser(user);
                        //We update the dataBase
                        DatabaseManager.updateBD(user);

                        //Once the user finishes the action, we leave the activity, back to the previous one in the stack
                        finish();
                        dialog.cancel();
                    }
                });

        iv.setOnTouchListener(new View.OnTouchListener() {
            int nTaps = 0; //To record the number of times the user taps the screen
            double t1 = 0; double t2 = 0; //To record the press and release time in case of long click

            public boolean onTouch(View v, MotionEvent event) {

                //We want tap events
                if(action == 0){
                    nTaps++;
                    if(nTaps == 4) {
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }

                }
                //We want long click events
                else if(action == 1){
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN: //We record the time he clicks on the screen
                            t1 = System.currentTimeMillis();

                        case MotionEvent.ACTION_UP:
                            t2 = System.currentTimeMillis();

                            if ((t2 - t1) >= CLICK_DURATION){
                                double aux = t2-t1;
                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            }
                    }
                }
                return true;
            }

        });

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //When finish() is called, this is the method that is executed imediately afterwards
        notActive = true;
    }
}
