package com.google.firebase.codelab.coderua;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList<String> web;
    private final ArrayList<Mob> mobs;
    public CustomList(Activity context,
                      ArrayList<String> web, ArrayList<Mob> mobs) {
        super(context, R.layout.list_single, web);
        this.context = context;
        this.web = web;
        this.mobs = mobs;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        TextView idTitle = (TextView) rowView.findViewById(R.id.mobinfo);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(web.get(position));
        if (web.get(position).equals("??????")) {
            idTitle.setText("???");
        } else {
            idTitle.setText("" + mobs.get(position).getMobID());
        }
        imageView.setImageBitmap(mobs.get(position).getImage());
        return rowView;
    }
}