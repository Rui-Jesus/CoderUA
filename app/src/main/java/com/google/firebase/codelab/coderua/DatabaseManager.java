package com.google.firebase.codelab.coderua;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatabaseManager {

    private static DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private static User user;

    //get's the user's info
    static void setUser(final MainActivity mainClass, final String displayEmail, final String displayName){
        DatabaseReference ref= database.child("Users");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = "";
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.child("email").getValue(String.class).equals(displayEmail)) {
                        user = new User(ds.child("name").getValue(String.class), displayEmail, ds.child("level").getValue(Integer.class), ds.child("nmobs").getValue(Integer.class), ds.child("mobsCaught").getValue(ArrayList.class), ds.child("proximity").getValue(Integer.class), ds.child("rarerate").getValue(Integer.class), ds.child("range").getValue(Integer.class), ds.child("percentage").getValue(Integer.class), ds.child("updateAvailable").getValue(Integer.class));
                        break;
                    }
                }
                if(name.equals(""))
                    createUser(displayName, displayEmail);
                mainClass.fillLayout();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        ref.addValueEventListener(eventListener);
    }

    //function to create an user if it doesn't exist in the database
    private static void createUser(String name, String email){
        user =  new User(name, email);
        database.child("Users").child("" + email.hashCode()).updateChildren(user.getMap());
    }

    static User getUser(){
        return user;
    }

    static void updateBD (User userToUpdate){
        database.child("Users").child("" + userToUpdate.getEmail().hashCode()).updateChildren(user.getMap());
    }
}
