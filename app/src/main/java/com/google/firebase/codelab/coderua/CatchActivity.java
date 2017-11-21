package com.google.firebase.codelab.coderua;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class CatchActivity extends AppCompatActivity {

    //To count the taps
    public int nTaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch);

        nTaps = 0;

        /* We create an alert dialog here to be used below  */
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(R.string.catchMob + "\n" + "\t The mob is Ctos");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                R.string.OK,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        View myView = findViewById(R.id.myView);
        myView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // ... Respond to touch events
                nTaps++;
                if(nTaps == 3){
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                return true;
            }
        });

        //After showing the dialog and so, we want to finish the activity
        finish();

    }
}
