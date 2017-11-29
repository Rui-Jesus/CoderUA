package com.google.firebase.codelab.coderua;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class CatchActivity extends AppCompatActivity implements View.OnDragListener {

    private static final String TAG = "CatchActivity";

    private static final int CLICK_DURATION = 1000; //This comes in miliseconds.

    //To mark if this activity is still running
    static boolean notActive = true;

    private AlertDialog.Builder builder1;

    private ImageView iv;
    private TextView tv;
    private int action;
    private ProgressBar progBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //App booted, it's active
        notActive = false;

        setContentView(R.layout.activity_catch);

        final int mobID = getIntent().getIntExtra("mobID", 0);
        iv = (ImageView)findViewById(R.id.myView);
        tv = (TextView)findViewById(R.id.action);
        progBar = (ProgressBar)findViewById(R.id.progressBar);
        progBar.setVisibility(View.INVISIBLE);

        iv.setImageBitmap(MobsHolder.getInstance(this).getMobById(mobID).getImage());

        double r = Math.random();

        //Depending on the action, we tell the user which activity to do, and set the action flag to select behaviour below.
        if(r >= 0.66) { action = 2;} //tap event
        else if(r<0.3 && r<0.66) {action = 1; tv.setText(getResources().getString(R.string.hold)); } //hold event
        else{action = 0; tv.setText(getResources().getString(R.string.drag)); } //drag event

        /* We create an alert dialog here to be used below  */
        builder1 = new AlertDialog.Builder(this);
        builder1.setMessage( getResources().getString(R.string.you_caught_a_monster) +
                "\n" +
                "\t " +  getResources().getString(R.string.mob_is) + " " +
                MobsHolder.getInstance(this).getMobById(mobID).getName());
        builder1.setCancelable(false);

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
                        else if(5 < user.getLevel() && user.getLevel() <= 10)
                            percentage += Math.abs( (1 - (3*user.getLevel()/100)) * 30 );

                        else if(user.getLevel() > 10)
                            percentage += Math.abs( (1 - (2*user.getLevel()/100)) * 20 );

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

        //Set up progress bar

        if(action == 1){
            progBar.setMax(CLICK_DURATION);
            progBar.setVisibility(View.VISIBLE);
            findViewById(R.id.container).setVisibility(View.GONE);
        }
        else if(action == 2){
            progBar.setMax(4);
            progBar.setVisibility(View.VISIBLE);
            findViewById(R.id.container).setVisibility(View.GONE);
        }
        else{
            findViewById(R.id.container).setOnDragListener(this);
        }

        iv.setOnTouchListener(new View.OnTouchListener() {
            int nTaps = 0; //To record the number of times the user taps the screen
            double t1 = 0; double t2 = 0; //To record the press and release time in case of long click

            public boolean onTouch(View v, MotionEvent event) {

                //We want tap events
                if(action == 2 && event.getAction() ==  MotionEvent.ACTION_DOWN){
                    nTaps++;
                    progBar.setProgress(progBar.getProgress() + 1);
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
                            double d = Math.ceil(t2-t1);
                            progBar.setProgress((int)(d));

                            if ((t2 - t1) >= CLICK_DURATION){
                                double aux = t2-t1;
                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            }
                    }

                }

                else if(action == 0){
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                        v.startDrag(null, shadowBuilder, v, 0);
                        v.setVisibility(View.INVISIBLE);
                    } else {
                        return false;
                    }
                }
                return true;
            }

        });

    }

    @Override
    public boolean onDrag(View v, DragEvent e) {

        if (e.getAction() == DragEvent.ACTION_DROP) {

            if(v.getId() == R.id.container){
                View view = (View) e.getLocalState();
                ViewGroup from = (ViewGroup) view.getParent();
                from.removeView(view);
                LinearLayout to = (LinearLayout) v;
                to.addView(view);
                view.setVisibility(View.VISIBLE);

                //This was the correct action, we want to finish
                AlertDialog alert11 = builder1.create();
                alert11.show();
                return true;
            }

            else{
                View view = (View) e.getLocalState();
                ViewGroup from = (ViewGroup) view.getParent();
                from.addView(view);
                view.setVisibility(View.VISIBLE);
            }

        }

        return true;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //When finish() is called, this is the method that is executed imediately afterwards
        notActive = true;
    }

    //NOT IN USE
    // The types specified here are the input data type, the progress type, and the result type
    /**
     * A private class to handle some quick jobs that the map needs. Used to prevent blocking the UI thread
     */
    private class MyAsyncTask extends AsyncTask<Context, Integer, String> {

        @Override
        protected String doInBackground(Context... params) {
            // Some long-running task like downloading an image.
            double t1 = System.currentTimeMillis();
            double t2;
            int i = 0;
            while(true){
                t2 = System.currentTimeMillis();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i = i +5;
                publishProgress(i);
                if ((t2 - t1) >= CLICK_DURATION)
                    break; //Click lasted long enough we finished
            }

            return null;
        }

        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progBar.setProgress(values[0]);
            // Executes whenever publishProgress is called from doInBackground
            // Used to update the progress indicator
        }

        protected void onPostExecute() {
            Log.i(TAG, "REACHED ON POST EXECUTE");
            AlertDialog alert11 = builder1.create();
            alert11.show();
            // This method is executed in the UIThread
            // with access to the result of the long running task
        }

    }

}
