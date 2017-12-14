package edu.illinois.finalproject;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Provides abstract methods to handle
 * Derived from https://stackoverflow.com/questions/33723139/wait-firebase-async-retrive-data-in-android
 */
public interface OnGetDataListener {
    public void onStart();
    /**Takes in/handles DataSnapshot after it is downloaded from Firebase.*/
    public void onSuccess(DataSnapshot dataSnapshot);
    /**Takes in /handles DatabaseError if one occurs while downloading data from Firebase*/
    public void onFailed(DatabaseError databaseError);
}
