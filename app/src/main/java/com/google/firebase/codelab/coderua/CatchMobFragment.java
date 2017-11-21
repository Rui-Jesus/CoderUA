package com.google.firebase.codelab.coderua;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by ruigr on 21/11/2017.
 */

public class CatchMobFragment extends DialogFragment {

    //We must create these variables using bundles it seems
    //private String mobName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.catchMob + "\n" + "\t The mob is Ctos")
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
        ;
        // Create the AlertDialog object and return it
        return builder.create();
    }
}