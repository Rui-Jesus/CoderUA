package com.google.firebase.codelab.coderua;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class CatchActivity extends AppCompatActivity {

    //To count the taps
    public int nTaps;

    //To mark if this activity is still running
    static boolean notActive = true;

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch);

        int mobID = getIntent().getIntExtra("mobID", 0);
        iv = (ImageView)findViewById(R.id.myView);
        iv.setImageBitmap(MobsHolder.getInstance(this).getMobById(mobID).getImage());

        //App booted, it's active
        notActive = false;

        nTaps = 0;

        /* We create an alert dialog here to be used below  */
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(R.string.catchMob + "\n" + "\t The mob is Ctos");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                R.string.OK,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Once the user finishes the action, we leave the activity, back to the previous one in the stack
                        finish();
                        dialog.cancel();
                    }
                });

        iv.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // ... Respond to touch events
                nTaps++;
                if(nTaps == 4){
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
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
